package com.investasikita.news.repository;

import com.investasikita.news.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Optional<Category> findFirstByName(String name);

    @Query(value = "SELECT DISTINCT cat FROM Category as cat "
        + "WHERE LOWER(cat.name) LIKE CONCAT('%', CONCAT(LOWER(:name),'%'))",
        countQuery = "SELECT COUNT (DISTINCT cat) FROM Category as cat "
            + "WHERE LOWER(cat.name) LIKE CONCAT('%', CONCAT(LOWER(:name),'%'))"
    )
    public Page<Category> getAll(@Param("name") String name, Pageable pageable);


}
