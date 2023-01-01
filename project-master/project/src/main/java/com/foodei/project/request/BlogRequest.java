package com.foodei.project.request;

import com.foodei.project.entity.Category;
import com.foodei.project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogRequest {
    private String id;
    private String title;
    private String description;
    private String content;
    private String slug;
    private String thumbnail;
    private int status;
    private User author;
    private List<Category> categories;
    private MultipartFile image;
}
