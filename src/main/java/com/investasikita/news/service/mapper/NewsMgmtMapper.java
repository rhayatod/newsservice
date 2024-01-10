package com.investasikita.news.service.mapper;

import com.investasikita.news.domain.*;
import com.investasikita.news.domain.enumeration.ArticleStatus;
import com.investasikita.news.service.dto.*;
import com.investasikita.news.service.mapper.*;
import com.investasikita.news.web.rest.po.NewsPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Mapper for the entity Notification and its DTO NotificationDTO.
 */

@SuppressWarnings("unchecked")
public class NewsMgmtMapper {
    private final Logger log = LoggerFactory.getLogger(NewsMgmtMapper.class);

    ArticleMapper articleMapper;
    ArticleQueueMapper articleQueueMapper;
    ArticleTagMapper articleTagMapper;
    CategoryMapper categoryMapper;
    HeadlineMapper headlineMapper;

    public NewsMgmtMapper(
        ArticleMapper articleMapper,
        ArticleQueueMapper articleQueueMapper,
        ArticleTagMapper articleTagMapper,
        CategoryMapper categoryMapper,
        HeadlineMapper headlineMapper) {
        this.articleMapper = articleMapper;
        this.articleQueueMapper = articleQueueMapper;
        this.articleTagMapper = articleTagMapper;
        this.categoryMapper = categoryMapper;
        this.headlineMapper = headlineMapper;
    }

    public NewsPO toDto(Article article) {

        NewsPO newsPO = new NewsPO();

        newsPO.article = articleMapper.toDto(article);
        newsPO.category = categoryMapper.toDto(article.getCategory());

        Collection<Headline> headlineSettings = article.getHeadlineSettings();
        Collection<ArticleQueue> publishingQueues = article.getPublishingQueues();
        Collection<ArticleTag> articleTags = article.getTags();

        if (article.getStatus() == null) {
            article.setStatus(ArticleStatus.DRAFT);
        }

        if (headlineSettings != null) {
            newsPO.headlineSettings = (newsPO.headlineSettings == null)
                ? new ArrayList<>()
                : newsPO.headlineSettings;

            headlineSettings.forEach(headline -> {
                headline.article(article);
                newsPO.headlineSettings.add(headlineMapper.toDto(headline));
            });
        }

        if (publishingQueues != null) {
            newsPO.publishingQueues = (newsPO.publishingQueues == null)
                ? new ArrayList<>()
                : newsPO.publishingQueues;

            publishingQueues.forEach(articleQueue -> {
                articleQueue.article(article);
                newsPO.publishingQueues.add(articleQueueMapper.toDto(articleQueue));
            });
        }

        if (articleTags != null) {
            newsPO.articleTags = (newsPO.articleTags == null)
                ? new ArrayList<>()
                : newsPO.articleTags;

            articleTags.forEach(articleTag -> {
                newsPO.articleTags.add(articleTagMapper.toDto(articleTag));
            });
        }

        log.debug("News Article : {}", newsPO.article);
        log.debug("News Category : {}", newsPO.category);
        return newsPO;
    }

    public Article toEntity(NewsPO newsPO) {
        if (newsPO == null || newsPO.article == null) {
            return null;
        }

        ArticleDTO articleDTO = newsPO.article;
        CategoryDTO categoryDTO = newsPO.category;
        Collection<HeadlineDTO> headlineDTOs = newsPO.headlineSettings;
        Collection<ArticleQueueDTO> articleQueueDTOs = newsPO.publishingQueues;
        Collection<ArticleTagDTO> articleTagDTOs = newsPO.articleTags;

        Category category = categoryMapper.toEntity(categoryDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article.setCategory(category);

        if (headlineDTOs != null) {
            headlineDTOs.forEach(headlineDTO -> {
                article.addHeadlineSettings(headlineMapper.toEntity(headlineDTO));
            });
        }

        if (articleQueueDTOs != null) {
            articleQueueDTOs.forEach(articleQueueDTO -> {
                article.addPublishingQueues(articleQueueMapper.toEntity(articleQueueDTO));
            });
        }

        if (articleTagDTOs != null) {
            articleTagDTOs.forEach(articleTagDTO -> {
                article.addTags(articleTagMapper.toEntity(articleTagDTO));
            });
        }

        return article;
    }

}
