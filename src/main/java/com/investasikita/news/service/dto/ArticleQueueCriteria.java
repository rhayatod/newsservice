package com.investasikita.news.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.investasikita.news.domain.enumeration.QueueStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the ArticleQueue entity. This class is used in ArticleQueueResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /article-queues?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArticleQueueCriteria implements Serializable {
    /**
     * Class for filtering QueueStatus
     */
    public static class QueueStatusFilter extends Filter<QueueStatus> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter publishDate;

    private StringFilter publishUser;

    private QueueStatusFilter status;

    private LongFilter articleId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(InstantFilter publishDate) {
        this.publishDate = publishDate;
    }

    public StringFilter getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(StringFilter publishUser) {
        this.publishUser = publishUser;
    }

    public QueueStatusFilter getStatus() {
        return status;
    }

    public void setStatus(QueueStatusFilter status) {
        this.status = status;
    }

    public LongFilter getArticleId() {
        return articleId;
    }

    public void setArticleId(LongFilter articleId) {
        this.articleId = articleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArticleQueueCriteria that = (ArticleQueueCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(publishDate, that.publishDate) &&
            Objects.equals(publishUser, that.publishUser) &&
            Objects.equals(status, that.status) &&
            Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        publishDate,
        publishUser,
        status,
        articleId
        );
    }

    @Override
    public String toString() {
        return "ArticleQueueCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (publishDate != null ? "publishDate=" + publishDate + ", " : "") +
                (publishUser != null ? "publishUser=" + publishUser + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (articleId != null ? "articleId=" + articleId + ", " : "") +
            "}";
    }

}
