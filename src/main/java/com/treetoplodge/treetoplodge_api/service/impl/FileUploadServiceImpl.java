package com.treetoplodge.treetoplodge_api.service.impl;

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
                images.add("http://localhost:8080/uploads/" + file.getParentFile().getName() + "/" + file.getName());
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

        return "/uploads/" + directory + "/" + fileName;
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
        Path filePath = Paths.get(baseUploadDir, fileUrl.substring("/uploads/".length())).toAbsolutePath().normalize();
        Files.deleteIfExists(filePath);
    }

    @Override
    public void deleteFiles(List<String> fileUrls) throws IOException {
        for (String fileUrl : fileUrls) {
            deleteFile(fileUrl);
        }
    }
}
