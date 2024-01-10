package com.investasikita.news.web.rest.po;

import com.investasikita.news.service.dto.*;

import java.io.Serializable;
import java.util.Collection;

@SuppressWarnings("serial")
public class NewsPO implements Serializable {

    public NewsPO() {
    }

    public ArticleDTO article;
    public CategoryDTO category;
    public Collection<HeadlineDTO> headlineSettings;
    public Collection<ArticleTagDTO> articleTags;
    public Collection<ArticleQueueDTO> publishingQueues;
}
