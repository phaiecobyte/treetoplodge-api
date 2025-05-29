package com.treetoplodge.treetoplodge_api.repository;

import com.treetoplodge.treetoplodge_api.model.RefreshToken;
import com.treetoplodge.treetoplodge_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    
    @Modifying
    int deleteByUser(User user);
    
    // Add this explicit query to delete by user ID
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
    
    // Check if a token exists for a user
    boolean existsByUser(User user);
}