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

/**
 * Criteria class for the ArticleTag entity. This class is used in ArticleTagResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /article-tags?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArticleTagCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tag;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTag() {
        return tag;
    }

    public void setTag(StringFilter tag) {
        this.tag = tag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArticleTagCriteria that = (ArticleTagCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tag
        );
    }

    @Override
    public String toString() {
        return "ArticleTagCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tag != null ? "tag=" + tag + ", " : "") +
            "}";
    }

}
