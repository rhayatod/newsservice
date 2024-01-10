package com.investasikita.news.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.investasikita.news.domain.enumeration.ArticleStatus;
import com.investasikita.news.domain.enumeration.LanguageOption;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Article entity. This class is used in ArticleResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /articles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArticleCriteria implements Serializable {
    /**
     * Class for filtering ArticleStatus
     */
    public static class ArticleStatusFilter extends Filter<ArticleStatus> {
    }
    /**
     * Class for filtering LanguageOption
     */
    public static class LanguageOptionFilter extends Filter<LanguageOption> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter slug;

    private StringFilter title;

    private StringFilter shortDescription;

    private StringFilter imageURL;

    private StringFilter imageFileName;

    private StringFilter author;

    private StringFilter externalAuthor;

    private StringFilter externalSource;

    private StringFilter externalSourceImage;

    private InstantFilter createdDate;

    private InstantFilter publishDate;

    private StringFilter publishUser;

    private BooleanFilter posted;

    private BooleanFilter isHeadline;

    private ArticleStatusFilter status;

    private LanguageOptionFilter lang;

    private LongFilter publishingQueuesId;

    private LongFilter headlineSettingsId;

    private LongFilter categoryId;

    private LongFilter tagsId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(StringFilter shortDescription) {
        this.shortDescription = shortDescription;
    }

    public StringFilter getImageURL() {
        return imageURL;
    }

    public void setImageURL(StringFilter imageURL) {
        this.imageURL = imageURL;
    }

    public StringFilter getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(StringFilter imageFileName) {
        this.imageFileName = imageFileName;
    }

    public StringFilter getAuthor() {
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
    }

    public StringFilter getExternalAuthor() {
        return externalAuthor;
    }

    public void setExternalAuthor(StringFilter externalAuthor) {
        this.externalAuthor = externalAuthor;
    }

    public StringFilter getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(StringFilter externalSource) {
        this.externalSource = externalSource;
    }

    public StringFilter getExternalSourceImage() {
        return externalSourceImage;
    }

    public void setExternalSourceImage(StringFilter externalSourceImage) {
        this.externalSourceImage = externalSourceImage;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
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

    public BooleanFilter getPosted() {
        return posted;
    }

    public void setPosted(BooleanFilter posted) {
        this.posted = posted;
    }

    public BooleanFilter getIsHeadline() {
        return isHeadline;
    }

    public void setIsHeadline(BooleanFilter isHeadline) {
        this.isHeadline = isHeadline;
    }

    public ArticleStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ArticleStatusFilter status) {
        this.status = status;
    }

    public LanguageOptionFilter getLang() {
        return lang;
    }

    public void setLang(LanguageOptionFilter lang) {
        this.lang = lang;
    }

    public LongFilter getPublishingQueuesId() {
        return publishingQueuesId;
    }

    public void setPublishingQueuesId(LongFilter publishingQueuesId) {
        this.publishingQueuesId = publishingQueuesId;
    }

    public LongFilter getHeadlineSettingsId() {
        return headlineSettingsId;
    }

    public void setHeadlineSettingsId(LongFilter headlineSettingsId) {
        this.headlineSettingsId = headlineSettingsId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArticleCriteria that = (ArticleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(title, that.title) &&
            Objects.equals(shortDescription, that.shortDescription) &&
            Objects.equals(imageURL, that.imageURL) &&
            Objects.equals(imageFileName, that.imageFileName) &&
            Objects.equals(author, that.author) &&
            Objects.equals(externalAuthor, that.externalAuthor) &&
            Objects.equals(externalSource, that.externalSource) &&
            Objects.equals(externalSourceImage, that.externalSourceImage) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(publishDate, that.publishDate) &&
            Objects.equals(publishUser, that.publishUser) &&
            Objects.equals(posted, that.posted) &&
            Objects.equals(isHeadline, that.isHeadline) &&
            Objects.equals(status, that.status) &&
            Objects.equals(lang, that.lang) &&
            Objects.equals(publishingQueuesId, that.publishingQueuesId) &&
            Objects.equals(headlineSettingsId, that.headlineSettingsId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(tagsId, that.tagsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        slug,
        title,
        shortDescription,
        imageURL,
        imageFileName,
        author,
        externalAuthor,
        externalSource,
        externalSourceImage,
        createdDate,
        publishDate,
        publishUser,
        posted,
        isHeadline,
        status,
        lang,
        publishingQueuesId,
        headlineSettingsId,
        categoryId,
        tagsId
        );
    }

    @Override
    public String toString() {
        return "ArticleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (shortDescription != null ? "shortDescription=" + shortDescription + ", " : "") +
                (imageURL != null ? "imageURL=" + imageURL + ", " : "") +
                (imageFileName != null ? "imageFileName=" + imageFileName + ", " : "") +
                (author != null ? "author=" + author + ", " : "") +
                (externalAuthor != null ? "externalAuthor=" + externalAuthor + ", " : "") +
                (externalSource != null ? "externalSource=" + externalSource + ", " : "") +
                (externalSourceImage != null ? "externalSourceImage=" + externalSourceImage + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (publishDate != null ? "publishDate=" + publishDate + ", " : "") +
                (publishUser != null ? "publishUser=" + publishUser + ", " : "") +
                (posted != null ? "posted=" + posted + ", " : "") +
                (isHeadline != null ? "isHeadline=" + isHeadline + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (lang != null ? "lang=" + lang + ", " : "") +
                (publishingQueuesId != null ? "publishingQueuesId=" + publishingQueuesId + ", " : "") +
                (headlineSettingsId != null ? "headlineSettingsId=" + headlineSettingsId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            "}";
    }

}
