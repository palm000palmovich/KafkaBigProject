package com.example.Client.services;

import com.example.Client.model.ItemEntity;
import com.example.Client.repositories.ItemEntityRepository;
import com.example.Shop.dto.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewItemsService {
    private Logger logger = LoggerFactory.getLogger(NewItemsService.class);
    private final ItemEntityRepository itemEntityRepository;
    private final NewItemsService newItemsService;

    public ItemEntity saveNewItem(Item item) {
        return null;
    }
}
