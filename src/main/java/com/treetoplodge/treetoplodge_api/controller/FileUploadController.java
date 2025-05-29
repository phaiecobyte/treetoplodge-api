package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.Service.impl.FileUploadServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/upload/")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadServiceImpl service;

    @PostMapping(value = "single", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SHOP', 'ADMIN')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("directory") String directory) throws IOException {
        String fileUrl = service.uploadFile(file, directory);
        return ResponseEntity.ok(fileUrl);
    }

    @PostMapping(value = "multiple", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files,
                                                    @RequestParam("directory") String directory) throws IOException {
        List<String> fileUrls = service.uploadFiles(files, directory);
        return ResponseEntity.ok(fileUrls);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public ResponseEntity<Void> deleteFile(@RequestParam("fileUrl") String fileUrl) throws IOException {
        service.deleteFile(fileUrl);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("multiple")
    @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public ResponseEntity<Void> deleteFiles(@RequestBody List<String> fileUrls) throws IOException {
        service.deleteFiles(fileUrls);
        return ResponseEntity.ok().build();
    }
}
