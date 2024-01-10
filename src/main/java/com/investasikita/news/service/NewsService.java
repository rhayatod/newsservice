package com.investasikita.news.service;

import com.investasikita.news.service.dto.ArticleDTO;
import com.investasikita.news.service.dto.HeadlineDTO;
import com.investasikita.news.web.rest.po.NewsPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

public interface NewsService {

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
     * Search for the article corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NewsPO> search(String query, Pageable pageable);

    Page<NewsPO> getAll(Pageable pageable);

    Page<NewsPO> getAll(Long categoryId, Pageable pageable);

    Page<NewsPO> getAllHeadlines(Pageable pageable);

    byte[] getImage(@PathVariable String id) throws IOException;
}
