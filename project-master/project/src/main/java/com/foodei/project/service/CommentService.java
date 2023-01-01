package com.foodei.project.service;

import com.foodei.project.entity.Comment;
import com.foodei.project.repository.CommentRepository;
import com.foodei.project.request.CommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // Lấy danh sách comment của một blog
    public List<Comment> findCommentsByBlog(String id){
        return commentRepository.findByBlog_IdContainsIgnoreCaseOrderByUpdatedAtDesc(id);
    }

    // Lưu comment
    public Comment saveComment(Comment comment){
        return commentRepository.save(comment);
    }

    // Map từ comment sang request
    public CommentRequest toCommentRequest(String id){
        Optional<Comment> commentOptional = findById(id);
        Comment comment = commentOptional.get();
        return CommentRequest.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .blog(comment.getBlog())
                .user(comment.getUser())
                .build();
    }

    // Map từ request sang comment
    public Comment fromRequestToComment(CommentRequest commentRequest){
        return Comment.builder()
                .id(commentRequest.getId())
                .content(commentRequest.getContent())
                .blog(commentRequest.getBlog())
                .user(commentRequest.getUser())
                .build();
    }

    // Tìm comment theo id
    public Optional<Comment> findById(String id){
        return commentRepository.findById(id);
    }

    // Xóa comment
    public void deleteComment(String id){
        Optional<Comment> comment = findById(id);
        comment.ifPresent(value -> commentRepository.delete(comment.get()));
    }
}
