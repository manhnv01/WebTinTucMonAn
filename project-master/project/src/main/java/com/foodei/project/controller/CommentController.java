package com.foodei.project.controller;

import com.foodei.project.entity.Blog;
import com.foodei.project.entity.Comment;
import com.foodei.project.entity.User;
import com.foodei.project.request.CommentRequest;
import com.foodei.project.service.BlogService;
import com.foodei.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;


    @PostMapping("/post-comment/{id}")
    public String postComment(@ModelAttribute CommentRequest commentRequest,
                              @PathVariable("id") String id,
                              HttpServletRequest request){
        // Lấy ra thông tin user đang đăng nhập
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentRequest.setUser(currentUser);

        Blog blog = blogService.getBlogById(id);
        commentRequest.setBlog(blog);

        Comment comment = commentService.fromRequestToComment(commentRequest);

        commentService.saveComment(comment);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    @PostMapping("/edit-comment/{id}")
    public String editComment(@RequestBody CommentRequest commentRequest,
                                      @PathVariable("id") String id,
                                      HttpServletRequest request){
//         Lấy ra thông tin user đang đăng nhập
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentRequest.setUser(currentUser);

        Blog blog = blogService.getBlogById(id);
        commentRequest.setBlog(blog);

        Comment comment = commentService.fromRequestToComment(commentRequest);

        commentService.saveComment(comment);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;

    }

    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") String id, HttpServletRequest request){
        commentService.deleteComment(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
