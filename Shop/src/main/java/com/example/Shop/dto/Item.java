package com.example.Shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @JsonProperty("product_id")
    private String productId;
    private String name;
    private String description;
    private Price price;
    private String category;
    private String brand;
    private Stock stock;
    private String sku;
    private List<String> tags;
    private Map<String, String> specifications;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    private String index;
    @JsonProperty("store_id")
    private String storeId;
}
