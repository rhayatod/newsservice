package com.investasikita.news.web.rest;
import com.codahale.metrics.annotation.Timed;
import com.investasikita.news.service.ArticleTagService;
import com.investasikita.news.service.dto.CategoryDTO;
import com.investasikita.news.web.rest.errors.BadRequestAlertException;
import com.investasikita.news.web.rest.util.HeaderUtil;
import com.investasikita.news.web.rest.util.PaginationUtil;
import com.investasikita.news.service.dto.ArticleTagDTO;
import com.investasikita.news.service.dto.ArticleTagCriteria;
import com.investasikita.news.service.ArticleTagQueryService;
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
 * REST controller for managing ArticleTag.
 */
@RestController
@RequestMapping("/api/management")
public class ArticleTagMgmtResource {

    private final Logger log = LoggerFactory.getLogger(ArticleTagMgmtResource.class);

    private static final String ENTITY_NAME = "newsServiceArticleTagMgmt";

    private final ArticleTagService articleTagService;

    private final ArticleTagQueryService articleTagQueryService;

    public ArticleTagMgmtResource(ArticleTagService articleTagService, ArticleTagQueryService articleTagQueryService) {
        this.articleTagService = articleTagService;
        this.articleTagQueryService = articleTagQueryService;
    }

    /**
     * POST  /article-tags : Create a new articleTag.
     *
     * @param articleTagDTO the articleTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new articleTagDTO, or with status 400 (Bad Request) if the articleTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/article-tags")
    public ResponseEntity<ArticleTagDTO> createArticleTag(@RequestBody ArticleTagDTO articleTagDTO) throws URISyntaxException {
        log.debug("REST request to create ArticleTag : {}", articleTagDTO);
        if (articleTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new articleTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArticleTagDTO result = articleTagService.save(articleTagDTO);
        return ResponseEntity.created(new URI("/api/article-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /article-tags : Updates an existing articleTag.
     *
     * @param articleTagDTO the articleTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated articleTagDTO,
     * or with status 400 (Bad Request) if the articleTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the articleTagDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/article-tags")
    public ResponseEntity<ArticleTagDTO> updateArticleTag(@RequestBody ArticleTagDTO articleTagDTO) throws URISyntaxException {
        log.debug("REST request to update ArticleTag : {}", articleTagDTO);
        if (articleTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArticleTagDTO result = articleTagService.save(articleTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, articleTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /article-tags : get all the articleTags.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of articleTags in body
     */
    @GetMapping("/article-tags")
    public ResponseEntity<List<ArticleTagDTO>> getAllArticleTags(ArticleTagCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ArticleTags by criteria: {}", criteria);
        Page<ArticleTagDTO> page = articleTagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/article-tags");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /categories : get all the categories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categories in body
     */
    @GetMapping("/article-tags-search")
    public ResponseEntity<List<ArticleTagDTO>> getAllArticleTagsbyParam(@RequestParam String tag, Pageable pageable) {
        log.debug("REST request to get Article Tag by param");
        Page<ArticleTagDTO> page = articleTagService.getAll(tag, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/article-tags-search");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /article-tags/count : count all the articleTags.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/article-tags/count")
    public ResponseEntity<Long> countArticleTags(ArticleTagCriteria criteria) {
        log.debug("REST request to count ArticleTags by criteria: {}", criteria);
        return ResponseEntity.ok().body(articleTagQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /article-tags/:id : get the "id" articleTag.
     *
     * @param id the id of the articleTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the articleTagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/article-tags/{id}")
    public ResponseEntity<ArticleTagDTO> getArticleTag(@PathVariable Long id) {
        log.debug("REST request to get ArticleTag : {}", id);
        Optional<ArticleTagDTO> articleTagDTO = articleTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleTagDTO);
    }

    /**
     * DELETE  /article-tags/:id : delete the "id" articleTag.
     *
     * @param id the id of the articleTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/article-tags/{id}")
    public ResponseEntity<Void> deleteArticleTag(@PathVariable Long id) {
        log.debug("REST request to delete ArticleTag : {}", id);
        articleTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/article-tags?query=:query : search for the articleTag corresponding
     * to the query.
     *
     * @param query the query of the articleTag search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/article-tags")
    public ResponseEntity<List<ArticleTagDTO>> searchArticleTags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ArticleTags for query {}", query);
        Page<ArticleTagDTO> page = articleTagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/management/_search/article-tags");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/article-tags/validate-tag")
    @Timed
    public ResponseEntity<Boolean> validateTag(@RequestParam("tag") String tag) {
        log.debug("REST request to check if tag exists: ", tag);
        Boolean result = false;
        try {
            if (tag != null && tag.trim() != "") {
                result = articleTagService.tagExists(tag.trim());
            }
        } catch (Exception e) {
            result = false;
        }
        return ResponseEntity.ok().body(!result);
    }


}
