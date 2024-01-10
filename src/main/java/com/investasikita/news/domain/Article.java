package com.investasikita.news.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.investasikita.news.domain.enumeration.ArticleStatus;

import com.investasikita.news.domain.enumeration.LanguageOption;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "image_file_name")
    private String imageFileName;

//    @Lob
//    @Type( type = "org.hibernate.type.TextType" )
    @Column(name = "content")
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "external_author")
    private String externalAuthor;

    @Column(name = "external_source")
    private String externalSource;

    @Column(name = "external_source_image")
    private String externalSourceImage;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Column(name = "publish_user")
    private String publishUser;

    @Column(name = "posted")
    private Boolean posted;

    @Column(name = "is_headline")
    private Boolean isHeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    private LanguageOption lang;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArticleQueue> publishingQueues = new HashSet<>();

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Headline> headlineSettings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("articles")
    private Category category;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "article_tags",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<ArticleTag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public Article slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public Article title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Article shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Article imageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getContent() {
        return content;
    }

    public Article content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public Article author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExternalAuthor() {
        return externalAuthor;
    }

    public Article externalAuthor(String externalAuthor) {
        this.externalAuthor = externalAuthor;
        return this;
    }

    public void setExternalAuthor(String externalAuthor) {
        this.externalAuthor = externalAuthor;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public Article externalSource(String externalSource) {
        this.externalSource = externalSource;
        return this;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }

    public String getExternalSourceImage() {
        return externalSourceImage;
    }

    public void setExternalSourceImage(String externalSourceImage) {
        this.externalSourceImage = externalSourceImage;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Article createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public Article publishDate(Instant publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishUser() {
        return publishUser;
    }

    public Article publishUser(String publishUser) {
        this.publishUser = publishUser;
        return this;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public Boolean isPosted() {
        return posted;
    }

    public Article posted(Boolean posted) {
        this.posted = posted;
        return this;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public Boolean getHeadline() {
        return isHeadline;
    }

    public void setHeadline(Boolean headline) {
        isHeadline = headline;
    }

    public Article status(ArticleStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public LanguageOption getLang() {
        return lang;
    }

    public Article lang(LanguageOption lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(LanguageOption lang) {
        this.lang = lang;
    }

    public Set<ArticleQueue> getPublishingQueues() {
        return publishingQueues;
    }

    public Article publishingQueues(Set<ArticleQueue> articleQueues) {
        this.publishingQueues = articleQueues;
        return this;
    }

    public Article addPublishingQueues(ArticleQueue articleQueue) {
        this.publishingQueues.add(articleQueue);
        articleQueue.setArticle(this);
        return this;
    }

    public Article removePublishingQueues(ArticleQueue articleQueue) {
        this.publishingQueues.remove(articleQueue);
        articleQueue.setArticle(null);
        return this;
    }

    public void setPublishingQueues(Set<ArticleQueue> articleQueues) {
        this.publishingQueues = articleQueues;
    }

    public Set<Headline> getHeadlineSettings() {
        return headlineSettings;
    }

    public Article headlineSettings(Set<Headline> headlines) {
        this.headlineSettings = headlines;
        return this;
    }

    public Article addHeadlineSettings(Headline headline) {
        this.headlineSettings.add(headline);
        headline.setArticle(this);
        return this;
    }

    public Article removeHeadlineSettings(Headline headline) {
        this.headlineSettings.remove(headline);
        headline.setArticle(null);
        return this;
    }

    public void setHeadlineSettings(Set<Headline> headlines) {
        this.headlineSettings = headlines;
    }

    public Category getCategory() {
        return category;
    }

    public Article category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<ArticleTag> getTags() {
        return tags;
    }

    public Article tags(Set<ArticleTag> articleTags) {
        this.tags = articleTags;
        return this;
    }

    public Article addTags(ArticleTag articleTag) {
        this.tags.add(articleTag);
        return this;
    }

    public Article removeTags(ArticleTag articleTag) {
        this.tags.remove(articleTag);
        return this;
    }

    public void setTags(Set<ArticleTag> articleTags) {
        this.tags = articleTags;
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
        Article article = (Article) o;
        if (article.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", title='" + getTitle() + "'" +
            ", shortDescription='" + getShortDescription() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", imageFileName='" + getImageFileName() + "'" +
            ", content='" + getContent() + "'" +
            ", author='" + getAuthor() + "'" +
            ", externalAuthor='" + getExternalAuthor() + "'" +
            ", externalSource='" + getExternalSource() + "'" +
            ", externalSourceImage='" + getExternalSourceImage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", publishUser='" + getPublishUser() + "'" +
            ", posted='" + isPosted() + "'" +
            ", isHeadline='" + getHeadline() + "'" +
            ", status='" + getStatus() + "'" +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
