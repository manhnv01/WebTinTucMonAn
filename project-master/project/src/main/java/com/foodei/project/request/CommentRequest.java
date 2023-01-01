package com.foodei.project.request;

import com.foodei.project.entity.Blog;
import com.foodei.project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private String id;
    private String content;
    private Blog blog;
    private User user;
}
