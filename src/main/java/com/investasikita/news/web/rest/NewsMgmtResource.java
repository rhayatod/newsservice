package com.investasikita.news.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.investasikita.news.domain.enumeration.ArticleStatus;
import com.investasikita.news.security.SecurityUtils;
import com.investasikita.news.service.NewsMgmtService;
import com.investasikita.news.web.rest.errors.BadRequestAlertException;
import com.investasikita.news.web.rest.po.NewsPO;
import com.investasikita.news.web.rest.util.HeaderUtil;
import com.investasikita.news.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Article Management.
 */
@RestController
@RequestMapping("/api/management")
public class NewsMgmtResource {

    private final Logger log = LoggerFactory.getLogger(NewsMgmtResource.class);

    private static final String ENTITY_NAME = "newsMgmtService";

    private final NewsMgmtService newsMgmtService;

    public NewsMgmtResource(NewsMgmtService newsMgmtService) {
        this.newsMgmtService = newsMgmtService;
    }

    /**
     * POST  /articles/create : Create a new article.
     *
     * @param newsData the newsPO as string
     * @return the ResponseEntity with status 201 (Created) and with body the new articleDTO, or with status 400 (Bad Request) if the article has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/news/create")
    public ResponseEntity<NewsPO> createNews(
        @RequestParam("newsPO") String newsData,
        @RequestParam(name="idPicture", required=false) MultipartFile idPicture) throws URISyntaxException {
        NewsPO newsPO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            newsPO = objectMapper.readValue(newsData.getBytes(), NewsPO.class);

            if (newsPO.article.getCreatedDate() == null) {
                newsPO.article.setCreatedDate(Instant.now());
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        NewsPO result = newsMgmtService.save(newsPO, idPicture);

        return ResponseEntity.created(new URI("/api/management/news/create/" + result.article.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.article.getId().toString()))
            .body(result);
    }

    /**
     * POST  /articles/update : Update a article.
     *
     * @param newsData the newsPO as string
     * @return the ResponseEntity with status 201 (Created) and with body the new articleDTO, or with status 400 (Bad Request) if the article has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/news/update")
    public ResponseEntity<NewsPO> updateNews(
        @RequestParam("newsPO") String newsData,
        @RequestParam(name="idPicture", required=false) MultipartFile idPicture) throws URISyntaxException {
        NewsPO newsPO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            newsPO = objectMapper.readValue(newsData.getBytes(StandardCharsets.UTF_8), NewsPO.class);

            if (newsPO.article.getId() == null) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        NewsPO result = newsMgmtService.save(newsPO, idPicture);

        return ResponseEntity.created(new URI("/api/management/news/update/" + result.article.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.article.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notifications : get all the notifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notifications in body
     */
    @GetMapping("/news-search")
    @Timed
    public ResponseEntity<List<NewsPO>> getAllNews(@RequestParam String title, Pageable pageable) {
        Page<NewsPO> page = newsMgmtService.getAll(title, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/news-search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notifications/:id : get the "id" notification.
     *
     * @param id the id of the notificationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/news/{id}")
    @Timed
    public ResponseEntity<NewsPO> getNewsById(@PathVariable Long id) {
        Optional<NewsPO> newsPO = newsMgmtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newsPO);
    }

    @GetMapping("/headlines-search")
    public ResponseEntity<List<NewsPO>> getAllHeadlines(@RequestParam String title, Pageable pageable) {
        Page<NewsPO> page = newsMgmtService.getAllHeadlines(title, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/headlines-search");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /img/products/:id : get product's image.
     *
     * @param id the id of the products to retrieve
     * @return the ResponseBody with status 200 (OK), or with status 404 (Not Found)
     */
    @GetMapping(value = "/news/img/article/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @Timed
    public @ResponseBody byte[] getImage(@PathVariable String id) throws IOException {
        log.debug("REST request to get Image : {}", id);
        return newsMgmtService.getImage(id);
    }

    /**
     * GET  /categories : get all the categories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categories in body
     */
    @GetMapping("/article-queues/search")
    public ResponseEntity<List<NewsPO>> getAllArticleQueueMgmt(@RequestParam String title, Pageable pageable) {
        log.debug("REST request to get Queue by param");
        Page<NewsPO> page = newsMgmtService.getAllArticleQueue(title, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/management/article-queues/search");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/news/reload")
    public ResponseEntity<Void> deleteAllArticle() {
        log.debug("REST request to Reload All Article");
        newsMgmtService.reload(true);
        return ResponseEntity.ok().headers(
            HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "Reload Success")).build();
    }


}


