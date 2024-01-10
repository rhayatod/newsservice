package com.investasikita.news.service.mapper;

import com.investasikita.news.domain.*;
import com.investasikita.news.service.dto.HeadlineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Headline and its DTO HeadlineDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ArticleMapper.class})
public interface HeadlineMapper extends EntityMapper<HeadlineDTO, Headline> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "article.id", target = "articleId")
    HeadlineDTO toDto(Headline headline);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "articleId", target = "article")
    Headline toEntity(HeadlineDTO headlineDTO);

    default Headline fromId(Long id) {
        if (id == null) {
            return null;
        }
        Headline headline = new Headline();
        headline.setId(id);
        return headline;
    }
}
