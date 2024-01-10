package com.investasikita.news.repository;

import com.investasikita.news.domain.Headline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

/**
 * Spring Data  repository for the Headline entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HeadlineRepository extends JpaRepository<Headline, Long>, JpaSpecificationExecutor<Headline> {

    @Query(value = "SELECT DISTINCT hdl FROM Headline as hdl "
        + "WHERE hdl.startDate <= :currentDate AND hdl.toDate > :currentDate ",
        countQuery = "SELECT COUNT (DISTINCT hdl) FROM Headline as hdl "
            + "WHERE startDate <= :currentDate AND hdl.toDate > :currentDate "
    )
    public Page<Headline> checkHeadlineScheduleStartDate(@Param("currentDate") Instant currentDate, Pageable pageable);

    @Query(value = "SELECT DISTINCT hdl FROM Headline as hdl "
        + "WHERE hdl.toDate < :currentDate ",
        countQuery = "SELECT COUNT (DISTINCT hd1) FROM Headline as hdl "
            + "WHERE hdl.toDate < :currentDate "
    )
    public Page<Headline> checkHeadlineScheduleEndDate(@Param("currentDate") Instant currentDate, Pageable pageable);

}
