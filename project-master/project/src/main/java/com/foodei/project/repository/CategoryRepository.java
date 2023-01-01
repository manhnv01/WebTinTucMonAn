package com.foodei.project.repository;

import com.foodei.project.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Page<Category> findByNameContainsIgnoreCase(String name, Pageable pageable);

    @Query(
            value = "SELECT c.id, c.name, c.avatar\n" +
                    "FROM blog_categories bc\n" +
                    "         left join category c on c.id = bc.categories_id\n" +
                    "         LEFT JOIN blog b on b.id = bc.blog_id\n" +
                    "GROUP BY c.id\n" +
                    "ORDER BY COUNT(b.id) DESC\n" +
                    "limit 4;",
            nativeQuery = true
    )
    List<Category> getCategoriesHighestBlogs();


}