package com.investasikita.news.service;

import com.investasikita.news.service.dto.ArticleTagDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ArticleTag.
 */
public interface ArticleTagService {

    /**
     * Save a articleTag.
     *
     * @param articleTagDTO the entity to save
     * @return the persisted entity
     */
    ArticleTagDTO save(ArticleTagDTO articleTagDTO);

    /**
     * Get all the articleTags.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ArticleTagDTO> findAll(Pageable pageable);


    /**
     * Get the "id" articleTag.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ArticleTagDTO> findOne(Long id);

    /**
     * Delete the "id" articleTag.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    void deleteAllSearch();

    boolean reload(boolean deleteAll);

    /**
     * Search for the articleTag corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ArticleTagDTO> search(String query, Pageable pageable);

    Page<ArticleTagDTO> getAll(String tag, Pageable pageable);

    public Boolean tagExists(String trim);

}
