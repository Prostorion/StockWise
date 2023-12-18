package com.example.stockwise.items.item;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Set<Item> getAllWarehouseItems(Long id) {
        return itemRepository.findAllByRackWarehouseIdAndTaskCompleted(id, Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Set<Item> getAllWarehouseItemsAndSort(Long id, String sort) throws Exception {
        if (sort == null || !(sort.contains("-asc") || sort.contains("-desc"))) {
            return getAllWarehouseItems(id);
        }
        String columnName = sort.split("-")[0];
        if (sort.contains("asc")) {
            return itemRepository.findAllByRackWarehouseIdAndTaskCompleted(id, Sort.by(Sort.Direction.ASC, columnName));
        } else {
            return itemRepository.findAllByRackWarehouseIdAndTaskCompleted(id, Sort.by(Sort.Direction.DESC, columnName));
        }


    }

    @Override
    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }

    @Override
    public void saveItems(Set<Item> items) {
        itemRepository.saveAll(items);
    }
}
