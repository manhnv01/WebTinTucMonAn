package com.foodei.project.controller;

import com.foodei.project.request.ForgotPassword;
import com.foodei.project.request.LoginRequest;
import com.foodei.project.request.UserRequest;
import com.foodei.project.service.AuthService;
import com.foodei.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;



    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("loginRequest", new LoginRequest("",""));
        return "dashboard/login";
    }

    @PostMapping("/login")
    public String login(Model model, @ModelAttribute LoginRequest request, HttpSession session){
        boolean existed = userService.emailExists(request.getEmail());
        if (!existed){
            model.addAttribute("fail", "Your email or password is incorrect.");
            return "dashboard/login";
        }
        authService.login(request, session);
        return "redirect:/";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        authService.logout(session);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        UserRequest userRequest = new UserRequest();
        model.addAttribute("userRequest",userRequest);
        return "dashboard/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute("userRequest") @Valid UserRequest userRequest){

        boolean checkPass = authService.checkConfirmPassword(userRequest.getPassword(), userRequest.getConfirmPassword());
        if (!checkPass){
            model.addAttribute("message", "Your confirm password does not match");
            return "dashboard/register";
        }

        boolean emailExisted = userService.emailExists(userRequest.getEmail());
        if (emailExisted){
            model.addAttribute("emailExisted", "This email already exists.");
            return "dashboard/register";
        }

        authService.registerNewUserAccount(userRequest);
        model.addAttribute("success", "Success.");

        return "dashboard/register" ;
    }

    @GetMapping("/confirm")
    public String confirmToken(@RequestParam("token") String token){
        authService.confirmToken(token);
        return "redirect:/login";
    }


    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Model model){
        model.addAttribute("user", new ForgotPassword());
        model.addAttribute("userService", userService);
        return "dashboard/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@ModelAttribute("user") ForgotPassword forgotPassword, Model model){
        // Kiểm tra email có tồn tại không
        if(!userService.emailExists(forgotPassword.getEmail())){
            model.addAttribute("messageFail", "We couldn't find your email. Please re-enter your email or register.");
            return "dashboard/forgot-password";
        } else {
            authService.resetPassword(forgotPassword.getEmail());
            return "dashboard/forgot-password-verify";
        }
    }




}
