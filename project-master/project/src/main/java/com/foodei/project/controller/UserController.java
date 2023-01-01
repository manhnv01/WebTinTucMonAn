package com.foodei.project.controller;

import com.foodei.project.entity.Blog;
import com.foodei.project.entity.Image;
import com.foodei.project.entity.User;
import com.foodei.project.request.UpdateUserRequest;
import com.foodei.project.service.AuthService;
import com.foodei.project.service.BlogService;
import com.foodei.project.service.ImageService;
import com.foodei.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private AuthService authService;


    @GetMapping("/dashboard/admin/users")
    public String getUsersListPage(Model model,
                                   @RequestParam(required = false, defaultValue = "") String keyword,
                                   @RequestParam(required = false,defaultValue = "1") Integer page){
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        if (page < 1){
            return "error/404";
        }

        Page<User> userPage = userService.findAllUserByNameAndEmail(page - 1, 10, keyword, keyword);

        List<User> userList = userPage.getContent();
        model.addAttribute("userList",userList);

        model.addAttribute("userService", userService);

        int totalPages = userPage.getTotalPages();
        model.addAttribute("totalPages", totalPages);

        return "dashboard/users";
    }



    @GetMapping("/dashboard/admin/create-user")
    public String getCreateUserPage(Model model){
        model.addAttribute("updateUserRequest", new UpdateUserRequest());

        return "dashboard/user-create";
    }


    @PostMapping("/dashboard/admin/create-user")
    public String createUser(Model model,
                             @ModelAttribute UpdateUserRequest updateUserRequest){
        boolean emailExisted = userService.emailExists(updateUserRequest.getEmail());
        if (emailExisted){
            model.addAttribute("emailExisted", "This email already exists.");
            return "dashboard/user-create";
        }

        boolean checkPass = authService.checkConfirmPassword(updateUserRequest.getPassword(), updateUserRequest.getConfirmPassword());
        if (!checkPass){
            model.addAttribute("checkPass", "Your confirm password does not match");
            return "dashboard/register";
        }

        User user = userService.fromRequestToUser(updateUserRequest);
        userService.saveUser(user);

        return "redirect:/dashboard/admin/users";
    }



    @GetMapping("/dashboard/user/profile")
    public String getUserProfile(Model model){
        // Lấy ra thông tin user đang đăng nhập
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        UpdateUserRequest updateUserRequest = userService.toUpdateRequest(user);
        model.addAttribute("updateUserRequest", updateUserRequest);

        String showRole = userService.showRoles(user);
        model.addAttribute("showRole", showRole);

        List<Blog> blogs = blogService.getBlogsByUserId(user.getId());
        model.addAttribute("blogs", blogs);

        model.addAttribute("userService", userService);

        return "dashboard/user-profile";
    }


    @PostMapping("/dashboard/user/update-user")
    public String updateUser(@RequestParam("image") MultipartFile imageUpload,
                             @ModelAttribute UpdateUserRequest updateUserRequest) throws IOException {
        // Lấy ra thông tin user đang đăng nhập
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Image image = new Image();
        if (imageUpload != null && !imageUpload.isEmpty()){
            image = imageService.uploadImage(imageUpload, currentUser);
            updateUserRequest.setAvatar(image.getUrl());
        }

        User user = userService.fromRequestToUser(updateUserRequest);
        userService.saveUser(user);

        return "redirect:/dashboard/user/profile";
    }


    @GetMapping("/dashboard/admin/user-active/{id}")
    public String activeUser(@PathVariable("id") String id){
        User user = userService.getUserById(id);
        userService.enableUser(user.getEmail());

        return "redirect:/dashboard/admin/users";
    }


    @GetMapping("/dashboard/admin/user-disable/{id}")
    public String disableUser(@PathVariable("id") String id){
        User user = userService.getUserById(id);
        userService.disableUser(user.getEmail());

        return "redirect:/dashboard/admin/users";
    }

    @GetMapping("/dashboard/admin/set-role/{id}/{role}")
    public String setRole(@PathVariable("id") String id,
                          @PathVariable("role") String role,
                          HttpServletRequest request){
        userService.setRole(id, role);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/dashboard/admin/remove-role/{id}/{role}")
    public String removeRole(@PathVariable("id") String id,
                          @PathVariable("role") String role,
                          HttpServletRequest request){
        userService.removeRole(id, role);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
