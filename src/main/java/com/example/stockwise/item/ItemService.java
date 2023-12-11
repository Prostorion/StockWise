package com.example.stockwise.item;

import java.util.Set;

public interface ItemService {
    Set<Item> getAllWarehouseItems(Long id) throws Exception;

    Set<Item> getAllWarehouseItemsAndSort(Long id, String sort) throws Exception;
}
