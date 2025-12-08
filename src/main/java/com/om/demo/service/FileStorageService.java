package com.om.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final String REEL_DIR = "uploads/reels/";

    public String saveFile(MultipartFile file) {
        try {
            File dir = new File(REEL_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(REEL_DIR + fileName);

            Files.copy(file.getInputStream(), path);

            return "/reels/" + fileName; // URL for Android
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file");
        }
    }
}

