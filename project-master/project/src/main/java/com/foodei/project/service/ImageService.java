package com.foodei.project.service;

import com.foodei.project.entity.Image;
import com.foodei.project.entity.User;
import com.foodei.project.exception.BadRequestException;
import com.foodei.project.exception.StorageException;
import com.foodei.project.repository.ImageRepository;
import com.foodei.project.utils.FileUploadUtil;
import com.foodei.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    // Folder path để upload file
    private final Path rootPath = Paths.get("uploads");
    private final Path windowsRootPath = Paths.get("C:\\Users\\Kien\\Desktop\\Back End\\project\\project\\target\\classes\\static\\uploads");


    public ImageService() {
        createFolder(rootPath);
//        createFolder(windowsRootPath);
    }

    // tạo folder nếu chưa tồn tại
    public void createFolder(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    // Tải ảnh lên
    public Image uploadImage(MultipartFile file, User user) throws IOException{
        // Xác thực file
        validate(file);

        // Tạo đường dẫn và tên file
        String userId = user.getId();
        Path uploadDir = Paths.get(rootPath.toString(),userId);
//        Path uploadDir = Paths.get(windowsRootPath.toString(),userId);
        String extension = Utils.getExtensionFile(file.getOriginalFilename());
        String fileName = userId + String.valueOf(Instant.now().getEpochSecond()) + "." + extension;
//        int lastIndexOf = uploadDir.toString().lastIndexOf("ic");
//        String urlName = uploadDir.toString().substring(lastIndexOf + 2);
        Path urlImage = Paths.get(uploadDir.toString(),fileName);
        String uploadDirStr = uploadDir.toString();
        String urlImageStr = urlImage.toString();

        if (uploadDirStr.contains("\\")){
            uploadDirStr = uploadDirStr.replace("\\", "/");
        }

        if(urlImageStr.contains("\\")){
            urlImageStr = urlImageStr.replace("\\", "/");
        }

        FileUploadUtil.saveFile(uploadDirStr, fileName, file);

        Image image = Image.builder()
                .url(urlImageStr)
                .user(user)
                .build();
        imageRepository.save(image);
        return image;

    }

    // Xác thực file
    public void validate(MultipartFile file) {
        // Validate file name
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.equals("")) {
            throw new BadRequestException("Invalid file name");
        }

        // Validate file extension
        String fileExtension = Utils.getExtensionFile(fileName);
        if (!Utils.checkFileExtenstion(fileExtension.toLowerCase())) {
            throw new BadRequestException("Invalid file");
        }

        // Kiểm tra size (upload dưới 2MB)
        if ((double) file.getSize() > (2 * 1024 * 1024)) {
            throw new BadRequestException("File must not excess 2MB");
        }
    }

    // Delete image
    public void deleteImage(String url) {
        try {
            Files.deleteIfExists(Paths.get(url));
            Image image = imageRepository.findByUrl(url)
                    .orElseThrow(() -> new StorageException("Errors occur while deleting file " + url));
            imageRepository.delete(image);
        } catch (IOException e) {
            throw new StorageException("Errors occur while deleting file " + url);
        }
    }

    // Tìm image bằng id
    public Image findById(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new StorageException("Errors occur while reading file " + id));
    }

    // Hiển thị url của image
    public String showUrl(String url) {
        if (url == null) {
            return "";
        } else if (!url.contains("http")) {
            return "\\" + url;
        } else {
            return url;
        }
    }
}
