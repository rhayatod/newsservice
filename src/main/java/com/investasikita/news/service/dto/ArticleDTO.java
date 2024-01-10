package com.investasikita.news.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.investasikita.news.domain.enumeration.ArticleStatus;
import com.investasikita.news.domain.enumeration.LanguageOption;
import org.codehaus.jackson.annotate.JsonSetter;

/**
 * A DTO for the Article entity.
 */
public class ArticleDTO implements Serializable {

    private Long id;

    private String slug;

    private String title;

    private String shortDescription;

    private String imageURL;

    private String imageFileName;

//    @Lob
    private String content;

    private String author;

    private String externalAuthor;

    private String externalSource;

    private String externalSourceImage;

    private Instant createdDate;

    private Instant publishDate;

    private String publishUser;

    private Boolean posted;

    private Boolean isHeadline;

    private ArticleStatus status;

    private LanguageOption lang;


    private Long categoryId;

    private Set<ArticleTagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageURL() {
        return imageURL;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExternalAuthor() {
        return externalAuthor;
    }

    public void setExternalAuthor(String externalAuthor) {
        this.externalAuthor = externalAuthor;
    }

    public String getExternalSource() {
        return externalSource;
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

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @JsonSetter
    public void setCreatedDate(String createdDate) {
        if(createdDate != null && createdDate != "") this.createdDate = Instant.parse(createdDate);
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

    public Boolean isPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public Boolean getHeadline() {
        return isHeadline;
    }

    public void setHeadline(Boolean headline) {
        isHeadline = headline;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public LanguageOption getLang() {
        return lang;
    }

    public void setLang(LanguageOption lang) {
        this.lang = lang;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Set<ArticleTagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<ArticleTagDTO> articleTags) {
        this.tags = articleTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArticleDTO articleDTO = (ArticleDTO) o;
        if (articleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), articleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
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
            ", category=" + getCategoryId() +
            "}";
    }
}
