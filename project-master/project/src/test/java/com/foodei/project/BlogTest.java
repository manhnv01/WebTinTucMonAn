package com.foodei.project;

import com.foodei.project.entity.Blog;
import com.foodei.project.entity.User;
import com.foodei.project.request.BlogRequest;
import com.foodei.project.service.BlogService;
import com.foodei.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogTest {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;


    @Test
    void deleteBlog() {
        blogService.deleteBlog("Qr4E4");
    }

    @Test
    void editBlog() {
        Blog blog = blogService.getBlogById("mek70");
        BlogRequest blogRequest = blogService.toBlogRequest(blog);
        blogRequest.setTitle("test test test test testtest 123");
        Blog blogedit = blogService.fromRequestToBlog(blogRequest);
        blogService.createAndEdit(blogedit);
    }
}
