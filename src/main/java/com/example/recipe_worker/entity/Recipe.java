package com.example.recipe_worker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    private UUID id;

    @Column(name = "chef_id", nullable = false)
    private UUID chefId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeStatus status;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeImage> images = new ArrayList<>();

    public enum RecipeStatus {
        DRAFT, PROCESSING, PUBLISHED, ARCHIVED
    }
}
