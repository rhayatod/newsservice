package com.investasikita.news.repository;

import com.investasikita.news.domain.ArticleTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ArticleTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long>, JpaSpecificationExecutor<ArticleTag> {

    Optional<ArticleTag> findFirstByTag(String tag);

    @Query(value = "SELECT DISTINCT atag FROM ArticleTag as atag "
        + "WHERE LOWER(atag.tag) LIKE CONCAT('%', CONCAT(LOWER(:tag),'%'))",
        countQuery = "SELECT COUNT (DISTINCT atag) FROM ArticleTag as atag "
            + "WHERE LOWER(atag.tag) LIKE CONCAT('%', CONCAT(LOWER(:tag),'%'))"
    )
    public Page<ArticleTag> getAll(@Param("tag") String tag, Pageable pageable);

}
