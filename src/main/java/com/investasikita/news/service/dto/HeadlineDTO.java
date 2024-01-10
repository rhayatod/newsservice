package com.investasikita.news.service.dto;
import org.codehaus.jackson.annotate.JsonSetter;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Headline entity.
 */
public class HeadlineDTO implements Serializable {

    private Long id;

    private Boolean isGlobal;

    private Instant startDate;

    private Instant toDate;

    private Integer position;


    private Long categoryId;

    private Long articleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    @JsonSetter
    public void setStartDate(String startDate) {
        if(startDate != null && startDate != "") this.startDate = Instant.parse(startDate);
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    @JsonSetter
    public void setToDate(String toDate) {
        if(toDate != null && toDate != "") this.toDate = Instant.parse(toDate);
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
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

        HeadlineDTO headlineDTO = (HeadlineDTO) o;
        if (headlineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), headlineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HeadlineDTO{" +
            "id=" + getId() +
            ", isGlobal='" + isIsGlobal() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", position=" + getPosition() +
            ", category=" + getCategoryId() +
            ", article=" + getArticleId() +
            "}";
    }
}
