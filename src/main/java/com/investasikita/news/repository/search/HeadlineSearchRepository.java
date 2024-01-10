package com.investasikita.news.repository.search;

import com.investasikita.news.domain.Headline;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Headline entity.
 */
public interface HeadlineSearchRepository extends ElasticsearchRepository<Headline, Long> {
}
