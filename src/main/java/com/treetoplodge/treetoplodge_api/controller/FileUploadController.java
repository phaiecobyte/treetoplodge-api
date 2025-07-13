package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.service.impl.FileUploadServiceImpl;
import com.treetoplodge.treetoplodge_api.exception.ApiResponse;
import com.treetoplodge.treetoplodge_api.exception.AppException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/upload/")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadServiceImpl service;

    @GetMapping("images")
    public ResponseEntity<Object> getImages() {
        return ApiResponse.success(service.listFile());
    }

    @PostMapping(value = "single", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SHOP', 'ADMIN')")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("directory") String directory) throws IOException {
        log.info("Intercept request upload single file");
        try{
            String fileUrl = service.uploadFile(file, directory);
            log.info("Succcess single upload file");
            return ApiResponse.success(fileUrl);
        }catch(AppException e){
            log.error("Application error occurred {}", e);
            return null;
        }catch(Exception e){
            log.error("An unexpected error occurred {}", e);
            return null;
        }finally{
            log.info("Completed single file upload operation");
        }
    }

    @PostMapping(value = "multiple", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public ResponseEntity<Object> uploadFiles(@RequestParam("files") List<MultipartFile> files,
                                                    @RequestParam("directory") String directory) throws IOException {
        log.info("Intercept request create multiple images");
        try{
            List<String> fileUrls = service.uploadFiles(files, directory);
            log.info("Success upload multiple file");
            return ApiResponse.success(fileUrls);
        }catch(AppException e){
            log.error("Application error occurred", e);
            return ApiResponse.error(e);
        }catch(Exception e){
            log.error("An unexpected error occurred", e);
            return ApiResponse.error(e);
        }finally{
            log.info("Completed multiple upload operation");
        }
    }

    @DeleteMapping("single")
    @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public ResponseEntity<Object> deleteFile(@RequestParam("fileUrl") String fileUrl) throws IOException {
        try{
            log.info("Successfuly single delete file");
            service.deleteFile(fileUrl);
            return ApiResponse.success(fileUrl);
        }catch(AppException e){
            log.error("Application error", e);
            return null;
        }catch(Exception e){
            log.error("An unexpected error occured", e);
            return null;
        }finally{
            log.info("Completed single delete file");
        }
    }

    @DeleteMapping("multiple")
    @PreAuthorize("hasAnyRole('ADMIN')")  // Update roles as needed
    public ResponseEntity<Object> deleteFiles(@RequestBody List<String> fileUrls) {
        try {
            log.info("Processing multiple file deletion request for {} files", fileUrls.size());
            service.deleteFiles(fileUrls);
            log.info("Successfully deleted multiple files");
            return ApiResponse.success(fileUrls);
        } catch (AppException e) {
            log.error("Application error during multiple file deletion", e);
            return ApiResponse.error(e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during multiple file deletion", e);
            return ApiResponse.error(e);
        } finally {
            log.info("Completed multiple file deletion operation");
        }
    }
}
