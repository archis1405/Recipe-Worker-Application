package com.example.recipe_worker.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ImageProcessorService {

    @Value("${image.upload-dir:uploads/images}")
    private String uploadDir;

    public String[] processImage(String originalUrl, String imageId) {
        try {
            // Ensure upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }


            String filename = extractFilename(originalUrl);
            File originalFile = new File(uploadDir, filename);

            if (!originalFile.exists()) {
                log.warn("Original file not found: {}", originalFile.getAbsolutePath());
                return new String[]{null, null, null};
            }


            String thumbnailPath = uploadDir + "/thumb_" + imageId + ".jpg";
            String mediumPath = uploadDir + "/med_" + imageId + ".jpg";
            String largePath = uploadDir + "/large_" + imageId + ".jpg";


            Thumbnails.of(originalFile)
                    .size(150, 150)
                    .outputFormat("jpg")
                    .outputQuality(0.85)
                    .toFile(thumbnailPath);


            Thumbnails.of(originalFile)
                    .size(500, 500)
                    .outputFormat("jpg")
                    .outputQuality(0.90)
                    .toFile(mediumPath);


            Thumbnails.of(originalFile)
                    .size(1200, 1200)
                    .outputFormat("jpg")
                    .outputQuality(0.95)
                    .toFile(largePath);

            log.info("Successfully processed images for: {}", imageId);

            return new String[]{
                    "/uploads/images/thumb_" + imageId + ".jpg",
                    "/uploads/images/med_" + imageId + ".jpg",
                    "/uploads/images/large_" + imageId + ".jpg"
            };

        } catch (IOException e) {
            log.error("Failed to process image: {}", imageId, e);
            return new String[]{null, null, null};
        }
    }


    private String extractFilename(String url) {
        if (url == null) {
            return null;
        }
        int lastSlash = url.lastIndexOf('/');
        return lastSlash >= 0 ? url.substring(lastSlash + 1) : url;
    }
}
