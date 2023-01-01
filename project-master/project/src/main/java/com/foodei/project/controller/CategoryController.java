package com.foodei.project.controller;

import com.foodei.project.entity.Category;
import com.foodei.project.entity.Image;
import com.foodei.project.entity.User;
import com.foodei.project.request.CategoryRequest;
import com.foodei.project.service.CategoryService;
import com.foodei.project.service.ImageService;
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
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ImageService imageService;


    @GetMapping("/dashboard/categories")
    public String getCategoriesPage(Model model,
                                    @RequestParam(required = false, defaultValue = "") String keyword,
                                    @RequestParam(required = false,defaultValue = "1") Integer page){
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        if (page < 1){
            return "error/404-error";
        }

        Page<Category> categoryPage = categoryService.findAllCategory(page - 1, 10, keyword);

        List<Category> categoryList = categoryPage.getContent();
        model.addAttribute("categoryList", categoryList);

        int totalPages = categoryPage.getTotalPages();
        model.addAttribute("totalPages", totalPages);

        return "dashboard/categories";
    }

    @GetMapping("/dashboard/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") String id, HttpServletRequest request){
        categoryService.deleteCategory(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/dashboard/admin/category/detail/{id}")
    public String getCategoryDetailPage(Model model, @PathVariable("id") String id){
        Category category = categoryService.findById(id).get();
        model.addAttribute("category", category);

        CategoryRequest categoryRequest = categoryService.toCategoryRequest(category);
        model.addAttribute("categoryRequest", categoryRequest);

        return "dashboard/category-detail";
    }

    @PostMapping("/dashboard/admin/category/edit/{id}")
    public String editCategory(@PathVariable("id") String id,
                               @RequestParam("image")MultipartFile imageUpload,
                               @ModelAttribute CategoryRequest categoryRequest) throws IOException {
        // Lấy ra thông tin user đang đăng nhập
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Image image = new Image();
        if (imageUpload != null && !imageUpload.isEmpty()){
            image = imageService.uploadImage(imageUpload, user);
            categoryRequest.setAvatar(image.getUrl());
        }

        Category category = categoryService.fromRequestToCategory(categoryRequest);
        categoryService.createAndEdit(category);

        return "redirect:/dashboard/admin/category/detail/" + id;
    }

    @GetMapping("/dashboard/admin/category/create")
    public String getCategoryCreatePage(Model model){

        model.addAttribute("categoryRequest", new CategoryRequest());

        return "dashboard/category-create";
    }

    @PostMapping("/dashboard/admin/category/create")
    public String createCategory(@ModelAttribute("categoryRequest") CategoryRequest categoryRequest,
                                 @RequestParam("image")MultipartFile imageUpload) throws IOException {
        // Lấy ra thông tin user đang đăng nhập
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Image image = new Image();
        if (imageUpload != null && !imageUpload.isEmpty()){
            image = imageService.uploadImage(imageUpload, user);
            categoryRequest.setAvatar(image.getUrl());
        }
        Category category = categoryService.fromRequestToCategory(categoryRequest);
        categoryService.createAndEdit(category);

        return "redirect:/dashboard/categories";
    }
}
