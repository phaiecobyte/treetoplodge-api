package com.treetoplodge.treetoplodge_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean newsLetter;
    private boolean specialOffers;
    private String roomPreference;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
