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

import com.investasikita.news.domain.ArticleQueue;
import com.investasikita.news.domain.*; // for static metamodels
import com.investasikita.news.repository.ArticleQueueRepository;
import com.investasikita.news.repository.search.ArticleQueueSearchRepository;
import com.investasikita.news.service.dto.ArticleQueueCriteria;
import com.investasikita.news.service.dto.ArticleQueueDTO;
import com.investasikita.news.service.mapper.ArticleQueueMapper;

/**
 * Service for executing complex queries for ArticleQueue entities in the database.
 * The main input is a {@link ArticleQueueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArticleQueueDTO} or a {@link Page} of {@link ArticleQueueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueueQueryService extends QueryService<ArticleQueue> {

    private final Logger log = LoggerFactory.getLogger(ArticleQueueQueryService.class);

    private final ArticleQueueRepository articleQueueRepository;

    private final ArticleQueueMapper articleQueueMapper;

    private final ArticleQueueSearchRepository articleQueueSearchRepository;

    public ArticleQueueQueryService(ArticleQueueRepository articleQueueRepository, ArticleQueueMapper articleQueueMapper, ArticleQueueSearchRepository articleQueueSearchRepository) {
        this.articleQueueRepository = articleQueueRepository;
        this.articleQueueMapper = articleQueueMapper;
        this.articleQueueSearchRepository = articleQueueSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ArticleQueueDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleQueueDTO> findByCriteria(ArticleQueueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ArticleQueue> specification = createSpecification(criteria);
        return articleQueueMapper.toDto(articleQueueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArticleQueueDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleQueueDTO> findByCriteria(ArticleQueueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArticleQueue> specification = createSpecification(criteria);
        return articleQueueRepository.findAll(specification, page)
            .map(articleQueueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleQueueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ArticleQueue> specification = createSpecification(criteria);
        return articleQueueRepository.count(specification);
    }

    /**
     * Function to convert ArticleQueueCriteria to a {@link Specification}
     */
    private Specification<ArticleQueue> createSpecification(ArticleQueueCriteria criteria) {
        Specification<ArticleQueue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ArticleQueue_.id));
            }
            if (criteria.getPublishDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublishDate(), ArticleQueue_.publishDate));
            }
            if (criteria.getPublishUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPublishUser(), ArticleQueue_.publishUser));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ArticleQueue_.status));
            }
            if (criteria.getArticleId() != null) {
                specification = specification.and(buildSpecification(criteria.getArticleId(),
                    root -> root.join(ArticleQueue_.article, JoinType.LEFT).get(Article_.id)));
            }
        }
        return specification;
    }
}
