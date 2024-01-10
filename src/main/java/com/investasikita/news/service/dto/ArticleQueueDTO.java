package com.investasikita.news.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import com.investasikita.news.domain.enumeration.QueueStatus;
import org.codehaus.jackson.annotate.JsonSetter;

/**
 * A DTO for the ArticleQueue entity.
 */
public class ArticleQueueDTO implements Serializable {

    private Long id;

    private Instant publishDate;

    private String publishUser;

    private QueueStatus status;


    private Long articleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    @JsonSetter
    public void setPublishDate(String publishDate) {
        if(publishDate != null && publishDate != "") this.publishDate = Instant.parse(publishDate);
    }

    public String getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public QueueStatus getStatus() {
        return status;
    }

    public void setStatus(QueueStatus status) {
        this.status = status;
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

        ArticleQueueDTO articleQueueDTO = (ArticleQueueDTO) o;
        if (articleQueueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), articleQueueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArticleQueueDTO{" +
            "id=" + getId() +
            ", publishDate='" + getPublishDate() + "'" +
            ", publishUser='" + getPublishUser() + "'" +
            ", status='" + getStatus() + "'" +
            ", article=" + getArticleId() +
            "}";
    }
}
