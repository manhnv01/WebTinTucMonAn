package com.foodei.project.request;

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
public class UpdateUserRequest {
    private String id;
    private String name;
    private String email;
    private String phone;
    private List<String> role;
    private String password;
    private String confirmPassword;
    private String avatar;
    private Boolean enabled;
    private MultipartFile image;
}
