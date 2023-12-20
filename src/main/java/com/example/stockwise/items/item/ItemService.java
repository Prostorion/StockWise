package com.example.stockwise.items.item;

import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

public interface ItemService {
    Set<Item> getAllWarehouseItems(Long id) throws Exception;

    Set<Item> getAllWarehouseItemsAndSort(Long id, String sort) throws Exception;

    void deleteItem(Item item);

    void saveItems(Set<Item> items);

    Item getItem(Long item_id, Long warehouse_id) throws Exception;
}
