package com.foodei.project.repository;

import com.foodei.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByBlog_IdContainsIgnoreCaseOrderByUpdatedAtDesc(String id);





}