package com.foodei.project.service;

import com.foodei.project.entity.Blog;
import com.foodei.project.entity.Category;
import com.foodei.project.repository.BlogRepository;
import com.foodei.project.request.BlogRequest;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogService {


    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private Slugify slugify;


    public List<Blog> findAll(){
        return blogRepository.findAll();
    }

    // Danh sách list blog sắp xếp theo ngày đăng mới nhất
    public List<Blog> findAllBlogsIndex(){
        return blogRepository.findBlogsByStatusEqualsOrderByPublishedAtDesc();
    }

    // Danh sách blog phân trang, tìm blog theo title
    public Page<Blog> findAllBlogsPageContainTitle(int page, int pageSize, String title){
        Pageable pageable = PageRequest.of(page, pageSize);
        return blogRepository.getByTitleContainsIgnoreCaseAndStatusEqualsOrderByPublishedAtDesc(title, pageable);
    }

    // Lấy ra list blog dựa theo category
    public List<Blog> getBlogsByCategoryName(String name){
        return blogRepository.getByCategories_NameContainsIgnoreCaseAllIgnoreCase(name);
    }

    // Lấy ra list blog có lượng comment nhiều nhất
    public List<Blog> sortCommentBlog(){
        List<Blog> blogs = findAllBlogsIndex();
        Collections.sort(blogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog o1, Blog o2) {
                return Integer.compare(o2.getComments().size(), o1.getComments().size());
            }
        });
        return blogs;
    }

    //Lấy ra 4 bài blog có lượng comment nhiều nhất
    public List<Blog> getBlogsByHighComment(){
        List<Blog> blogs = sortCommentBlog();
        List<Blog> newblogs = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            newblogs.add(i - 1,blogs.get(i));
        }
        return newblogs;
    }

    // Lấy ra bài viết có lượng comment cao nhất
    public Blog getBlogHighestComment(){
        List<Blog> blogs = sortCommentBlog();
        return blogs.get(0);
    }

    // Lấy list 5 bài blog mới nhất
    public List<Blog> getBlogsHeader(){
        List<Blog> blogs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            blogs.add(i,findAllBlogsIndex().get(i));
        }
        return blogs;
    }

    //Lấy ra page blog dựa trên title và category name
    public Page<Blog> getAllBlogsByCategoryAndTitle(int page, int pageSize, String title, String name){
        Pageable pageable = PageRequest.of(page, pageSize);
        return blogRepository.getByTitleContainsIgnoreCaseAndStatusEqualsAndCategories_NameOrderByPublishedAtDesc(title,name,pageable);
    }

    //Lấy thông tin của blog dựa trên id
    public Blog getBlogById(String id){
        return blogRepository.getBlogById(id);
    }

    //Hiển thị danh sách category của blog dưới dạng String "a, b, c,.."
    public String showCategoryBlog(Blog blog){
        return String.join(", ", blog.getCategories().stream().map(Category::getName).toList());
    }


    //Lấy blog phân trang, tìm theo title hiển thị trong dashboard
    public Page<Blog> findAllBlogsPageByTitle(int page, int pageSize, String title){
        Pageable pageable = PageRequest.of(page, pageSize);
        return blogRepository.findByTitleContainsIgnoreCaseOrderByCreateAtDesc(title, pageable);
    }

    //Lấy blog phân trang, tìm kiếm theo title của một user
    public Page<Blog> findAllBlogByUserId(int page, int pageSize, String title, String id){
        Pageable pageable = PageRequest.of(page, pageSize);
        return blogRepository.findByTitleContainsIgnoreCaseAndUser_IdOrderByPublishedAtDesc(title, id, pageable);
    }

    //Xóa blog bằng id
    public void deleteBlog(String id){
        Optional<Blog> blog = blogRepository.findById(id);
        blog.ifPresent(value -> blogRepository.delete(value));
    }

    //Lấy danh sách blog của user
    public List<Blog> getBlogsByUserId(String id){
        return blogRepository.findByUser_Id(id);
    }

    //Dashboard - create and edit
    public Blog createAndEdit(Blog blog){
        blog.setSlug(slugify.slugify(blog.getTitle()));
        return blogRepository.save(blog);
    }

    //Map từ blog sang blogRequest
    public BlogRequest toBlogRequest(Blog blog){
        return BlogRequest.builder()
                .id(blog.getId())
                .author(blog.getUser())
                .categories(blog.getCategories())
                .content(blog.getContent())
                .description(blog.getDescription())
                .slug(blog.getSlug())
                .status(blog.getStatus())
                .thumbnail(blog.getThumbnail())
                .title(blog.getTitle())
                .build();
    }


    //Map từ blogRequest sang blog
    public Blog fromRequestToBlog(BlogRequest blogRequest){
        String thumbnail = blogRequest.getThumbnail();
        // Kiểm tra có thay đổi avatar không
        if (thumbnail == null && blogRequest.getId() != null){
            //Nếu avatar không thay đổi thì lấy avatar đang có
            thumbnail = getBlogById(blogRequest.getId()).getThumbnail();
        }

        return Blog.builder()
                .id(blogRequest.getId())
                .title(blogRequest.getTitle())
                .description(blogRequest.getDescription())
                .content(blogRequest.getContent())
                .status(blogRequest.getStatus())
                .categories(blogRequest.getCategories())
                .slug(blogRequest.getSlug())
                .thumbnail(thumbnail)
                .status(blogRequest.getStatus())
                .build();
    }
}
