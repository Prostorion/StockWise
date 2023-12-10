package com.example.stockwise.item;

import com.example.stockwise.warehouse.WarehouseRepository;
import com.example.stockwise.warehouse.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService{

    ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Set<Item> getAllWarehouseItems(Long id) throws Exception {
        return itemRepository.findAllByWarehouseIdOrderByNameAsc(id);
    }

    @Override
    public Set<Item> getAllWarehouseItemsAndSort(Long id, String sort) throws Exception {
        if (sort == null){
            return getAllWarehouseItems(id);
        }
        if (sort.contains("asc")){
            if (sort.contains("name")){
                return itemRepository.findAllByWarehouseIdOrderByNameAsc(id);
            }
            else if (sort.contains("measurement")){
                return itemRepository.findAllByWarehouseIdOrderByMeasurementAsc(id);
            }
            else if (sort.contains("amount")){
                return itemRepository.findAllByWarehouseIdOrderByAmountAsc(id);
            }
            else if (sort.contains("rack")){
                return itemRepository.findAllByWarehouseIdOrderByRackAsc(id);
            }
        }
        else if (sort.contains("desc")){
            if (sort.contains("name")){
                return itemRepository.findAllByWarehouseIdOrderByNameDesc(id);
            }
            else if (sort.contains("measurement")){
                return itemRepository.findAllByWarehouseIdOrderByMeasurementDesc(id);
            }
            else if (sort.contains("amount")){
                return itemRepository.findAllByWarehouseIdOrderByAmountDesc(id);
            }
            else if (sort.contains("rack")){
                return itemRepository.findAllByWarehouseIdOrderByRackDesc(id);
            }
        }

        return getAllWarehouseItems(id);
    }
}
