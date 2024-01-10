package com.investasikita.news.service.impl;

import com.investasikita.news.service.ArticleQueueService;
import com.investasikita.news.domain.ArticleQueue;
import com.investasikita.news.repository.ArticleQueueRepository;
import com.investasikita.news.repository.search.ArticleQueueSearchRepository;
import com.investasikita.news.service.dto.ArticleQueueDTO;
import com.investasikita.news.service.mapper.ArticleQueueMapper;
import com.vladmihalcea.concurrent.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ArticleQueue.
 */
@Service
@Transactional
public class ArticleQueueServiceImpl implements ArticleQueueService {

    private final Logger log = LoggerFactory.getLogger(ArticleQueueServiceImpl.class);

    private final ArticleQueueRepository articleQueueRepository;

    private final ArticleQueueMapper articleQueueMapper;

    private final ArticleQueueSearchRepository articleQueueSearchRepository;

    public ArticleQueueServiceImpl(ArticleQueueRepository articleQueueRepository, ArticleQueueMapper articleQueueMapper, ArticleQueueSearchRepository articleQueueSearchRepository) {
        this.articleQueueRepository = articleQueueRepository;
        this.articleQueueMapper = articleQueueMapper;
        this.articleQueueSearchRepository = articleQueueSearchRepository;
    }

    /**
     * Save a articleQueue.
     *
     * @param articleQueueDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public ArticleQueueDTO save(ArticleQueueDTO articleQueueDTO) {
        log.debug("Request to save ArticleQueue : {}", articleQueueDTO);
        ArticleQueue articleQueue = articleQueueMapper.toEntity(articleQueueDTO);
        articleQueue = articleQueueRepository.save(articleQueue);
        ArticleQueueDTO result = articleQueueMapper.toDto(articleQueue);
        articleQueueSearchRepository.save(articleQueue);
        return result;
    }

    /**
     * Get all the articleQueues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArticleQueueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArticleQueues");
        return articleQueueRepository.findAll(pageable)
            .map(articleQueueMapper::toDto);
    }


    /**
     * Get one articleQueue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleQueueDTO> findOne(Long id) {
        log.debug("Request to get ArticleQueue : {}", id);
        return articleQueueRepository.findById(id)
            .map(articleQueueMapper::toDto);
    }

    /**
     * Delete the articleQueue by id.
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public void delete(Long id) {
        log.debug("Request to delete ArticleQueue : {}", id);
        articleQueueRepository.deleteById(id);
        articleQueueSearchRepository.deleteById(id);
    }

    @Override
    public void deleteAllSearch() {
        articleQueueSearchRepository.deleteAll();
    }

    @Override
    public boolean reload(boolean deleteAll) {
        if (deleteAll)
            deleteAllSearch();

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<ArticleQueueDTO> articleQueuePage = findAll(page);

            while (articleQueuePage.hasContent()) {
                try {
                    articleQueuePage.forEach(articleQueueDTO -> {
                        ArticleQueue articleQueue = articleQueueMapper.toEntity(articleQueueDTO);
                        articleQueueSearchRepository.save(articleQueue);
                    });

                    if (articleQueuePage.hasNext()) {
                        articleQueuePage = findAll(articleQueuePage.nextPageable());
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
     * Search for the articleQueue corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArticleQueueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ArticleQueues for query {}", query);
        return articleQueueSearchRepository.search(queryStringQuery(query), pageable)
            .map(articleQueueMapper::toDto);
    }

    @Override
    public Page<ArticleQueueDTO> getAll(String status, Pageable pageable) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));

        return articleQueueRepository.getAll(status, pageable).map(articleQueueMapper::toDto);
    }

    @Override
    public Page<ArticleQueueDTO> checkPublishDate(Instant currentDate, Pageable pageable) {
        return articleQueueRepository.checkPublishDate(currentDate, pageable).map(articleQueueMapper::toDto);
    }

}
