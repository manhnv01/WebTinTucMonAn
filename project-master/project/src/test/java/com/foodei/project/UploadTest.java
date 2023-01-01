package com.foodei.project;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadTest {


    private final Path rootPath = Paths.get("src/main/resources/static/uploads/cmIAk21");
    private final Path rootPath1 = Paths.get("uploads");



    @Test
    void makeFolder() {
        try {
            Files.createDirectories(rootPath1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void makeFolder1() {
        Path path = Paths.get("src\\main\\resources\\static\\uploads\\ii5XE");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void lastIndex() {
        int lastIndexOf = rootPath.toString().lastIndexOf("ic");
        String s = rootPath.toString().substring(lastIndexOf + 2);
    }


}
