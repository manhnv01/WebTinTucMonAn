package com.foodei.project.request;

import com.foodei.project.anotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest{
    private String id;
    @NotNull
    @NotEmpty
    @NotBlank(message = "Full name cannot blank")
    private String name;

    @ValidEmail
    @NotNull
    @NotEmpty
    @NotBlank(message = "Email cannot blank")
    @Email(message = "Invalid email")
    private String email;

    @Size(min=6, max=20, message="Password length must between 6 and 20 characters")
    private String password;

    @Size(min=6, max=20, message="Password length must between 6 and 20 characters")
    private String confirmPassword;

    private String phone;
}
