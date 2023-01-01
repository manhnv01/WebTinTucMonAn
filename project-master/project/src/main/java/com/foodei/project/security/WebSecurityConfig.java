package com.foodei.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceCustom userDetailsServiceCustom;

    @Autowired
    private FilterCustom filterCustom;

    @Autowired
    private AuthenticationEntryPointCustom authenticationEntryPointCustom;
    @Autowired
    private AccessDeniedHandlerCustom accessDeniedHandlerCustom;


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceCustom)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/","/blogs","/category/**","/about","/contact",
                            "/forgot-password",
                            "/register",
                            "/confirm",
                            "/error",
                            "/login").permitAll()
                    .antMatchers("/dashboard/user/profile",
                            "/dashboard/user/update-user",
                            "/post-comment/**",
                            "/edit-comment/**",
                            "/comment/delete/**").hasRole("MEMBER")
                    .antMatchers("/dashboard",
                            "/dashboard/categories",
                            "/dashboard/admin/category/edit/**",
                            "/dashboard/admin/category/detail/**",
                            "/dashboard/admin/category/create",
                            "/dashboard/categories/delete/**",
                            "/dashboard/blogs",
                            "/dashboard/admin/users",
                            "/dashboard/admin/create-user",
                            "/dashboard/admin/set-role/**",
                            "/dashboard/admin/remove-role/**",
                            "/dashboard/admin/user-active/**",
                            "/dashboard/admin/user-disable/**").hasRole("ADMIN")
                    .antMatchers("/dashboard/my-blogs/**",
                            "/dashboard/blogs/create-blog",
                            "/dashboard/blogs/detail/**",
                            "/dashboard/blogs/delete/**",
                            "/dashboard/blogs/edit/**").hasAnyRole("EDITOR","ADMIN")
//                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                .deleteCookies("MY_SESSION")
                    .permitAll()
                .and()
                .addFilterBefore(filterCustom, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/web/**","/admin/**","/uploads/**");
    }
}
