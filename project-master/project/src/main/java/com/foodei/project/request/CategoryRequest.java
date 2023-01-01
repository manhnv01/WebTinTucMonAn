package com.foodei.project.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    private String id;
    private String name;
    private String avatar;
    private MultipartFile image;
}
