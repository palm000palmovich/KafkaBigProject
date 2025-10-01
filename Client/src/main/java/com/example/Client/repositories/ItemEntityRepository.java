package com.example.Client.repositories;

import com.example.Client.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {
}
