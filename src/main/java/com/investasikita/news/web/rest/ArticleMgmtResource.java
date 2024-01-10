package com.investasikita.news.web.rest;
import com.investasikita.news.service.ArticleService;
import com.investasikita.news.web.rest.errors.BadRequestAlertException;
import com.investasikita.news.web.rest.util.HeaderUtil;
import com.investasikita.news.web.rest.util.PaginationUtil;
import com.investasikita.news.service.dto.ArticleDTO;
import com.investasikita.news.service.dto.ArticleCriteria;
import com.investasikita.news.service.ArticleQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Article.
 */
@RestController
@RequestMapping("/api/management")
public class ArticleMgmtResource {

    private final Logger log = LoggerFactory.getLogger(ArticleMgmtResource.class);

    private static final String ENTITY_NAME = "newsServiceArticleMgmt";

    private final ArticleService articleService;

    private final ArticleQueryService articleQueryService;

    public ArticleMgmtResource(ArticleService articleService, ArticleQueryService articleQueryService) {
        this.articleService = articleService;
        this.articleQueryService = articleQueryService;
    }

    /**
     * POST  /articles : Create a new article.
     *
     * @param articleDTO the articleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new articleDTO, or with status 400 (Bad Request) if the article has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/articles")
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) throws URISyntaxException {
        log.debug("REST request to create Article : {}", articleDTO);
        if (articleDTO.getId() != null) {
            throw new BadRequestAlertException("A new article cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArticleDTO result = articleService.save(articleDTO);
        return ResponseEntity.created(new URI("/api/management/articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /articles : Updates an existing article.
     *
     * @param articleDTO the articleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated articleDTO,
     * or with status 400 (Bad Request) if the articleDTO is not valid,
     * or with status 500 (Internal Server Error) if the articleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/articles")
    public ResponseEntity<ArticleDTO> updateArticle(@RequestBody ArticleDTO articleDTO) throws URISyntaxException {
        log.debug("REST request to update Article : {}", articleDTO);
        if (articleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArticleDTO result = articleService.save(articleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, articleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /articles : get all the articles.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of articles in body
     */
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getAllArticles(ArticleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Articles by criteria: {}", criteria);
        Page<ArticleDTO> page = articleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/articles");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/article-select")
    public ResponseEntity<List<ArticleDTO>> getAllArticleForSelect(@RequestParam String title, Pageable pageable) {
        log.debug("REST request to get Articles for Headline");
        Page<ArticleDTO> page = articleService.findAllArticleForHeadlines(title, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/article-select");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /articles/count : count all the articles.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/articles/count")
    public ResponseEntity<Long> countArticles(ArticleCriteria criteria) {
        log.debug("REST request to count Articles by criteria: {}", criteria);
        return ResponseEntity.ok().body(articleQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /articles/:id : get the "id" article.
     *
     * @param id the id of the articleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the articleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Optional<ArticleDTO> articleDTO = articleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleDTO);
    }

    /**
     * DELETE  /articles/:id : delete the "id" article.
     *
     * @param id the id of the articleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        articleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/articles?query=:query : search for the article corresponding
     * to the query.
     *
     * @param query the query of the article search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/articles")
    public ResponseEntity<List<ArticleDTO>> searchArticles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Articles for query {}", query);
        Page<ArticleDTO> page = articleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/management/_search/articles");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
