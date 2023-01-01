package com.foodei.project.service;

import com.foodei.project.entity.Token;
import com.foodei.project.entity.User;
import com.foodei.project.exception.BadRequestException;
import com.foodei.project.exception.NotFoundException;
import com.foodei.project.request.LoginRequest;
import com.foodei.project.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthenticationManager authenticationManager;



    // Login
    public void login(LoginRequest loginRequest, HttpSession session){
        //Tạo đối tượng xác thực dựa trên thông tin loginRequest
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // Tiến hành xác thực
        Authentication authentication = authenticationManager.authenticate(token);

        // Lưu thông tin đối tượng đã đăng nhập
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lưu thông tin vào session
        session.setAttribute("MY_SESSION", authentication.getName());
    }

    // Logout
    public void logout(HttpSession session){
        session.invalidate();
    }

    //Tạo mới user
    public void registerNewUserAccount(UserRequest userRequest){
        Optional<User> userOptional = userService.getUserByEmail(userRequest.getEmail());

        // Kiểm tra xem user đã tồn tại chưa
        boolean existed = userService.emailExists(userRequest.getEmail());

        if (existed){
            // Nếu user được tìm thấy có trùng các thuộc tính và chưa được kích hoạt
            // Gửi email kích hoạt
            User user = userOptional.get();
            if (!user.getEnabled()
                    && user.getName().equals(userRequest.getName())
                    && user.getEmail().equals(userRequest.getEmail())){
                genrateTokenAndSendEmail(user);
                return;
            }

        }

        // Mã hóa password
        String passwordEncode = passwordEncoder.encode(userRequest.getPassword());

        // Tạo user mới
        User user = new User(userRequest.getName(), userRequest.getEmail(), passwordEncode, new ArrayList<>(List.of("MEMBER")), userRequest.getPhone());

         userService.saveUser(user);

         genrateTokenAndSendEmail(user);
    }


    //Sinh token - gửi email
    private void genrateTokenAndSendEmail(User user){
        //Sinh token
        String tokenString = UUID.randomUUID().toString();

        //Tạo token và lưu token
        Token token = new Token(
            tokenString,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        tokenService.saveToken(token);

        //Gửi email
        String domain = "localhost:8080";
        String link = "http://" + domain + "/confirm?token=" + tokenString;
        mailService.sendEmail(user.getEmail(),"Verify your account", link);

    }

    // Xác thực token
    public void confirmToken(String tokenString){
        //Lấy thông tin của token
        Token token = tokenService.getToken(tokenString).orElseThrow(() ->
                new NotFoundException("Token not found")
        );

        // Kiểm tra token đã được confirm chưa
        if (token.getConfirmedAt() != null){
            throw new BadRequestException("Token verified");
        }

        // Kiểm tra token đã hết hạn chưa
        LocalDateTime expriedAt = token.getExpiresAt();
        if (expriedAt.isBefore(LocalDateTime.now())){
            throw new BadRequestException("Token has expired");
        }

        // Active token
        tokenService.setConfirmedAt(tokenString);

        // Active user
        userService.enableUser(token.getUser().getEmail());
    }


    // Forgot password
    public void resetPassword(String email){
        // Kiểm tra xem user có tồn tại không
        boolean existed = userService.emailExists(email);

        if (existed){
            // nếu nhập đúng email thì gửi password mới
            User user = userService.getUserByEmail(email).get();
            sendMailResetPassword(user);
        }
    }

    // Kiểm tra mật khẩu xác nhận có đúng không
    public boolean checkConfirmPassword(String password, String confirm){
        return password.equals(confirm);
    }

    // Gửi email reset password
    private void sendMailResetPassword(User user){
        //Sinh password
        String newPassword = UUID.randomUUID().toString();
        String newPasswordEncode = passwordEncoder.encode(newPassword);

        //Lưu mật khẩu mới
        userService.resetPassword(user.getEmail(), newPasswordEncode);

        //Gửi email
        String content = "Your new password is: " + newPassword;
        mailService.sendEmail(user.getEmail(),"Reset your password.", content);

    }


}
