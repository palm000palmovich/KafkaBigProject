package com.example.Client.controller;

import com.example.Client.model.ItemEntity;
import com.example.Client.services.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemsService itemsService;

    @GetMapping
    public ResponseEntity<List<ItemEntity>> getAllItems() {
        return ResponseEntity.ok(itemsService.getAllItems());
    }
}
