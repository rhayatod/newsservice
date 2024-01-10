package com.investasikita.news.service;

import com.investasikita.news.service.dto.ArticleDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

/**
 * Service Interface for managing Article.
 */
public interface ArticleService {

    /**
     * Save a article.
     *
     * @param articleDTO the entity to save
     * @return the persisted entity
     */
    ArticleDTO save(ArticleDTO articleDTO);

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ArticleDTO> findAll(Pageable pageable);

    /**
     * Get all the Article with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<ArticleDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" article.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ArticleDTO> findOne(Long id);

    /**
     * Delete the "id" article.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    void deleteAllSearch();

    boolean reload(boolean deleteAll);

    /**
     * Search for the article corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ArticleDTO> search(String query, Pageable pageable);

    List<ArticleDTO> findAll();

    Page<ArticleDTO> findAllArticleForHeadlines(String title, Pageable pageable);

}
