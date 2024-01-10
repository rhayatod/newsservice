package com.investasikita.news.service.mapper;

import com.investasikita.news.domain.*;
import com.investasikita.news.service.dto.ArticleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Article and its DTO ArticleDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ArticleTagMapper.class})
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {

    @Mapping(source = "category.id", target = "categoryId")
    ArticleDTO toDto(Article article);

    @Mapping(target = "publishingQueues", ignore = true)
    @Mapping(target = "headlineSettings", ignore = true)
    @Mapping(source = "categoryId", target = "category")
    Article toEntity(ArticleDTO articleDTO);

    default Article fromId(Long id) {
        if (id == null) {
            return null;
        }
        Article article = new Article();
        article.setId(id);
        return article;
    }
}
