package com.example.backendspringboot;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {
    Optional<ShoppingItem> findByName(String name);
    boolean existsByName(String name);

    void deleteByName(String name);
}
