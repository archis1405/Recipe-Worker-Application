package com.example.recipe_worker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "recipe_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeImage {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "medium_url")
    private String mediumUrl;

    @Column(name = "large_url")
    private String largeUrl;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
}