package com.example.stockwise.item;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Set<Item> getAllWarehouseItems(Long id) throws Exception {
        //TODO: sorting impl
        return itemRepository.findAllByRackWarehouseIdAndTask(id, null);
    }

    @Override
    public Set<Item> getAllWarehouseItemsAndSort(Long id, String sort) throws Exception {


        return getAllWarehouseItems(id);
    }
}
