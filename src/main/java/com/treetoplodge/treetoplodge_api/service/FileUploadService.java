package com.treetoplodge.treetoplodge_api.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {
    String uploadFile(MultipartFile file, String directory) throws IOException;

    List<String> uploadFiles(List<MultipartFile> files, String directory) throws IOException;

    void deleteFile(String fileUrl) throws IOException;

    void deleteFiles(List<String> fileUrls) throws IOException;
}
