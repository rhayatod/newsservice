package com.investasikita.news.service;

import com.investasikita.news.service.dto.HeadlineDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Interface for managing Headline.
 */
public interface HeadlineService {

    /**
     * Save a headline.
     *
     * @param headlineDTO the entity to save
     * @return the persisted entity
     */
    HeadlineDTO save(HeadlineDTO headlineDTO);

    /**
     * Get all the headlines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HeadlineDTO> findAll(Pageable pageable);


    /**
     * Get the "id" headline.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<HeadlineDTO> findOne(Long id);

    /**
     * Delete the "id" headline.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    void deleteAllSearch();

    boolean reload(boolean deleteAll);

    /**
     * Search for the headline corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HeadlineDTO> search(String query, Pageable pageable);

    Page<HeadlineDTO> getAll(Pageable pageable);

    Page<HeadlineDTO> checkHeadlineScheduleStartDate(Instant currentDate, Pageable pageable);

    Page<HeadlineDTO> checkHeadlineScheduleEndDate(Instant currentDate, Pageable pageable);
}
