package com.foodei.project.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FilterCustom extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceCustom userDetailsServiceCustom;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            // Lấy thông tin username (email)  trong session
            String userEmail = (String) request.getSession().getAttribute("MY_SESSION");

            // Tạo đối tượng xác thực
            UsernamePasswordAuthenticationToken authentication = getAuthentication(userEmail);
            if(authentication == null){
                filterChain.doFilter(request, response);
                return;
            }

            // Lưu vào trong context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }

        public UsernamePasswordAuthenticationToken getAuthentication(String userEmail){
            if (userEmail == null) {
                return null;
            }

            // Lấy ra thông tin của user theo email
            UserDetails user = userDetailsServiceCustom.loadUserByUsername(userEmail);

            // Tạo đối tượng xác thực
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
