package com.investasikita.news.service.mapper;

import com.investasikita.news.domain.*;
import com.investasikita.news.service.dto.ArticleTagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ArticleTag and its DTO ArticleTagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArticleTagMapper extends EntityMapper<ArticleTagDTO, ArticleTag> {



    default ArticleTag fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArticleTag articleTag = new ArticleTag();
        articleTag.setId(id);
        return articleTag;
    }
}
