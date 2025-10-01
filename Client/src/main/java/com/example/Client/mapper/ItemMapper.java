package com.example.Client.mapper;

import com.example.Client.model.ItemEntity;
import com.example.Shop.dto.Item;
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

        if (item.getTags() != null) {
            itemEntity.setTags(item.getTags().toString());
        } else {
            itemEntity.setTags("");
        }
        if (item.getSpecifications() != null) {
            itemEntity.setSpecifications(item.getSpecifications().toString());
        } else {
            itemEntity.setSpecifications("");
        }

        itemEntity.setCreatedAt(item.getCreatedAt());
        itemEntity.setUpdatedAt(LocalDateTime.now());

        return itemEntity;
    }
}
