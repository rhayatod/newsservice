package com.investasikita.news.repository;

import com.investasikita.news.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    @Query(value = "SELECT DISTINCT article FROM Article article LEFT JOIN FETCH article.tags",
        countQuery = "SELECT COUNT (DISTINCT article) FROM Article article LEFT JOIN article.tags")
    Page<Article> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "SELECT DISTINCT article FROM Article article LEFT JOIN FETCH article.tags")
    List<Article> findAllWithEagerRelationships();

    @Query("SELECT DISTINCT article FROM Article article LEFT JOIN FETCH article.tags where article.id =:id")
    Optional<Article> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT art FROM Article art WHERE art.status IN ('POSTED') "
        + "AND LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))",
        countQuery = "SELECT COUNT(DISTINCT art) FROM Article art WHERE art.status IN ('POSTED')"
        + "AND LOWER(art.title) LIKE CONCAT('%', CONCAT(LOWER(:title),'%'))")
    Page<Article> findAllArticleForHeadlineMapping(@Param("title") String title, Pageable pageable);


}
