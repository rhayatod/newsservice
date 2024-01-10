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

import com.investasikita.news.domain.ArticleTag;
import com.investasikita.news.domain.*; // for static metamodels
import com.investasikita.news.repository.ArticleTagRepository;
import com.investasikita.news.repository.search.ArticleTagSearchRepository;
import com.investasikita.news.service.dto.ArticleTagCriteria;
import com.investasikita.news.service.dto.ArticleTagDTO;
import com.investasikita.news.service.mapper.ArticleTagMapper;

/**
 * Service for executing complex queries for ArticleTag entities in the database.
 * The main input is a {@link ArticleTagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArticleTagDTO} or a {@link Page} of {@link ArticleTagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleTagQueryService extends QueryService<ArticleTag> {

    private final Logger log = LoggerFactory.getLogger(ArticleTagQueryService.class);

    private final ArticleTagRepository articleTagRepository;

    private final ArticleTagMapper articleTagMapper;

    private final ArticleTagSearchRepository articleTagSearchRepository;

    public ArticleTagQueryService(ArticleTagRepository articleTagRepository, ArticleTagMapper articleTagMapper, ArticleTagSearchRepository articleTagSearchRepository) {
        this.articleTagRepository = articleTagRepository;
        this.articleTagMapper = articleTagMapper;
        this.articleTagSearchRepository = articleTagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ArticleTagDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleTagDTO> findByCriteria(ArticleTagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ArticleTag> specification = createSpecification(criteria);
        return articleTagMapper.toDto(articleTagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArticleTagDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleTagDTO> findByCriteria(ArticleTagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArticleTag> specification = createSpecification(criteria);
        return articleTagRepository.findAll(specification, page)
            .map(articleTagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleTagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ArticleTag> specification = createSpecification(criteria);
        return articleTagRepository.count(specification);
    }

    /**
     * Function to convert ArticleTagCriteria to a {@link Specification}
     */
    private Specification<ArticleTag> createSpecification(ArticleTagCriteria criteria) {
        Specification<ArticleTag> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ArticleTag_.id));
            }
            if (criteria.getTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTag(), ArticleTag_.tag));
            }
        }
        return specification;
    }
}
