package com.investasikita.news.service.impl;

import com.investasikita.news.domain.Article;
import com.investasikita.news.repository.NewsRepository;
import com.investasikita.news.repository.search.NewsSearchRepository;
import com.investasikita.news.service.NewsService;
import com.investasikita.news.service.NsObjectStorageService;
import com.investasikita.news.service.mapper.*;
import com.investasikita.news.web.rest.po.NewsPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    private final Logger log = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;

    private final NewsSearchRepository newsSearchRepository;

    private final ArticleMapper articleMapper;
    private final ArticleQueueMapper articleQueueMapper;
    private final ArticleTagMapper articleTagMapper;
    private final CategoryMapper categoryMapper;
    private final HeadlineMapper headlineMapper;

    private final NewsMgmtMapper newsMgmtMapper;

    private final NsObjectStorageService storageService;

    public NewsServiceImpl(
        NewsRepository newsRepository,
        NewsSearchRepository newsSearchRepository,
        ArticleMapper articleMapper,
        ArticleQueueMapper articleQueueMapper,
        ArticleTagMapper articleTagMapper,
        CategoryMapper categoryMapper,
        HeadlineMapper headlineMapper,
        NsObjectStorageService storageService
    ) {
        this.newsRepository = newsRepository;
        this.newsSearchRepository = newsSearchRepository;
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
    public Page<NewsPO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<NewsPO> findOne(Long id) {
        return newsSearchRepository.findById(id)
            .map(newsMgmtMapper::toDto);
    }

    @Override
    public Page<NewsPO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        pageable.getSort();
        if (!pageable.getSort().iterator().hasNext() && newsSearchRepository.count() > 0)
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));
        String queryStr = parsingText(query);
        return newsSearchRepository.search(queryStringQuery(queryStr), pageable)
            .map(newsMgmtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsPO> getAll(Pageable pageable) {
        pageable.getSort();
        if (!pageable.getSort().iterator().hasNext() && newsSearchRepository.count() > 0)
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));

        String queryStr = parsingText("");
        return newsSearchRepository.search(queryStringQuery(queryStr), pageable)
            .map(newsMgmtMapper::toDto);
    }

    @Override
    public Page<NewsPO> getAll(Long categoryId, Pageable pageable) {
        pageable.getSort();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            new Sort(Sort.Direction.DESC, "publishDate"));

//        if (!pageable.getSort().iterator().hasNext() && newsSearchRepository.count() > 0)
//            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
//                new Sort(Sort.Direction.DESC, "publishDate"));

        String queryStr = parsingText("", categoryId, false);
        return newsSearchRepository.search(queryStringQuery(queryStr), pageable)
            .map(newsMgmtMapper::toDto);
    }

    @Override
    public Page<NewsPO> getAllHeadlines(Pageable pageable) {
        pageable.getSort();
        pageable = PageRequest.of(pageable.getPageNumber(), 5,
            new Sort(Sort.Direction.DESC, "publishDate"));

//        if (!pageable.getSort().iterator().hasNext() && newsSearchRepository.count() > 0)
//            pageable = PageRequest.of(pageable.getPageNumber(), 5,
//                new Sort(Sort.Direction.DESC, "publishDate"));
        String query = parsingText("", null, true);
        return newsSearchRepository.search(queryStringQuery(query), pageable)
            .map(newsMgmtMapper::toDto);
    }

    @Override
    public byte[] getImage(String id) throws IOException {
        return storageService.toByteArray(NsObjectStorageService.ClientObjectType.IMAGE, id);
    }

    private String parsingText(String query) {
        return parsingText(query, null, false);
    }

    private String parsingText(String query, Long categoryId, boolean isHeadline) {
        String[] strList = query.split(" ");
        StringBuilder queryStr;

        if (query.contains(":")) {
            return query;
        }

        queryStr = new StringBuilder();
        queryStr.append("status: published ");
        if (isHeadline)
            queryStr.append("AND headline: true ");

        if (categoryId != null)
            queryStr.append("AND category.id: ").append(categoryId);

        for (int i = 0; i <= strList.length-1; i++ ) {
            String str = strList[i];
            if (i == 0) {
                queryStr.append(" AND (");
                queryStr.append(String.format(
                    "title:*%s* OR shortDescription: *%s* OR content: *%s* " +
                    "OR tags.tag:*%s* OR author: *%s* OR externalAuthor: *%s* OR externalSource: *%s*",
                    str, str, str, str, str, str, str));
            } else {
                queryStr.append(String.format(
                    "OR title:*%s* OR shortDescription: *%s* OR content: *%s* OR tags.tag:*%s* " +
                    "OR tags.tag:*%s* OR author: *%s* OR externalAuthor: *%s* OR externalSource: *%s*",
                    str, str, str, str, str, str, str));
            }
        }
        queryStr.append(")");

        log.debug("query string : {}", queryStr.toString());
        return queryStr.toString();
    }

}
