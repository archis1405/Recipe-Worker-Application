package com.example.recipe_worker.repository;

import com.example.recipe_worker.entity.RecipeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, UUID> {
}
