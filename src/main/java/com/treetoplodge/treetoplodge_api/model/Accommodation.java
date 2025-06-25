package com.treetoplodge.treetoplodge_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull @Column(unique = true)
    private String accommodationId;

    @NotBlank(message = "name cannot is required")
    @Size(max = 255)
    private String name;
    
    @NotBlank(message = "type is required")
    @Size(max = 100)
    private String type;

    @NotBlank(message = "Description is required")
    @Size(max = 2000)
    private String description;

    @Size(max = 1000)
    private String mainImgUrl;
    
    @ElementCollection
    @CollectionTable(name = "accommodation_images",
        joinColumns = @JoinColumn(name = "accommodation_id", referencedColumnName = "AccommodationId")
    )
    @Column(name = "image_url")
    private List<String> additionalImgUrls;
    
    @NotNull
    @Positive
    private BigDecimal pricePerNight;
    
    @Positive
    @Min(1)
    private int maxGuests;
    
    @PositiveOrZero
    private int beds;
    private Integer bedrooms;
    
    @PositiveOrZero
    private int bathrooms;

    private float rating;
    private int reviewCount;
    private boolean available;
    
    @ElementCollection
    @CollectionTable(name = "accommodation_features",
        joinColumns = @JoinColumn(name = "accommodation_id", referencedColumnName = "AccommodationId")
    )
    @Column(name = "feature")
    private List<String> features;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}