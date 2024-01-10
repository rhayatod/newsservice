package com.investasikita.news.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Headline.
 */
@Entity
@Table(name = "headline")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "headline")
public class Headline implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "is_global")
    private Boolean isGlobal;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "to_date")
    private Instant toDate;

    @Column(name = "position")
    private Integer position;

    @ManyToOne
    @JsonIgnoreProperties("headlines")
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties("headlineSettings")
    private Article article;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsGlobal() {
        return isGlobal;
    }

    public Headline isGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
        return this;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Headline startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public Headline toDate(Instant toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public Integer getPosition() {
        return position;
    }

    public Headline position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Category getCategory() {
        return category;
    }

    public Headline category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Article getArticle() {
        return article;
    }

    public Headline article(Article article) {
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
        Headline headline = (Headline) o;
        if (headline.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), headline.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Headline{" +
            "id=" + getId() +
            ", isGlobal='" + isIsGlobal() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", position=" + getPosition() +
            "}";
    }
}
