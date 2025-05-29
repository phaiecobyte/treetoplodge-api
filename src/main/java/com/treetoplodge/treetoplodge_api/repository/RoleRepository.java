package com.treetoplodge.treetoplodge_api.repository;

import com.treetoplodge.treetoplodge_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(Role.ERole name);
}
