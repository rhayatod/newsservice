package com.investasikita.news.repository.search;

import com.investasikita.news.domain.ArticleTag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ArticleTag entity.
 */
public interface ArticleTagSearchRepository extends ElasticsearchRepository<ArticleTag, Long> {
}
