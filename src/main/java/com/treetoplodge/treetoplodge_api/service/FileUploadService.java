package com.treetoplodge.treetoplodge_api.service;

import org.springframework.web.multipart.MultipartFile;

import com.treetoplodge.treetoplodge_api.model.DeleteResult;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {
    List<String> listFile ();
    String uploadFile(MultipartFile file, String directory) throws IOException;

    List<String> uploadFiles(List<MultipartFile> files, String directory) throws IOException;

    void deleteFile(String fileUrl) throws IOException;

    DeleteResult deleteFiles(List<String> fileUrls) throws IOException;
}
