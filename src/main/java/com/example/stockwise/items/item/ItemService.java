package com.example.stockwise.items.item;

import java.util.Set;

public interface ItemService {
    Set<Item> getAllWarehouseItems(Long id) throws Exception;

    Set<Item> getAllWarehouseItemsAndSort(Long id, String sort) throws Exception;

    void deleteItem(Item item);

    void saveItems(Set<Item> items);
}
