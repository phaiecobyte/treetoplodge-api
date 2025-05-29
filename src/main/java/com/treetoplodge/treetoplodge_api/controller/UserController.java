
package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.exception.ApiResponse;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.repository.UserRepository;
import com.treetoplodge.treetoplodge_api.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllUsers(Pageable pageable) {
        try {
            Page<User> users = userRepository.findAll(pageable);
            return ApiResponse.success(users);
        } catch (Exception e) {
            log.error("Error fetching users", e);
            return ApiResponse.error(e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> AppException.notFound("User not found"));
            return ApiResponse.success(user);
        } catch (Exception e) {
            log.error("Error fetching current user", e);
            return ApiResponse.error(e);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        try {
            User user = userRepository.findByUserId(id)
                    .orElseThrow(() -> AppException.notFound("User not found with ID: " + id));
            return ApiResponse.success(user);
        } catch (AppException e) {
            return ApiResponse.error(e);
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            return ApiResponse.error(e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> AppException.notFound("User not found with ID: " + id));

            // Only update non-sensitive fields
            user.setFullName(userDetails.getFullName());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setUpdatedAt(LocalDateTime.now());

            // Get current user for audit purposes
            UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            user.setUpdatedBy(currentUser.getUsername());

            userRepository.save(user);
            return ApiResponse.updated(user);
        } catch (AppException e) {
            return ApiResponse.error(e);
        } catch (Exception e) {
            log.error("Error updating user with ID: {}", id, e);
            return ApiResponse.error(e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deactivateUser(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> AppException.notFound("User not found with ID: " + id));

            // Soft delete - deactivate user
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());

            // Get current user for audit purposes
            UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            user.setUpdatedBy(currentUser.getUsername());

            userRepository.save(user);
            return ApiResponse.success("User deactivated successfully");
        } catch (AppException e) {
            return ApiResponse.error(e);
        } catch (Exception e) {
            log.error("Error deactivating user with ID: {}", id, e);
            return ApiResponse.error(e);
        }
    }
}