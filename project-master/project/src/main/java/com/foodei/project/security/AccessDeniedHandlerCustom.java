package com.foodei.project.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodei.project.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerCustom implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorMessage message = new ErrorMessage(HttpStatus.FORBIDDEN, "You do not have permission");

        ObjectMapper objectMapper = new ObjectMapper();
        String messageJSON = objectMapper.writeValueAsString(message);

        response.addHeader("Content-type", "application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(messageJSON);
    }
}
