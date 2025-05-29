package com.treetoplodge.treetoplodge_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class FoodBeverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String code;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "description required")
    private String description;

    @NotNull @Positive
    private BigDecimal price;
    private String mainImgUrl;

    @ElementCollection
    @CollectionTable(name = "foodbeverage_images",
        joinColumns = @JoinColumn(name = "foodbeverage_code", referencedColumnName = "code")
    )
    @Column(name = "image_url")
    private List<String> additionalImageUrls;

    @NotBlank(message = "category is required")
    private String category;

    @Min(1) @Max(5)
    private int rating;
    private String review;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
