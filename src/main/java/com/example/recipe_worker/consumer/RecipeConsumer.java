package com.example.recipe_worker.consumer;

import com.example.recipe_worker.dto.RecipeMessage;
import com.example.recipe_worker.service.RecipeProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecipeConsumer {

    private final RecipeProcessorService recipeProcessorService;


    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consumeRecipeMessage(RecipeMessage message) {
        log.info("Received recipe message from queue: {}", message.getRecipeId());

        try {
            recipeProcessorService.processRecipe(message);
            log.info("Recipe processed successfully: {}", message.getRecipeId());
        } catch (Exception e) {
            log.error("Error processing recipe message: {}", message.getRecipeId(), e);
            // Re-throw to trigger RabbitMQ requeue/DLQ logic
            throw e;
        }
    }
}
