package com.investasikita.news.repository.search;

import com.investasikita.news.domain.ArticleQueue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ArticleQueue entity.
 */
public interface ArticleQueueSearchRepository extends ElasticsearchRepository<ArticleQueue, Long> {
}
