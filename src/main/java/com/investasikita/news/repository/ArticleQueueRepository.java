package com.investasikita.news.repository;

import com.investasikita.news.domain.ArticleQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data  repository for the ArticleQueue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleQueueRepository extends JpaRepository<ArticleQueue, Long>, JpaSpecificationExecutor<ArticleQueue> {

    @Query(value = "SELECT DISTINCT que FROM ArticleQueue as que "
        + ""
        + "WHERE LOWER(que.status) LIKE CONCAT('%', CONCAT(LOWER(:status),'%'))",
        countQuery = "SELECT COUNT (DISTINCT que) FROM ArticleQueue as que "
            + "WHERE LOWER(que.status) LIKE CONCAT('%', CONCAT(LOWER(:status),'%'))"
    )
    public Page<ArticleQueue> getAll(@Param("status") String status, Pageable pageable);


//    @Query(value = "SELECT DISTINCT que FROM ArticleQueue as que "
//        + "WHERE status IN ('WAIT', 'WAIT_RERUN') AND que.publishDate BETWEEN ((now() - interval '1 hours') at time zone 'utc') "
//        + "AND (now() at time zone 'utc')",
//        countQuery = "SELECT COUNT (DISTINCT que) FROM ArticleQueue as que "
//            + "WHERE status IN ('WAIT', 'WAIT_RERUN') AND que.publishDate BETWEEN ((now() - interval '1 hours') at time zone 'utc') "
//            + "AND (now() at time zone 'utc')"
//    )

    @Query(value = "SELECT DISTINCT que FROM ArticleQueue as que "
        + "WHERE status IN ('WAIT', 'WAIT_RERUN') "
        + "AND que.publishDate <= :publishDate",
        countQuery = "SELECT COUNT (DISTINCT que) FROM ArticleQueue as que "
            + "WHERE status IN ('WAIT', 'WAIT_RERUN') "
            + "AND que.publishDate <= :publishDate"
    )
    public Page<ArticleQueue> checkPublishDate(@Param("publishDate") Instant publishDate, Pageable page);

    @Query(value = "SELECT DISTINCT que FROM ArticleQueue as que "
        + "WHERE status IN ('WAIT', 'WAIT_RERUN') AND que.publishDate >= :startDate "
        + "AND que.publishDate <= :toDate",
        countQuery = "SELECT COUNT (DISTINCT que) FROM ArticleQueue as que "
            + "WHERE status IN ('WAIT', 'WAIT_RERUN') AND que.publishDate >= :startDate "
            + "AND que.publishDate <= :toDate"
    )
    public List<ArticleQueue> checkPublishDate(@Param("startDate") Instant startDate, @Param("toDate") Instant toDate);

}
