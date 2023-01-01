package com.foodei.project.service;

import com.foodei.project.entity.User;
import com.foodei.project.repository.UserRepository;
import com.foodei.project.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //Hiển thị User dưới dạng phân trang + tìm kiếm theo tên hoặc email
    public Page<User> findAllUserByNameAndEmail(int page, int pageSize, String name, String email){
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.findByNameContainsIgnoreCaseAndEmailContainsIgnoreCase(name, email, pageable);
    }

    //Lấy thông tin user bằng id
    public User getUserById(String id){
        return userRepository.findById(id);
    }

    //Hiển thị danh sách role dưới dạng "a, b, c"
    public String showRoles(User user){
        return String.join(", ", user.getRole());
    }

    //Tìm user bằng email
    public Optional<User> getUserByEmail(String mail){
        return userRepository.findByEmail(mail);
    }

    // Kiểm tra email đã tồn tại chưa
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Active user
    public void enableUser(String email) {
        userRepository.enableUser(email);
    }

    // Disable user
    public void disableUser(String email) {
        userRepository.disableUser(email);
    }

    // Set role
    public User setRole(String id, String role){
        User user = getUserById(id);
        if(!user.getRole().contains(role)){
            user.getRole().add(role);
            return userRepository.save(user);
        } else {
            return user;
        }
    }

    // Remove role
    public User removeRole(String id, String role){
        User user = getUserById(id);
        if(user.getRole().contains(role)){
            user.getRole().remove(role);
            return userRepository.save(user);
        } else {
            return user;
        }
    }

    // Save user
    public User saveUser(User user){
       return userRepository.save(user);
    }

    //Reset Password
    public void resetPassword(String email, String password){
        User user = userRepository.findByEmail(email).get();
        user.setPassword(password);
    }

    // Map từ user sang request
    public UpdateUserRequest toUpdateRequest(User user){
        return UpdateUserRequest.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhone())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .enabled(user.getEnabled())
                .build();
    }

    // Map từ request sang user
    public User fromRequestToUser(UpdateUserRequest updateUserRequest){
        User user = new User();
        String id = updateUserRequest.getId();
        // Kiểm tra có thay đổi avatar không
        String avatar = updateUserRequest.getAvatar();
        if (avatar == null && id != null && !id.equals("")){
            //Nếu avatar không thay đổi thì lấy avatar đang có
            avatar = getUserById(updateUserRequest.getId()).getAvatar();
        }

        // Kiểm tra password có thay đổi không
        String password = updateUserRequest.getPassword();
        if (password == null || password.equals("")
                && id != null && !id.equals("")){
            // Nếu password không thay đổi thì lấy password cũ
            password = getUserById(updateUserRequest.getId()).getPassword();
            user.setPassword(password);
        } else {
            String passwordEncode = passwordEncoder.encode(password);
            user.setPassword(passwordEncode);
        }

        user.setId(updateUserRequest.getId());
        user.setName(updateUserRequest.getName());
        user.setEmail(updateUserRequest.getEmail());
        user.setRole(updateUserRequest.getRole());
        user.setPhone(updateUserRequest.getPhone());
        user.setAvatar(avatar);
        user.setEnabled(updateUserRequest.getEnabled());
        return user;

    }


}
