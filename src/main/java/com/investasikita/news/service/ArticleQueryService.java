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

import com.investasikita.news.domain.Article;
import com.investasikita.news.domain.*; // for static metamodels
import com.investasikita.news.repository.ArticleRepository;
import com.investasikita.news.repository.search.ArticleSearchRepository;
import com.investasikita.news.service.dto.ArticleCriteria;
import com.investasikita.news.service.dto.ArticleDTO;
import com.investasikita.news.service.mapper.ArticleMapper;

/**
 * Service for executing complex queries for Article entities in the database.
 * The main input is a {@link ArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArticleDTO} or a {@link Page} of {@link ArticleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueryService extends QueryService<Article> {

    private final Logger log = LoggerFactory.getLogger(ArticleQueryService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    private final ArticleSearchRepository articleSearchRepository;

    public ArticleQueryService(ArticleRepository articleRepository, ArticleMapper articleMapper, ArticleSearchRepository articleSearchRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.articleSearchRepository = articleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ArticleDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> findByCriteria(ArticleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleMapper.toDto(articleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArticleDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findByCriteria(ArticleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification, page)
            .map(articleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.count(specification);
    }

    /**
     * Function to convert ArticleCriteria to a {@link Specification}
     */
    private Specification<Article> createSpecification(ArticleCriteria criteria) {
        Specification<Article> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Article_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Article_.slug));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Article_.title));
            }
            if (criteria.getShortDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortDescription(), Article_.shortDescription));
            }
            if (criteria.getImageURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageURL(), Article_.imageURL));
            }
            if (criteria.getAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthor(), Article_.author));
            }
            if (criteria.getExternalAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalAuthor(), Article_.externalAuthor));
            }
            if (criteria.getExternalSource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalSource(), Article_.externalSource));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Article_.createdDate));
            }
            if (criteria.getPublishDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublishDate(), Article_.publishDate));
            }
            if (criteria.getPublishUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPublishUser(), Article_.publishUser));
            }
            if (criteria.getPosted() != null) {
                specification = specification.and(buildSpecification(criteria.getPosted(), Article_.posted));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Article_.status));
            }
            if (criteria.getLang() != null) {
                specification = specification.and(buildSpecification(criteria.getLang(), Article_.lang));
            }
            if (criteria.getPublishingQueuesId() != null) {
                specification = specification.and(buildSpecification(criteria.getPublishingQueuesId(),
                    root -> root.join(Article_.publishingQueues, JoinType.LEFT).get(ArticleQueue_.id)));
            }
            if (criteria.getHeadlineSettingsId() != null) {
                specification = specification.and(buildSpecification(criteria.getHeadlineSettingsId(),
                    root -> root.join(Article_.headlineSettings, JoinType.LEFT).get(Headline_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Article_.category, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getTagsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagsId(),
                    root -> root.join(Article_.tags, JoinType.LEFT).get(ArticleTag_.id)));
            }
        }
        return specification;
    }
}
