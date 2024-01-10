package com.investasikita.news.service.impl;

import com.investasikita.news.domain.enumeration.ArticleStatus;
import com.investasikita.news.service.ArticleService;
import com.investasikita.news.domain.Article;
import com.investasikita.news.repository.ArticleRepository;
import com.investasikita.news.repository.search.ArticleSearchRepository;
import com.investasikita.news.service.dto.ArticleDTO;
import com.investasikita.news.service.mapper.ArticleMapper;
import com.vladmihalcea.concurrent.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    private final ArticleSearchRepository articleSearchRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper, ArticleSearchRepository articleSearchRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.articleSearchRepository = articleSearchRepository;
    }

    /**
     * Save a article.
     *
     * @param articleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        ArticleDTO result = articleMapper.toDto(article);
        if (article.getStatus().equals(ArticleStatus.PUBLISHED)) {
            articleSearchRepository.save(article);
        } else {
            articleSearchRepository.delete(article);
        }

        return result;
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable)
            .map(articleMapper::toDto);
    }

    /**
     * Get all the Article with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ArticleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return articleRepository.findAllWithEagerRelationships(pageable).map(articleMapper::toDto);
    }


    /**
     * Get one article by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findOneWithEagerRelationships(id)
            .map(articleMapper::toDto);
    }

    /**
     * Delete the article by id.
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
        articleSearchRepository.deleteById(id);
    }

    @Override
    public void deleteAllSearch() {
        articleSearchRepository.deleteAll();
    }

    @Override
    public boolean reload(boolean deleteAll) {
        if (deleteAll)
            deleteAllSearch();

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<ArticleDTO> articlePage = findAll(page);

            while (articlePage.hasContent()) {
                try {
                    articlePage.forEach(articleDTO -> {
                        Article article = articleMapper.toEntity(articleDTO);
                        articleSearchRepository.save(article);
                    });

                    if (articlePage.hasNext()) {
                        articlePage = findAll(articlePage.nextPageable());
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

    /**
     * Search for the article corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArticleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        return articleSearchRepository.search(queryStringQuery(query), pageable)
            .map(articleMapper::toDto);
    }

    @Override
    public List<ArticleDTO> findAll() {
        List<Article> articleList = articleRepository.findAll();
        articleSearchRepository.saveAll(articleList);
        return articleMapper.toDto(articleList);
    }

    @Override
    public Page<ArticleDTO> findAllArticleForHeadlines(String title, Pageable pageable) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));

        return articleRepository.findAllArticleForHeadlineMapping(title, pageable).map(articleMapper::toDto);
    }
}
