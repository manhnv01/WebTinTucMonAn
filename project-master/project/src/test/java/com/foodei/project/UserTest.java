package com.foodei.project;

import com.foodei.project.entity.User;
import com.foodei.project.repository.UserRepository;
import com.foodei.project.request.UserRequest;
import com.foodei.project.service.AuthService;
import com.foodei.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Autowired
    private JavaMailSender javaMailSender;


    @Test
    void save_user() {
        User user1 = User.builder()
                .name("Admin")
                .email("admin@mail.com")
                .password(passwordEncoder.encode("123123"))
                .role(List.of("MEMBER", "EDITOR", "ADMIN"))
                .enabled(true)
                .build();

        User user2 = User.builder()
                .name("EDITOR")
                .email("editor@mail.com")
                .password(passwordEncoder.encode("123123"))
                .role(List.of("MEMBER", "EDITOR"))
                .enabled(true)
                .build();

        User user3 = User.builder()
                .name("MEMBER")
                .email("member@mail.com")
                .password(passwordEncoder.encode("123123"))
                .role(List.of("MEMBER"))
                .enabled(true)
                .build();

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void setEditor() {
        userService.setRole("0sELfR","EDITOR");

    }

    @Test
    void setAdmin() {
        userService.setRole("0sELfR","ADMIN");
    }

    @Test
    void removeEditor() {
        userService.removeRole("0sELfR","EDITOR");
    }

    @Test
    void removeAdmin() {
        userService.removeRole("0sELfR","ADMIN");
    }

    @Test
    void createUser() {
        String pass = passwordEncoder.encode("123123");
        UserRequest userRequest = new UserRequest("","a1","fukz2803@gmail.com",pass,pass,"0123456798");
        authService.registerNewUserAccount(userRequest);
    }

    @Test
    void delete() {
        User user = userRepository.findById("BuqS9");
        userRepository.delete(user);
    }


}
