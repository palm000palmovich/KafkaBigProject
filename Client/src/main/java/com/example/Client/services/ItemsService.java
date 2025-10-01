package com.example.Client.services;

import com.example.Client.model.ItemEntity;
import com.example.Client.repositories.ItemEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsService {
    private final ItemEntityRepository itemEntityRepository;

    public List<ItemEntity> getAllItems() {
        return itemEntityRepository.findAll();
    }
}
