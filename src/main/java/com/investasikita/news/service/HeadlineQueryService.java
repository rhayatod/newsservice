package com.investasikita.news.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.investasikita.news.domain.Headline;
import com.investasikita.news.domain.*; // for static metamodels
import com.investasikita.news.repository.HeadlineRepository;
import com.investasikita.news.repository.search.HeadlineSearchRepository;
import com.investasikita.news.service.dto.HeadlineCriteria;
import com.investasikita.news.service.dto.HeadlineDTO;
import com.investasikita.news.service.mapper.HeadlineMapper;

/**
 * Service for executing complex queries for Headline entities in the database.
 * The main input is a {@link HeadlineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HeadlineDTO} or a {@link Page} of {@link HeadlineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HeadlineQueryService extends QueryService<Headline> {

    private final Logger log = LoggerFactory.getLogger(HeadlineQueryService.class);

    private final HeadlineRepository headlineRepository;

    private final HeadlineMapper headlineMapper;

    private final HeadlineSearchRepository headlineSearchRepository;

    public HeadlineQueryService(HeadlineRepository headlineRepository, HeadlineMapper headlineMapper, HeadlineSearchRepository headlineSearchRepository) {
        this.headlineRepository = headlineRepository;
        this.headlineMapper = headlineMapper;
        this.headlineSearchRepository = headlineSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HeadlineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HeadlineDTO> findByCriteria(HeadlineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Headline> specification = createSpecification(criteria);
        return headlineMapper.toDto(headlineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HeadlineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HeadlineDTO> findByCriteria(HeadlineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Headline> specification = createSpecification(criteria);
        return headlineRepository.findAll(specification, page)
            .map(headlineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HeadlineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Headline> specification = createSpecification(criteria);
        return headlineRepository.count(specification);
    }

    /**
     * Function to convert HeadlineCriteria to a {@link Specification}
     */
    private Specification<Headline> createSpecification(HeadlineCriteria criteria) {
        Specification<Headline> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Headline_.id));
            }
            if (criteria.getIsGlobal() != null) {
                specification = specification.and(buildSpecification(criteria.getIsGlobal(), Headline_.isGlobal));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Headline_.startDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), Headline_.toDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), Headline_.position));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Headline_.category, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getArticleId() != null) {
                specification = specification.and(buildSpecification(criteria.getArticleId(),
                    root -> root.join(Headline_.article, JoinType.LEFT).get(Article_.id)));
            }
        }
        return specification;
    }
}
