package com.investasikita.news.service.impl;

import com.investasikita.news.service.ArticleTagService;
import com.investasikita.news.domain.ArticleTag;
import com.investasikita.news.repository.ArticleTagRepository;
import com.investasikita.news.repository.search.ArticleTagSearchRepository;
import com.investasikita.news.service.dto.ArticleTagDTO;
import com.investasikita.news.service.mapper.ArticleTagMapper;
import com.vladmihalcea.concurrent.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ArticleTag.
 */
@Service
@Transactional
public class ArticleTagServiceImpl implements ArticleTagService {

    private final Logger log = LoggerFactory.getLogger(ArticleTagServiceImpl.class);

    private final ArticleTagRepository articleTagRepository;

    private final ArticleTagMapper articleTagMapper;

    private final ArticleTagSearchRepository articleTagSearchRepository;

    public ArticleTagServiceImpl(ArticleTagRepository articleTagRepository, ArticleTagMapper articleTagMapper, ArticleTagSearchRepository articleTagSearchRepository) {
        this.articleTagRepository = articleTagRepository;
        this.articleTagMapper = articleTagMapper;
        this.articleTagSearchRepository = articleTagSearchRepository;
    }

    /**
     * Save a articleTag.
     *
     * @param articleTagDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public ArticleTagDTO save(ArticleTagDTO articleTagDTO) {
        log.debug("Request to save ArticleTag : {}", articleTagDTO);
        ArticleTag articleTag = articleTagMapper.toEntity(articleTagDTO);
        articleTag = articleTagRepository.save(articleTag);
        ArticleTagDTO result = articleTagMapper.toDto(articleTag);
        articleTagSearchRepository.save(articleTag);
        return result;
    }

    /**
     * Get all the articleTags.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArticleTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArticleTags");
        return articleTagRepository.findAll(pageable)
            .map(articleTagMapper::toDto);
    }


    /**
     * Get one articleTag by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleTagDTO> findOne(Long id) {
        log.debug("Request to get ArticleTag : {}", id);
        return articleTagRepository.findById(id)
            .map(articleTagMapper::toDto);
    }

    /**
     * Delete the articleTag by id.
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public void delete(Long id) {
        log.debug("Request to delete ArticleTag : {}", id);
        articleTagRepository.deleteById(id);
//        articleTagSearchRepository.deleteById(id);
    }

    @Override
    public void deleteAllSearch() {
        articleTagSearchRepository.deleteAll();
    }

    @Override
    public boolean reload(boolean deleteAll) {
        if (deleteAll)
            deleteAllSearch();

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<ArticleTagDTO> articleTagPage = findAll(page);

            while (articleTagPage.hasContent()) {
                try {
                    articleTagPage.forEach(articleTagDTO -> {
                        ArticleTag articleTag = articleTagMapper.toEntity(articleTagDTO);
                        articleTagSearchRepository.save(articleTag);
                    });

                    if (articleTagPage.hasNext()) {
                        articleTagPage = findAll(articleTagPage.nextPageable());
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
     * Search for the articleTag corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArticleTagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ArticleTags for query {}", query);
        return articleTagSearchRepository.search(queryStringQuery(query), pageable)
            .map(articleTagMapper::toDto);
    }

    @Override
    public Page<ArticleTagDTO> getAll(String tag, Pageable pageable) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));

        return articleTagRepository.getAll(tag, pageable).map(articleTagMapper::toDto);
    }

    @Override
    public Boolean tagExists(String trim) {
        log.debug("Request to check if email already exists : {}", trim);
        Optional<ArticleTag> optArticleTag = articleTagRepository.findFirstByTag(trim);
        return optArticleTag.isPresent();
    }
}
