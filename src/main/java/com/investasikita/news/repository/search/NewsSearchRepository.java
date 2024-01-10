package com.investasikita.news.repository.search;

import com.investasikita.news.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data Elasticsearch repository for the Article entity.
 */
public interface NewsSearchRepository extends ElasticsearchRepository<Article, Long> {

    // @Query(value = "{'bool' : {'must' : {'field' : {'isHeadline' : true}}}}")
    @Query(
        "{\"bool\" : " +
            "{\"must\" : " +
                "[{\"term\" : {\"headline\" : true} }, " +
                " {\"term\" : {\"status\" : \"published\"} }" +
            "]}}")
//        "{\"bool\" : {\"must\" : {\"term\" : {\"status\" : \"published\"}}}}")
    public Page<Article> getAllHeadlines(Pageable pageable);

//    @Query(
//        "{\"bool\" : " +
//            "{\"must\" : " +
//                "[{\"term\" : {\"category.id\" : ?0} }, " +
//                 "{\"term\" : {\"headline\" : true} }, " +
//                 "{\"term\" : {\"status\" : \"published\"} }" +
//            "]}}")
//    public Page<Article> getAllNews(Long categoryId, Pageable pageable);

    @Query(
//        "\"filter\" : { " +
            "{\"bool\" : " +
                "{\"must\" : [ " +
                "{\"term\" : {\"category.id\" : ?0} }, " +
                "{\"term\" : {\"headline\" : true} }, " +
                "{\"term\" : {\"status\" : \"published\"} }" +
                "]} " +
            "}"
//        "} "
//        "\"_source\": [\"title\", \"content\", \"tag\"]"
    )
    public Page<Article> getAllNews(Long categoryId, Pageable pageable);

}
