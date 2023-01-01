package com.foodei.project.service;

import com.foodei.project.entity.Category;
import com.foodei.project.repository.CategoryRepository;
import com.foodei.project.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy danh sách category
    public List<Category> findAllCategoryIndex(){
        return categoryRepository.findAll();
    }

    // Lấy page category phân trang, tìm theo tên
    public Page<Category> findAllCategory(int page, int pageSize, String name){
        Pageable pageable = PageRequest.of(page, pageSize);
        return categoryRepository.findByNameContainsIgnoreCase(name, pageable);
    }

    // Lấy page category phân trang
    public Page<Category> findAllPageCategory(int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return categoryRepository.findAll(pageable);
    }

    // Lấy danh sách category có nhiều bài viết nhất
    public List<Category> getCategoryMostBlog(){
        return categoryRepository.getCategoriesHighestBlogs();
    }

    // Tìm category theo id
    public Optional<Category> findById(String id){
        return categoryRepository.findById(id);
    }

    // Xóa category
    public void deleteCategory(String id){
        Optional<Category> category = findById(id);
        category.ifPresent(value -> categoryRepository.delete(value));
    }


    //Map từ category sang request
    public CategoryRequest toCategoryRequest(Category category){
        return CategoryRequest.builder()
                .id(category.getId())
                .name(category.getName())
                .avatar(category.getAvatar())
                .build();
    }

    // Map từ request sang category
    public Category fromRequestToCategory(CategoryRequest categoryRequest){

        // Kiểm tra có thay đổi avatar không
        String avatar = categoryRequest.getAvatar();
        if (avatar == null && categoryRequest.getId() != null){
            //Nếu avatar không thay đổi thì lấy avatar đang có
            avatar = findById(categoryRequest.getId()).get().getAvatar();
        }

        return Category.builder()
                .id(categoryRequest.getId())
                .name(categoryRequest.getName())
                .avatar(avatar)
                .build();
    }

    // Tạo và sửa category
    public Category createAndEdit(Category category){
        return categoryRepository.save(category);
    }
}
