package com.craftconnect.controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.craftconnect.dto.ImageUploadResponse;
import com.craftconnect.service.CloudinaryImageService;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private static final Set<String> ALLOWED_FOLDERS =
            Set.of(
                    "craftconnect/products",
                    "craftconnect/profiles",
                    "craftconnect/covers"
            );

    private final CloudinaryImageService imageService;

    public ImageUploadController(
            CloudinaryImageService imageService) {

        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ImageUploadResponse uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder)
            throws IOException {

        if (!ALLOWED_FOLDERS.contains(folder)) {
            throw new IllegalArgumentException(
                    "Invalid image folder");
        }

        return imageService.uploadImage(file, folder);
    }
}