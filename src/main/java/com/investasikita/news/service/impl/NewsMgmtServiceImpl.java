package com.investasikita.news.service.impl;

import com.investasikita.news.domain.Article;
import com.investasikita.news.domain.ArticleQueue;
import com.investasikita.news.domain.ArticleTag;
import com.investasikita.news.domain.Headline;
import com.investasikita.news.domain.enumeration.ArticleStatus;
import com.investasikita.news.repository.*;
import com.investasikita.news.repository.search.*;
import com.investasikita.news.security.SecurityUtils;
import com.investasikita.news.service.*;
import com.investasikita.news.service.dto.ArticleDTO;
import com.investasikita.news.service.dto.ArticleQueueDTO;
import com.investasikita.news.service.dto.ArticleTagDTO;
import com.investasikita.news.service.dto.HeadlineDTO;
import com.investasikita.news.service.mapper.*;
import com.investasikita.news.web.rest.po.NewsPO;
import com.vladmihalcea.concurrent.Retry;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class NewsMgmtServiceImpl implements NewsMgmtService {

    private final Logger log = LoggerFactory.getLogger(NewsMgmtServiceImpl.class);

    private final NewsRepository newsRepository;
    private final NewsSearchRepository newsSearchRepository;

    private final ArticleService articleService;
    private final ArticleQueueService articleQueueService;
    private final ArticleTagService articleTagService;
    private final CategoryService categoryService;
    private final HeadlineService headlineService;

    private final ArticleMapper articleMapper;
    private final ArticleQueueMapper articleQueueMapper;
    private final ArticleTagMapper articleTagMapper;
    private final CategoryMapper categoryMapper;
    private final HeadlineMapper headlineMapper;

    private final NewsMgmtMapper newsMgmtMapper;
    private final NsObjectStorageService storageService;


    public NewsMgmtServiceImpl(
        NewsRepository newsRepository,
        NewsSearchRepository newsSearchRepository,

        ArticleService articleService,
        ArticleQueueService articleQueueService,
        CategoryService categoryService,
        ArticleTagService articleTagService,
        HeadlineService headlineService,

        ArticleMapper articleMapper,
        ArticleQueueMapper articleQueueMapper,
        ArticleTagMapper articleTagMapper,
        CategoryMapper categoryMapper,
        HeadlineMapper headlineMapper,
        NsObjectStorageService storageService) {
        this.newsRepository = newsRepository;
        this.newsSearchRepository = newsSearchRepository;
        this.articleService = articleService;
        this.articleQueueService = articleQueueService;
        this.categoryService = categoryService;
        this.articleTagService = articleTagService;
        this.headlineService = headlineService;
        this.articleMapper = articleMapper;
        this.articleQueueMapper = articleQueueMapper;
        this.articleTagMapper = articleTagMapper;
        this.categoryMapper = categoryMapper;
        this.headlineMapper = headlineMapper;
        this.storageService = storageService;

        this.newsMgmtMapper = new NewsMgmtMapper(
            articleMapper, articleQueueMapper, articleTagMapper, categoryMapper, headlineMapper);
    }

    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public NewsPO save(NewsPO newsPO, MultipartFile...files) {
        Article article = articleMapper.toEntity(newsPO.article);
        ArticleDTO articleDTO;

        Collection<ArticleQueueDTO> articleQueues = newsPO.publishingQueues;
        Collection<ArticleTagDTO> articleTags = newsPO.articleTags;
        Collection<HeadlineDTO> headlines = newsPO.headlineSettings;

        article.setSlug(parsingSlug(article.getSlug()));

        if (article.getStatus() == ArticleStatus.DRAFT || article.getStatus() == ArticleStatus.PUBLISHED) {
            String currentUser = SecurityUtils.getCurrentUserLogin().get();

            if (article.getCreatedDate() == null) {
                article.setCreatedDate(Instant.now());
            }

            if (article.getPublishDate() == null) {
                article.setPublishUser(currentUser);
                article.setPublishDate(Instant.now());
            }

            if (article.getStatus() == ArticleStatus.PUBLISHED) {
                if (article.isPosted() == null) {
                    article.setPosted(true);
                }
                else if (!article.isPosted()) {
                    article.setPosted(true);
                }
            }
        }

        if (article.getStatus() == ArticleStatus.POSTED) {
            article.setPosted(true);
        }

        if (files != null && files.length > 0) {
            String idFileName;
            if (files[0] != null) {
                idFileName = storageService.store(NsObjectStorageService.ClientObjectType.IMAGE, files[0],
                    article.getImageFileName());

                article.setImageURL(idFileName);
            }
        }

        if (articleTags != null) {
            if (articleTags.size() > 0) {
                Article article1 = article;
                articleTags.forEach(articleTagDTO -> {
                    articleTagDTO = articleTagService.save(articleTagDTO);
                    ArticleTag articleTag = articleTagMapper.toEntity(articleTagDTO);
                    article1.addTags(articleTag);

                });
                article = article1;
            }
        }

        articleDTO = articleMapper.toDto(article);
        articleDTO = articleService.save(articleDTO);
        article = articleMapper.toEntity(articleDTO);

        if (articleQueues != null) {
            if (articleQueues.size() > 0) {
                Article article1 = article;
                articleQueues.forEach(articleQueueDTO -> {
                    articleQueueDTO.setArticleId(article1.getId());
                    articleQueueDTO = articleQueueService.save(articleQueueDTO);
                    ArticleQueue articleQueue = articleQueueMapper.toEntity(articleQueueDTO);
                    articleQueue.setArticle(article1);

                    article1.addPublishingQueues(articleQueue);
                });

                article = article1;
            }
        }

        if (headlines != null) {
            if (headlines.size() > 0 && article.getHeadline() != null) {
                Article article1 = article;
                headlines.forEach(headlineDTO -> {
                    headlineDTO = headlineService.save(headlineDTO);
                    Headline headline = headlineMapper.toEntity(headlineDTO);
                    article1.addHeadlineSettings(headline);
                });

                article = article1;
            }
        }

        return newsMgmtMapper.toDto(article);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsPO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return newsRepository.findAll(pageable)
            .map(newsMgmtMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<NewsPO> findOne(Long id) {
        return newsRepository.findById(id)
            .map(newsMgmtMapper::toDto);
    }

    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        newsRepository.deleteById(id);
        newsSearchRepository.deleteById(id);
    }

    @Override
    public boolean reload(boolean deleteAll) {
        if (deleteAll)
            articleService.deleteAllSearch();

        categoryService.reload(deleteAll);
        headlineService.reload(deleteAll);
        articleTagService.reload(deleteAll);
        articleQueueService.reload(deleteAll);

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<NewsPO> newsPage = getAllPublishArticle(page);

            while (newsPage.hasContent()) {
                try {
                    newsPage.forEach(newsPO -> {
                        Article article = newsMgmtMapper.toEntity(newsPO);
                        articleService.save(articleMapper.toDto(article));
                    });

                    if (newsPage.hasNext()) {
                        newsPage = findAll(newsPage.nextPageable());
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Page<NewsPO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        return newsSearchRepository.search(queryStringQuery(query), pageable)
            .map(newsMgmtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsPO> getAll(String title, Pageable pageable) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "createdDate"));

        return newsRepository.getAllNewsMgmt(title, pageable).map(newsMgmtMapper::toDto);
    }

    @Override
    public Page<NewsPO> getAllPublishArticle(Pageable pageable) {
        return newsRepository.getAllNews(pageable).map(newsMgmtMapper::toDto);
    }

    @Override
    public Page<NewsPO> getAllHeadlines(String title, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            new Sort(Sort.Direction.DESC, "id"));

        return newsRepository.getAllHeadlinesMgmt(title, pageable).map(newsMgmtMapper::toDto);
    }

    @Override
    public Page<NewsPO> getAllArticleQueue(String title, Pageable pageable) {
        return newsRepository.getAllArticleQueueMgmt(title, pageable).map(newsMgmtMapper::toDto);
    }

    @Override
    public byte[] getImage(String id) throws IOException {
        return storageService.toByteArray(NsObjectStorageService.ClientObjectType.IMAGE, id);
    }

    private String parsingSlug(String slug) {
        return slug.toLowerCase().replaceAll("[^a-zA-Z0-9-]", "-");
    }
}
