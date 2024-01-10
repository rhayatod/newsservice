package com.investasikita.news.service.impl;

import com.investasikita.news.service.HeadlineService;
import com.investasikita.news.domain.Headline;
import com.investasikita.news.repository.HeadlineRepository;
import com.investasikita.news.repository.search.HeadlineSearchRepository;
import com.investasikita.news.service.dto.HeadlineDTO;
import com.investasikita.news.service.mapper.HeadlineMapper;
import com.vladmihalcea.concurrent.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Headline.
 */
@Service
@Transactional
public class HeadlineServiceImpl implements HeadlineService {

    private final Logger log = LoggerFactory.getLogger(HeadlineServiceImpl.class);

    private final HeadlineRepository headlineRepository;

    private final HeadlineMapper headlineMapper;

    private final HeadlineSearchRepository headlineSearchRepository;

    public HeadlineServiceImpl(HeadlineRepository headlineRepository, HeadlineMapper headlineMapper, HeadlineSearchRepository headlineSearchRepository) {
        this.headlineRepository = headlineRepository;
        this.headlineMapper = headlineMapper;
        this.headlineSearchRepository = headlineSearchRepository;
    }

    /**
     * Save a headline.
     *
     * @param headlineDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public HeadlineDTO save(HeadlineDTO headlineDTO) {
        log.debug("Request to save Headline : {}", headlineDTO);
        Headline headline = headlineMapper.toEntity(headlineDTO);
        headline = headlineRepository.save(headline);
        HeadlineDTO result = headlineMapper.toDto(headline);
        headlineSearchRepository.save(headline);
        return result;
    }

    /**
     * Get all the headlines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HeadlineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Headlines");
        return headlineRepository.findAll(pageable)
            .map(headlineMapper::toDto);
    }


    /**
     * Get one headline by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HeadlineDTO> findOne(Long id) {
        log.debug("Request to get Headline : {}", id);
        return headlineRepository.findById(id)
            .map(headlineMapper::toDto);
    }

    /**
     * Delete the headline by id.
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public void delete(Long id) {
        log.debug("Request to delete Headline : {}", id);
        headlineRepository.deleteById(id);
        headlineSearchRepository.deleteById(id);
    }

    @Override
    public void deleteAllSearch() {
        headlineSearchRepository.deleteAll();
    }

    @Override
    public boolean reload(boolean deleteAll) {
        if (deleteAll)
            deleteAllSearch();

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<HeadlineDTO> headlinePage = findAll(page);

            while (headlinePage.hasContent()) {
                try {
                    headlinePage.forEach(headlineDTO -> {
                        Headline headline = headlineMapper.toEntity(headlineDTO);
                        headlineSearchRepository.save(headline);
                    });

                    if (headlinePage.hasNext()) {
                        headlinePage = findAll(headlinePage.nextPageable());
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Search for the headline corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HeadlineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Headlines for query {}", query);
        return headlineSearchRepository.search(queryStringQuery(query), pageable)
            .map(headlineMapper::toDto);
    }

    @Override
    public Page<HeadlineDTO> getAll(Pageable pageable) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));

        return headlineRepository.findAll(pageable).map(headlineMapper::toDto);
    }

    @Override
    public Page<HeadlineDTO> checkHeadlineScheduleEndDate(Instant currentDate, Pageable pageable) {
        return headlineRepository.checkHeadlineScheduleEndDate(currentDate, pageable).map(headlineMapper::toDto);
    }

    @Override
    public Page<HeadlineDTO> checkHeadlineScheduleStartDate(Instant currentDate, Pageable pageable) {
        return headlineRepository.checkHeadlineScheduleStartDate(currentDate, pageable).map(headlineMapper::toDto);
    }
}
