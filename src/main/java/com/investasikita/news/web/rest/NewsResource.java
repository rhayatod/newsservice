package com.investasikita.news.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.investasikita.news.service.NewsService;
import com.investasikita.news.service.NsObjectStorageService;
import com.investasikita.news.web.rest.po.NewsPO;
import com.investasikita.news.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Article Management.
 */
@RestController
@RequestMapping("/api/v1")
public class NewsResource {

    private final Logger log = LoggerFactory.getLogger(NewsResource.class);

    private static final String ENTITY_NAME = "newsService";

    private final NewsService newsService;

    public NewsResource(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    @Timed
    public ResponseEntity<List<NewsPO>> getAllNews(@RequestParam(name="filter", required=false) Long categoryId, Pageable pageable) {
        log.debug("REST request to get article by category : {}", categoryId);
        Page<NewsPO> page = newsService.getAll(categoryId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,  "/api/v1/news/" + categoryId);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/headlines")
    @Timed
    public ResponseEntity<List<NewsPO>> getHeadlines(Pageable pageable) {
        Page<NewsPO> page = newsService.getAllHeadlines(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/v1/headlines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/news/{id}")
    @Timed
    public ResponseEntity<NewsPO> getNewsById(@PathVariable Long id) {
        log.debug("REST request to get News : {}", id);
        Optional<NewsPO> newsPO = newsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newsPO);
    }

    /**
     * GET  /img/article/:id : get product's image.
     *
     * @param id the id of the products to retrieve
     * @return the ResponseBody with status 200 (OK), or with status 404 (Not Found)
     */
    @GetMapping(value = "/news/img/article/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @Timed
    public @ResponseBody byte[] getImage(@PathVariable String id) throws IOException {
        log.debug("REST request to get Image : {}", id);
        return newsService.getImage(id);
    }

    /**
     * SEARCH  /_search/article-queues?query=:query : search for the articleQueue corresponding
     * to the query.
     *
     * @param query the query of the articleQueue search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/news")
    public ResponseEntity<List<NewsPO>> searchArticleQueues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ArticleQueues for query {}", query);

//        String queryStr = String.format(
//            "status: published AND (title:*%s* OR shortDescription: *%s* OR content: *%s* OR tags.tag:*%s*)",
//            query, query, query, query);

        Page<NewsPO> page = newsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/v1/_search/news");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


}
