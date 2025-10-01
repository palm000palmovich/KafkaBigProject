package com.example.Client.mapper;

import com.example.Client.model.ItemEntity;
import com.example.Shop.dto.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public ItemEntity dtoToEntity(Item item) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setProductId(item.getProductId());
        itemEntity.setName(item.getName());
        itemEntity.setDescription(item.getDescription());
        itemEntity.setAmount(item.getPrice().getAmount());
        itemEntity.setCurrency(item.getPrice().getCurrency());
        itemEntity.setCategory(item.getCategory());
        itemEntity.setBrand(item.getBrand());
        itemEntity.setTags(item.getTags().toString());
        itemEntity.setSpecifications(item.getSpecifications().toString());
        itemEntity.setCreatedAt(item.getCreatedAt());
        itemEntity.setUpdatedAt(LocalDateTime.now());

        return itemEntity;
    }
}
