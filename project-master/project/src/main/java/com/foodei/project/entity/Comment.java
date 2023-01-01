package com.foodei.project.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(generator = "custom_generate")
    @GenericGenerator(name = "custom_generate", strategy = "com.foodei.project.generator.CustomIdGenerator")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "create_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        createAt = LocalDateTime.now();
        updatedAt = createAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}