package com.craftconnect.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.craftconnect.dto.ImageUploadResponse;

public interface CloudinaryImageService {

    ImageUploadResponse uploadImage(
            MultipartFile file,
            String folder) throws IOException;
}