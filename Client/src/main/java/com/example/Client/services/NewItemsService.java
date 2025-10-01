package com.example.Client.services;

import com.example.Client.mapper.ItemMapper;
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
    private final ItemMapper itemMapper;

    public void saveNewItem(Item item) {
        try {
            ItemEntity savedItem = itemEntityRepository.save(
                    itemMapper.dtoToEntity(item)
            );
            logger.info("Новый товар успешно сохранен в БД: {}", savedItem.toString());
        } catch (Exception ex) {
            logger.error("Saving error: {}", ex.getMessage());
        }
    }
}
