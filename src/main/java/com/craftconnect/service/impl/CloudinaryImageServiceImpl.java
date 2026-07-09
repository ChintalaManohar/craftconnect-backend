package com.craftconnect.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.craftconnect.dto.ImageUploadResponse;
import com.craftconnect.service.CloudinaryImageService;

@Service
public class CloudinaryImageServiceImpl
        implements CloudinaryImageService {

    private final Cloudinary cloudinary;

    public CloudinaryImageServiceImpl(
            Cloudinary cloudinary) {

        this.cloudinary = cloudinary;
    }

    @Override
    public ImageUploadResponse uploadImage(
            MultipartFile file,
            String folder) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Image file is required");
        }

        Map<?, ?> result =
                cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "folder", folder,
                                "resource_type", "image"
                        ));

        return new ImageUploadResponse(
                result.get("secure_url").toString(),
                result.get("public_id").toString()
        );
    }
}