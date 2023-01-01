package com.foodei.project.repository;

import com.foodei.project.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findByUser_Id(String id);

    Optional<Image> findByUrl(String url);

}