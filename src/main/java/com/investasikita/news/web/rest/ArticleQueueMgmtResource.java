package com.investasikita.news.web.rest;
import com.investasikita.news.security.SecurityUtils;
import com.investasikita.news.service.ArticleQueueService;
import com.investasikita.news.service.dto.CategoryDTO;
import com.investasikita.news.web.rest.errors.BadRequestAlertException;
import com.investasikita.news.web.rest.util.HeaderUtil;
import com.investasikita.news.web.rest.util.PaginationUtil;
import com.investasikita.news.service.dto.ArticleQueueDTO;
import com.investasikita.news.service.dto.ArticleQueueCriteria;
import com.investasikita.news.service.ArticleQueueQueryService;
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
 * REST controller for managing ArticleQueue.
 */
@RestController
@RequestMapping("/api/management")
public class ArticleQueueMgmtResource {

    private final Logger log = LoggerFactory.getLogger(ArticleQueueMgmtResource.class);

    private static final String ENTITY_NAME = "newsServiceArticleQueueMgmt";

    private final ArticleQueueService articleQueueService;

    private final ArticleQueueQueryService articleQueueQueryService;

    public ArticleQueueMgmtResource(ArticleQueueService articleQueueService, ArticleQueueQueryService articleQueueQueryService) {
        this.articleQueueService = articleQueueService;
        this.articleQueueQueryService = articleQueueQueryService;
    }

    /**
     * POST  /article-queues/create : Create a new articleQueue.
     *
     * @param articleQueueDTO the articleQueueDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new articleQueueDTO, or with status 400 (Bad Request) if the articleQueue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/article-queues/create")
    public ResponseEntity<ArticleQueueDTO> createArticleQueue(@RequestBody ArticleQueueDTO articleQueueDTO) throws URISyntaxException {
        log.debug("REST request to create ArticleQueue : {}", articleQueueDTO);

        String user;
        Optional<String> users = SecurityUtils.getCurrentUserLogin();
        if (users != null) {
            user = users.get();
        } else {
            user = "anonymous";
        }
        articleQueueDTO.setPublishUser(user);

        ArticleQueueDTO result = articleQueueService.save(articleQueueDTO);
        return ResponseEntity.created(new URI("/api/management/article-queues/create/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /article-queues/update : Update a new articleQueue.
     *
     * @param articleQueueDTO the articleQueueDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new articleQueueDTO, or with status 400 (Bad Request) if the articleQueue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/article-queues/update")
    public ResponseEntity<ArticleQueueDTO> updateArticleQueue(@RequestBody ArticleQueueDTO articleQueueDTO) throws URISyntaxException {
        log.debug("REST request to save ArticleQueue : {}", articleQueueDTO);
        if (articleQueueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArticleQueueDTO result = articleQueueService.save(articleQueueDTO);
        return ResponseEntity.created(new URI("/api/management/article-queues/update/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /article-queues : Updates an existing articleQueue.
     *
     * @param articleQueueDTO the articleQueueDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated articleQueueDTO,
     * or with status 400 (Bad Request) if the articleQueueDTO is not valid,
     * or with status 500 (Internal Server Error) if the articleQueueDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/article-queues")
    public ResponseEntity<ArticleQueueDTO> putArticleQueue(@RequestBody ArticleQueueDTO articleQueueDTO) throws URISyntaxException {
        log.debug("REST request to update ArticleQueue : {}", articleQueueDTO);
        if (articleQueueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArticleQueueDTO result = articleQueueService.save(articleQueueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, articleQueueDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /article-queues : get all the articleQueues.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of articleQueues in body
     */
    @GetMapping("/article-queues")
    public ResponseEntity<List<ArticleQueueDTO>> getAllArticleQueues(ArticleQueueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ArticleQueues by criteria: {}", criteria);
        Page<ArticleQueueDTO> page = articleQueueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/article-queues");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /categories : get all the categories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categories in body
     */
//    @GetMapping("/article-queues/search")
//    public ResponseEntity<List<ArticleQueueDTO>> getAllCategorybyParam(@RequestParam String status, Pageable pageable) {
//        log.debug("REST request to get Queue by param");
//        Page<ArticleQueueDTO> page = articleQueueService.getAll(status, pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/article-queues/search");
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }

    /**
    * GET  /article-queues/count : count all the articleQueues.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/article-queues/count")
    public ResponseEntity<Long> countArticleQueues(ArticleQueueCriteria criteria) {
        log.debug("REST request to count ArticleQueues by criteria: {}", criteria);
        return ResponseEntity.ok().body(articleQueueQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /article-queues/:id : get the "id" articleQueue.
     *
     * @param id the id of the articleQueueDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the articleQueueDTO, or with status 404 (Not Found)
     */
    @GetMapping("/article-queues/{id}")
    public ResponseEntity<ArticleQueueDTO> getArticleQueue(@PathVariable Long id) {
        log.debug("REST request to get ArticleQueue : {}", id);
        Optional<ArticleQueueDTO> articleQueueDTO = articleQueueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleQueueDTO);
    }

    /**
     * DELETE  /article-queues/:id : delete the "id" articleQueue.
     *
     * @param id the id of the articleQueueDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/article-queues/{id}")
    public ResponseEntity<Void> deleteArticleQueue(@PathVariable Long id) {
        log.debug("REST request to delete ArticleQueue : {}", id);
        articleQueueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/article-queues?query=:query : search for the articleQueue corresponding
     * to the query.
     *
     * @param query the query of the articleQueue search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/article-queues")
    public ResponseEntity<List<ArticleQueueDTO>> searchArticleQueues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ArticleQueues for query {}", query);
        Page<ArticleQueueDTO> page = articleQueueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/management/_search/article-queues");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
