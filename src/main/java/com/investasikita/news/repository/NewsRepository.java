package com.investasikita.news.repository;

import com.investasikita.news.domain.Article;
import com.investasikita.news.domain.Headline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;


/**
 * Spring Data  repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    @Query(value = "SELECT DISTINCT art FROM Article as art "
        + "LEFT JOIN FETCH art.tags as tag "
        + "LEFT JOIN FETCH art.category as cat "
        + "LEFT JOIN FETCH art.headlineSettings as hdl "
        + "LEFT JOIN FETCH art.publishingQueues as que ",
        countQuery = "SELECT COUNT (DISTINCT art) FROM Article as art "
            + "LEFT JOIN art.tags as tag "
            + "LEFT JOIN art.category as cat "
            + "LEFT JOIN art.headlineSettings as hdl "
            + "LEFT JOIN art.publishingQueues as que "
    )
    public Page<Article> getAllNewsMgmt(Pageable pageable);

    @Query(value = "SELECT DISTINCT art FROM Article as art "
        + "LEFT JOIN FETCH art.tags as tag "
        + "LEFT JOIN FETCH art.category as cat "
        + "LEFT JOIN FETCH art.headlineSettings as hdl "
        + "LEFT JOIN FETCH art.publishingQueues as que "
        + "WHERE LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))",
        countQuery = "SELECT COUNT (DISTINCT art) FROM Article as art "
            + "LEFT JOIN art.tags as tag "
            + "LEFT JOIN art.category as cat "
            + "LEFT JOIN art.headlineSettings as hdl "
            + "LEFT JOIN art.publishingQueues as que "
            + "WHERE LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))"
    )
    public Page<Article> getAllNewsMgmt(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT DISTINCT art FROM Article as art "
        + "LEFT JOIN art.category as cat "
        + "INNER JOIN art.headlineSettings as hdl "
        + "WHERE art.isHeadline = true "
        + "AND LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))",
        countQuery = "SELECT COUNT (DISTINCT art) FROM Article as art "
            + "LEFT JOIN art.category as cat "
            + "INNER JOIN art.headlineSettings as hdl "
            + "WHERE art.isHeadline = true "
            + "AND LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))"
    )
    public Page<Article> getAllHeadlinesMgmt(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT DISTINCT art FROM Article as art "
        + "LEFT JOIN FETCH art.category as cat "
        + "INNER JOIN FETCH art.publishingQueues as que "
        + "WHERE LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))",
        countQuery = "SELECT COUNT (DISTINCT art) FROM Article as art "
            + "LEFT JOIN art.category as cat "
            + "INNER JOIN art.publishingQueues as que "
            + "WHERE LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))"
    )
    public Page<Article> getAllArticleQueueMgmt(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT DISTINCT art FROM Article as art "
        + "LEFT JOIN FETCH art.tags as tag "
        + "LEFT JOIN FETCH art.category as cat "
        + "LEFT JOIN FETCH art.headlineSettings as hdl "
        + "LEFT JOIN FETCH art.publishingQueues as que "
        + "WHERE art.status = 'PUBLISHED' ",
        countQuery = "SELECT COUNT (DISTINCT art) FROM Article as art "
            + "LEFT JOIN art.tags as tag "
            + "LEFT JOIN art.category as cat "
            + "LEFT JOIN art.headlineSettings as hdl "
            + "LEFT JOIN art.publishingQueues as que "
            + "WHERE art.status = 'PUBLISHED' "
    )
    public Page<Article> getAllNews(Pageable page);

}
