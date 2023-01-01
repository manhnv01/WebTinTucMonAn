package com.foodei.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgotPassword {
    @NotBlank(message = "Email cannot blank")
    @Email(message = "Invalid email")
    private String email;

}
