package com.example.Client.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", unique = true, nullable = false)
    private String productId;
    private String name;
    private String description;
    private Long amount;
    private String currency;
    private String category;
    private String brand;
    @Column(columnDefinition = "TEXT")
    private String tags; // JSON как строка
    @Column(columnDefinition = "TEXT")
    private String specifications; // JSON как строка
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
    private OrderEntity orderEntity;
}
