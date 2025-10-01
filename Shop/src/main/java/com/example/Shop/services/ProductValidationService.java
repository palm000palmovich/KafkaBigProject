package com.example.Shop.services;

import com.example.Shop.dto.Item;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
@NoArgsConstructor
public class ProductValidationService {
    private final Logger logger = LoggerFactory.getLogger(ProductValidationService.class);
    private List<String> forbiddenWords;

    @Value("${app.kafka.forbidden-words}")
    private String forbiddenWordsString;

    @PostConstruct
    private void init() {
        logger.info("String with forbidden words: {}", forbiddenWordsString);
        this.forbiddenWords = Arrays.asList(forbiddenWordsString.split(","));
        logger.info("Loaded forbidden words: {}", forbiddenWords);
    }

    public boolean isValidProduct(Item item) {
        if (item.getName() == null || item.getDescription() == null ||
                item.getCategory() == null || item.getBrand() == null) {
            logger.error("Product {} has null fields", item.getProductId());
            return false;
        }

        String name = item.getName().toLowerCase();
        String description = item.getDescription().toLowerCase();
        String category = item.getCategory().toLowerCase();
        String brand = item.getBrand().toLowerCase();

        //Валидация
        //Если не содержится в списке запрещенных -> true
        boolean isValid = !containsForbiddenWord(name) &&
                !containsForbiddenWord(description) &&
                !containsForbiddenWord(category) &&
                !containsForbiddenWord(brand);

        if (!isValid) {
            logger.error("Product {} rejected due to forbidden words", item.getProductId());
        } else {
            logger.info("Product {} validation successful", item.getProductId());
        }

        //true -> валидно
        return isValid;
    }


    private boolean containsForbiddenWord(String text) {
        boolean isContains = forbiddenWords.stream().anyMatch(text::contains);
        logger.info("{} is valid: {}", text, !isContains);
        return isContains;

    }
}
