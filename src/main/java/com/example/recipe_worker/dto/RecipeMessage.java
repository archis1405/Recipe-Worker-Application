package com.example.recipe_worker.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String recipeId;
    private String chefId;
    private String title;
    private String summary;
    private List<String> ingredients;
    private List<String> steps;
    private List<String> labels;
    private Integer preparationTime;
    private Integer cookingTime;
    private Integer servings;
    private List<ImageInfo> images;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String imageId;
        private String originalUrl;
        private Integer displayOrder;
    }
}