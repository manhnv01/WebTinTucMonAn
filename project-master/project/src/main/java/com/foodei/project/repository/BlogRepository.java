package com.foodei.project.repository;

import com.foodei.project.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {

    //Index
    @Query("select b from Blog b where b.status = 1 order by b.publishedAt DESC")
    List<Blog> findBlogsByStatusEqualsOrderByPublishedAtDesc();


    @Query("""
            select b from Blog b inner join b.categories categories
            where upper(categories.name) like upper(concat('%', ?1, '%'))""")
    List<Blog> getByCategories_NameContainsIgnoreCaseAllIgnoreCase(String name);

    @Query("""
            select b from Blog b
            where upper(b.title) like upper(concat('%', ?1, '%')) and b.status = 1
            order by b.publishedAt DESC""")
    Page<Blog> getByTitleContainsIgnoreCaseAndStatusEqualsOrderByPublishedAtDesc(String title, Pageable pageable);

    @Query("""
            select b from Blog b inner join b.categories categories
            where upper(b.title) like upper(concat('%', ?1, '%')) and b.status = 1 and categories.name = ?2
            order by b.publishedAt DESC""")
    Page<Blog> getByTitleContainsIgnoreCaseAndStatusEqualsAndCategories_NameOrderByPublishedAtDesc(String title, String name, Pageable pageable);

    Blog getBlogById(String id);



    //Dashboard - Blogs
    Page<Blog> findByTitleContainsIgnoreCaseOrderByCreateAtDesc(String title, Pageable pageable);


    //Dashboard - own blogs
    @Query("""
            select b from Blog b
            where upper(b.title) like upper(concat('%', ?1, '%')) and b.user.id = ?2
            order by b.publishedAt DESC""")
    Page<Blog> findByTitleContainsIgnoreCaseAndUser_IdOrderByPublishedAtDesc(String title, String id, Pageable pageable);

    //Dashboard - profile
    List<Blog> findByUser_Id(String id);




}