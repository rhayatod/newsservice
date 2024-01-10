package com.investasikita.news.service;

import com.investasikita.news.service.dto.CategoryDTO;
import com.investasikita.news.web.rest.po.NewsPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.Optional;

public interface NewsMgmtService {

    /**
     * Save a article.
     *
     * @param newsPO the entity to save
     * @return the persisted entity
     */
    NewsPO save(NewsPO newsPO, MultipartFile...files);

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NewsPO> findAll(Pageable pageable);

    /**
     * Get the "id" article.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<NewsPO> findOne(Long id);

    /**
     * Delete the "id" article.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    boolean reload(boolean deleteAll);

    /**
     * Search for the article corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NewsPO> search(String query, Pageable pageable);

    Page<NewsPO> getAll(String title, Pageable pageable);

    Page<NewsPO> getAllPublishArticle(Pageable pageable);

    Page<NewsPO> getAllHeadlines(String title, Pageable pageable);

    Page<NewsPO> getAllArticleQueue(String title, Pageable pageable);

    byte[] getImage(@PathVariable String id) throws IOException;

}
