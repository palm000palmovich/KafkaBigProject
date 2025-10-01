package com.example.Shop.controllers;


import com.example.Shop.dto.Item;
import com.example.Shop.exceptions.CensoreException;
import com.example.Shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private Logger logger = LoggerFactory.getLogger(ShopController.class);

    @PostMapping(path = "/send_new_item")
    public ResponseEntity<?> saveItem(@RequestBody Item item) {
        try {
            Item sentItem = shopService.sendNewItem(item);
            return ResponseEntity.ok("Product accepted: " + sentItem.getProductId());
        } catch (CensoreException exception) {
            logger.error("Censore error: {}", exception.getMessage());
            return ResponseEntity.badRequest().body("Rejected: " + exception.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Sending to kafka topic error.");
        }
    }

}
