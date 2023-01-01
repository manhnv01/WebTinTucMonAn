package com.foodei.project.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
@TypeDef(
        name = "json",
        typeClass = JsonStringType.class
)
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "custom_generate")
    @GenericGenerator(name = "custom_generate", strategy = "com.foodei.project.generator.CustomIdGenerator")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "password", nullable = false)
    private String password;

    @Type(type = "json")
    @Column(name = "role", columnDefinition = "json")
    private List<String> role;

    @Column(name = "phone")
    private String phone;

    @Column(name = "enabled")
    private Boolean enabled = false;


    public User(String name, String email, String password, List<String> role, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        this.getRole().forEach(r -> roles.add(new SimpleGrantedAuthority("ROLE_" + r)));
        return roles;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    
}