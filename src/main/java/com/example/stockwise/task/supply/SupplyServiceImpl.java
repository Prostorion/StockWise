package com.example.stockwise.task.supply;

import com.example.stockwise.graph.Edge;
import com.example.stockwise.items.history.HistoryItem;
import com.example.stockwise.items.history.HistoryRepository;
import com.example.stockwise.items.item.Item;
import com.example.stockwise.items.item.ItemRepository;
import com.example.stockwise.items.itemToExport.ExportItem;
import com.example.stockwise.items.itemToExport.ExportItemRepository;
import com.example.stockwise.task.TaskService;
import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;
    private final WarehouseService warehouseService;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ExportItemRepository exportItemRepository;
    private final HistoryRepository historyRepository;
    private final TaskService taskService;

    @Override
    @Transactional
    public void addSupply(Supply supply, Long id) throws Exception {
        setWarehouse(supply, id);
        setAuthor(supply);
        setAssignee(supply, id);
        setDateTime(supply);
        setItems(supply, id);
        exportItemRepository.saveAll(supply.getItems());
    }

    @Override
    public Set<Supply> getAllSuppliesByWarehouseId(Long id) {
        return supplyRepository.findAllByWarehouseIdOrderByDeadline(id);
    }

    @Override
    public List<List<Edge>> getPath(Long taskId, Long id) throws Exception {
        return taskService.getPath(taskId, id, false);
    }

    @Override
    @Transactional
    public void deleteSupply(Long taskId, Long id) throws Exception {
        Supply supply = supplyRepository.findById(taskId).orElseThrow(() -> new Exception("Supply not found"));
        if (!supply.getWarehouse().getId().equals(id)) {
            throw new Exception("Supply not found in this warehouse");
        }
        supplyRepository.delete(supply);
    }

    @Override
    @Transactional
    public void completeSupply(Long taskId, Long id) throws Exception {
        Supply supply = supplyRepository.findById(taskId).orElseThrow(() -> new Exception("Supply not found"));
        if (!supply.getWarehouse().getId().equals(id)) {
            throw new Exception("Supply not found in this warehouse");
        }
        saveHistory(supply);
        changeItemsAmount(supply);
        supplyRepository.delete(supply);
    }

    private void saveHistory(Supply supply) throws Exception {
        for (ExportItem i : supply.getItems()) {
            Item item = itemRepository.findById(i.getItem_id()).orElseThrow(() -> new Exception("item not found"));
            HistoryItem historyItem = HistoryItem.builder()
                    .username(userService.getUser().getUsername())
                    .amount(i.getAmount())
                    .type("Export")
                    .name(item.getName())
                    .measurement(item.getMeasurement())
                    .timeOfAddition(LocalDateTime.now())
                    .warehouse(supply.getWarehouse())
                    .build();
            historyRepository.save(historyItem);
        }
    }

    private void changeItemsAmount(Supply supply) throws Exception {
        Set<ExportItem> exportItems = supply.getItems();
        for (ExportItem i : exportItems) {
            Item item = itemRepository.findById(i.getItem_id()).orElseThrow(() -> new Exception("item not found"));
            item.setAmount(item.getAmount() - i.getAmount());
            if (item.getAmount() <= 0) {
                itemRepository.delete(item);
            } else {
                itemRepository.save(item);
            }
        }
    }

    private void setItems(Supply supply, Long id) throws Exception {
        Set<ExportItem> items = supply.getItems();
        for (var item : items) {
            if (items.stream().filter(i -> i.getItem_id().equals(item.getItem_id())).toList().size() > 1) {
                throw new Exception("Several identical things are present");
            }
        }
        for (ExportItem i : items) {
            Item item = itemRepository.findById(i.getItem_id()).orElseThrow(() -> new Exception("item is not found"));
            if (!item.getRack().getWarehouse().getId().equals(id)) {
                throw new Exception("warehouse id is invalid");
            }
            if (i.getAmount() > item.getAmount() || i.getAmount() < 1) {
                throw new Exception("Change amount of " + item.getName() + " to [1;  " + item.getAmount() + "]");
            }
        }
        supply = supplyRepository.save(supply);
        for (ExportItem i : items) {
            i.setSupply(supply);
        }

    }

    private static void setDateTime(Supply supply) throws Exception {
        LocalDateTime deadline = supply.getDeadline();
        if (deadline.isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new Exception("The deadline has passed");
        }
        supply.setCreationDate(LocalDateTime.now());
    }

    private void setAssignee(Supply supply, Long id) throws Exception {
        User assignee = userService.getUserByUsername(supply.getAssignee().getUsername()).orElseThrow(() -> new Exception("User not found (assignee)"));
        if (!userService.getWarehouseUsers(id).contains(assignee)) {
            throw new Exception("User is not allowed to warehouse");
        }
        supply.setAssignee(assignee);
    }

    private void setAuthor(Supply supply) throws Exception {
        supply.setAuthor(userService.getUser());
    }

    private void setWarehouse(Supply supply, Long id) throws Exception {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        supply.setWarehouse(warehouse);
    }
}
