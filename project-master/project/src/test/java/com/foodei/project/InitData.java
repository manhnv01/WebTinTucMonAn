package com.foodei.project;

import com.foodei.project.entity.*;
import com.foodei.project.repository.*;
import com.foodei.project.service.BlogService;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class InitData {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private Slugify slugify;

    @Autowired
    private Random rd;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    void save_user_identity_card() {
        List<String> roles = new ArrayList(List.of("MEMBER","EDITOR","ADMIN"));

        for (int i = 0; i < 20; i++) {
            List<String> rolesRd = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                String rolerd = roles.get(rd.nextInt(roles.size()));

                if (!rolesRd.contains(rolerd)){
                    rolesRd.add(rolerd);
                }
            }

            User user = User.builder()
                    .name(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .password(passwordEncoder.encode(faker.number().digits(6)))
                    .role(rolesRd)
                    .enabled(rd.nextBoolean())
                    .phone(faker.phoneNumber().phoneNumber())
                    .build();

            userRepository.save(user);
        }
    }

    @Test
    void save_category() {
        Category category1 = Category.builder().name("Breakfast").avatar("/web/img/categories/cat-1.jpg").build();
        Category category2 = Category.builder().name("Lunch").avatar("/web/img/categories/cat-2.jpg").build();
        Category category3 = Category.builder().name("Dinner").avatar("/web/img/categories/cat-3.jpg").build();
        Category category4 = Category.builder().name("Desserts").avatar("/web/img/categories/cat-4.jpg").build();

        categoryRepository.saveAll(List.of(category1,category2,category3,category4));
    }

    @Test
    void save_image() {
        // Lấy ds user
        List<User> users = userRepository.findAll();

        for (int i = 0; i < 150; i++) {
            User userRd = users.get(rd.nextInt(users.size()));

            Image image = Image.builder()
                    .url(faker.company().logo())
                    .user(userRd)
                    .build();

            imageRepository.save(image);
        }
    }

    @Test
    void save_avatar_of_user() {
        // Lấy ds user
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            List<Image> images = imageRepository.findByUser_Id(user.getId());
            String imageRd = images.get(rd.nextInt(images.size())).getUrl();
            user.setAvatar(imageRd);
            userRepository.save(user);
        });
    }

//    @Test
//    void get_images_by_user_id() {
//        List<Image> images = imageRepository.findByUser_Id();
//        images.forEach(System.out::println);
//    }

    @Test
    void save_blog() {
        List<User> users = userRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        for (int i = 0; i < 30; i++) {
            User userRd = users.get(rd.nextInt(users.size()));

            List<Image> images = imageRepository.findByUser_Id(userRd.getId());
            String imageRd = images.get(rd.nextInt(images.size())).getUrl();

            List<Category> categoriesRd = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Category categoryRd = categories.get(rd.nextInt(categories.size()));
                if(!categoriesRd.contains(categoryRd)) {
                    categoriesRd.add(categoryRd);
                }
            }

            String title = faker.lorem().sentence(10);
            Blog blog = Blog.builder()
                    .title(title)
                    .slug(slugify.slugify(title))
                    .description(faker.lorem().sentence(50))
                    .content(faker.lorem().sentence(1000))
                    .thumbnail(imageRd)
                    .categories(categoriesRd)
                    .status(rd.nextInt(2))
                    .user(userRd)
                    .build();

            blogRepository.save(blog);
        }
    }

    @Test
    void save_comment() {
        List<User> users = userRepository.findAll();
        List<Blog> blogs = blogRepository.findAll();

        for (int i = 0; i < 100; i++) {
            User userRd = users.get(rd.nextInt(users.size()));
            Blog blogRd = blogs.get(rd.nextInt(blogs.size()));

            Comment comment = Comment.builder()
                    .content(faker.lorem().sentence(20))
                    .user(userRd)
                    .blog(blogRd)
                    .build();

            commentRepository.save(comment);
        }
    }

//    @Test
//    void get_all_blog_info_test() {
//        List<BlogInfo> rs = blogRepository.getAllBlogInfo();
//        rs.forEach(System.out::println);
//    }
}
