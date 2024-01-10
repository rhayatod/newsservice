package com.investasikita.news.service.mapper;

import com.investasikita.news.domain.*;
import com.investasikita.news.service.dto.ArticleQueueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ArticleQueue and its DTO ArticleQueueDTO.
 */
@Mapper(componentModel = "spring", uses = {ArticleMapper.class})
public interface ArticleQueueMapper extends EntityMapper<ArticleQueueDTO, ArticleQueue> {

    @Mapping(source = "article.id", target = "articleId")
    ArticleQueueDTO toDto(ArticleQueue articleQueue);

    @Mapping(source = "articleId", target = "article")
    ArticleQueue toEntity(ArticleQueueDTO articleQueueDTO);

    default ArticleQueue fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArticleQueue articleQueue = new ArticleQueue();
        articleQueue.setId(id);
        return articleQueue;
    }
}
