package com.investasikita.news.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Headline entity. This class is used in HeadlineResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /headlines?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HeadlineCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter isGlobal;

    private InstantFilter startDate;

    private InstantFilter toDate;

    private IntegerFilter position;

    private LongFilter categoryId;

    private LongFilter articleId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(BooleanFilter isGlobal) {
        this.isGlobal = isGlobal;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getToDate() {
        return toDate;
    }

    public void setToDate(InstantFilter toDate) {
        this.toDate = toDate;
    }

    public IntegerFilter getPosition() {
        return position;
    }

    public void setPosition(IntegerFilter position) {
        this.position = position;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final HeadlineCriteria that = (HeadlineCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(isGlobal, that.isGlobal) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(position, that.position) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        isGlobal,
        startDate,
        toDate,
        position,
        categoryId,
        articleId
        );
    }

    @Override
    public String toString() {
        return "HeadlineCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (isGlobal != null ? "isGlobal=" + isGlobal + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (toDate != null ? "toDate=" + toDate + ", " : "") +
                (position != null ? "position=" + position + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (articleId != null ? "articleId=" + articleId + ", " : "") +
            "}";
    }

}
