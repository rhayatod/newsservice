package com.investasikita.news.service.impl;

import com.investasikita.news.service.CategoryService;
import com.investasikita.news.domain.Category;
import com.investasikita.news.repository.CategoryRepository;
import com.investasikita.news.repository.search.CategorySearchRepository;
import com.investasikita.news.service.dto.CategoryDTO;
import com.investasikita.news.service.mapper.CategoryMapper;
import com.vladmihalcea.concurrent.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final CategorySearchRepository categorySearchRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CategorySearchRepository categorySearchRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categorySearchRepository = categorySearchRepository;
    }

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        CategoryDTO result = categoryMapper.toDto(category);
        categorySearchRepository.save(category);
        return result;
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll(pageable)
            .map(categoryMapper::toDto);
    }


    /**
     * Get one category by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id)
            .map(categoryMapper::toDto);
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
        categorySearchRepository.deleteById(id);
    }

    @Override
    public void deleteAllSearch() {
        categorySearchRepository.deleteAll();
    }

    @Override
    public boolean reload(boolean deleteAll) {
        if (deleteAll)
            deleteAllSearch();

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<CategoryDTO> categoryPage = findAll(page);

            while (categoryPage.hasContent()) {
                try {
                    categoryPage.forEach(categoryDTO -> {
                        Category category = categoryMapper.toEntity(categoryDTO);
                        categorySearchRepository.save(category);
                    });

                    if (categoryPage.hasNext()) {
                        categoryPage = findAll(categoryPage.nextPageable());
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Search for the category corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Categories for query {}", query);
        return categorySearchRepository.search(queryStringQuery(query), pageable)
            .map(categoryMapper::toDto);
    }

    @Override
    public Page<CategoryDTO> getAll(Pageable pageable) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));

        return findAll(pageable);
    }

    @Override
    public Page<CategoryDTO> getAll(String name, Pageable pageable) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext())
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "id"));

        return categoryRepository.getAll(name, pageable).map(categoryMapper::toDto);
    }

    @Override
    public Boolean categoryExists(String name) {
        log.debug("Request to check if category already exists : {}", name);
        Optional<Category> optCategory = categoryRepository.findFirstByName(name);
        return optCategory.isPresent();
    }
}
