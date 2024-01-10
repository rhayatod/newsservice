package com.investasikita.news.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.investasikita.news.domain.enumeration.QueueStatus;

/**
 * A ArticleQueue.
 */
@Entity
@Table(name = "article_queue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "articlequeue")
public class ArticleQueue implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Column(name = "publish_user")
    private String publishUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private QueueStatus status;

    @ManyToOne
    @JsonIgnoreProperties("publishingQueues")
    private Article article;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public ArticleQueue publishDate(Instant publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishUser() {
        return publishUser;
    }

    public ArticleQueue publishUser(String publishUser) {
        this.publishUser = publishUser;
        return this;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public QueueStatus getStatus() {
        return status;
    }

    public ArticleQueue status(QueueStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(QueueStatus status) {
        this.status = status;
    }

    public Article getArticle() {
        return article;
    }

    public ArticleQueue article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArticleQueue articleQueue = (ArticleQueue) o;
        if (articleQueue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), articleQueue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArticleQueue{" +
            "id=" + getId() +
            ", publishDate='" + getPublishDate() + "'" +
            ", publishUser='" + getPublishUser() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
