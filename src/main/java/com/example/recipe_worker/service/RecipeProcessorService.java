package com.example.recipe_worker.service;

import com.example.recipe_worker.dto.RecipeMessage;
import com.example.recipe_worker.entity.Recipe;
import com.example.recipe_worker.entity.RecipeImage;
import com.example.recipe_worker.repository.RecipeImageRepository;
import com.example.recipe_worker.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeProcessorService {

    private final RecipeRepository recipeRepository;
    private final RecipeImageRepository imageRepository;
    private final ImageProcessorService imageProcessorService;


    @Transactional
    public void processRecipe(RecipeMessage message) {
        try {
            log.info("Processing recipe: {}", message.getRecipeId());

            UUID recipeId = UUID.fromString(message.getRecipeId());
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new RuntimeException("Recipe not found: " + recipeId));


            if (message.getImages() != null && !message.getImages().isEmpty()) {
                for (RecipeMessage.ImageInfo imageInfo : message.getImages()) {
                    processRecipeImage(imageInfo, recipe);
                }
            }

            recipe.setStatus(Recipe.RecipeStatus.PUBLISHED);
            recipe.setPublishedAt(LocalDateTime.now());
            recipe.setUpdatedAt(LocalDateTime.now());

            recipeRepository.save(recipe);

            log.info("Successfully processed and published recipe: {}", recipeId);

        } catch (Exception e) {
            log.error("Failed to process recipe: {}", message.getRecipeId(), e);

            throw new RuntimeException("Recipe processing failed", e);
        }
    }


    private void processRecipeImage(RecipeMessage.ImageInfo imageInfo, Recipe recipe) {
        try {
            UUID imageId = UUID.fromString(imageInfo.getImageId());
            RecipeImage image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found: " + imageId));


            String[] processedUrls = imageProcessorService.processImage(
                    imageInfo.getOriginalUrl(),
                    imageInfo.getImageId()
            );


            image.setThumbnailUrl(processedUrls[0]);
            image.setMediumUrl(processedUrls[1]);
            image.setLargeUrl(processedUrls[2]);

            imageRepository.save(image);

            log.info("Successfully processed image: {}", imageId);

        } catch (Exception e) {
            log.error("Failed to process image: {}", imageInfo.getImageId(), e);

        }
    }
}
