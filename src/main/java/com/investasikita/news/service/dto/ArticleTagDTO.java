package com.investasikita.news.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ArticleTag entity.
 */
public class ArticleTagDTO implements Serializable {

    private Long id;

    private String tag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
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

        ArticleTagDTO articleTagDTO = (ArticleTagDTO) o;
        if (articleTagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), articleTagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArticleTagDTO{" +
            "id=" + getId() +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
