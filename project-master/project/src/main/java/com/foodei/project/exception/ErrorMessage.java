package com.foodei.project.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorMessage {
    private HttpStatus status;
    private String message;
}
