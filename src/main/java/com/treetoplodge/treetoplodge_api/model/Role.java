package com.treetoplodge.treetoplodge_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private ERole name;

    public enum ERole{
        ROLE_CUSTOMER,
        ROLE_SHOP,
        ROLE_ADMIN
    }
}
