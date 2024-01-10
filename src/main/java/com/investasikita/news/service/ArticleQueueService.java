package com.investasikita.news.service;

import com.investasikita.news.service.dto.ArticleQueueDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Interface for managing ArticleQueue.
 */
public interface ArticleQueueService {

    /**
     * Save a articleQueue.
     *
     * @param articleQueueDTO the entity to save
     * @return the persisted entity
     */
    ArticleQueueDTO save(ArticleQueueDTO articleQueueDTO);

    /**
     * Get all the articleQueues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ArticleQueueDTO> findAll(Pageable pageable);


    /**
     * Get the "id" articleQueue.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ArticleQueueDTO> findOne(Long id);

    /**
     * Delete the "id" articleQueue.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    void deleteAllSearch();

    boolean reload(boolean deleteAll);

    /**
     * Search for the articleQueue corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ArticleQueueDTO> search(String query, Pageable pageable);

    Page<ArticleQueueDTO> getAll(String status, Pageable pageable);

    Page<ArticleQueueDTO> checkPublishDate(Instant currentDate, Pageable pageable);
}
