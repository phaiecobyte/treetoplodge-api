package com.treetoplodge.treetoplodge_api.service.impl;

import com.treetoplodge.treetoplodge_api.model.DeleteResult;
import com.treetoplodge.treetoplodge_api.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir:uploads}")
    private String baseUploadDir;

    @Value("${app.public-url}")
    private String publicUrl;

    @Override
    public List<String> listFile() {
        List<String> images = new ArrayList<>();
        File directory = new File(baseUploadDir);
        traverseDirectory(directory, images);
        return images;
    }

    private void traverseDirectory(File directory, List<String> images) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                traverseDirectory(file, images);
            } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".gif")) {
                images.add(publicUrl + "/uploads/" + file.getParentFile().getName() + "/" + file.getName());
            }
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String directory) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(baseUploadDir, directory).toAbsolutePath().normalize();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return publicUrl+"/uploads/" + directory + "/" + fileName;
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String directory) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileUrl = uploadFile(file, directory);
            fileUrls.add(fileUrl);
        }
        return fileUrls;
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        try {
            // Handle different URL formats
            String relativePath;
            if (fileUrl.contains("://")) {
                // Handle full URLs (with http/https)
                int uploadsIndex = fileUrl.indexOf("/uploads/");
                if (uploadsIndex == -1) {
                    log.warn("Invalid file URL format: {}", fileUrl);
                    throw new IOException("Invalid file URL format");
                }
                relativePath = fileUrl.substring(uploadsIndex + "/uploads/".length());
            } else if (fileUrl.startsWith("/uploads/")) {
                relativePath = fileUrl.substring("/uploads/".length());
            } else {
                relativePath = fileUrl;
            }
            
            Path filePath = Paths.get(baseUploadDir, relativePath).toAbsolutePath().normalize();
            log.info("Attempting to delete file: {}", filePath);
            
            if (Files.deleteIfExists(filePath)) {
                log.info("File successfully deleted: {}", filePath);
            } else {
                log.warn("File not found: {}", filePath);
            }
        } catch (Exception e) {
            log.error("Error deleting file: {}", fileUrl, e);
            throw e;
        }
    }

    @Override
    public DeleteResult deleteFiles(List<String> fileUrls) throws IOException {
        if (fileUrls == null || fileUrls.isEmpty()) {
            log.warn("No files provided for deletion");
            return new DeleteResult(List.of(), List.of());
        }
        
        log.info("Starting batch deletion of {} files", fileUrls.size());
        List<String> successfulDeletes = new ArrayList<>();
        List<String> failedDeletes = new ArrayList<>();
        
        for (String fileUrl : fileUrls) {
            try {
                deleteFile(fileUrl);
                successfulDeletes.add(fileUrl);
            } catch (Exception e) {
                log.error("Failed to delete file: {}", fileUrl, e);
                failedDeletes.add(fileUrl);
            }
        }
        
        log.info("Batch deletion complete. Success: {}, Failed: {}", 
                successfulDeletes.size(), failedDeletes.size());
        
        // Return results instead of throwing exception
        return new DeleteResult(successfulDeletes, failedDeletes);
    }
}
