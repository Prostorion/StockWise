package com.example.stockwise.task;

import com.example.stockwise.items.ItemMapper;
import com.example.stockwise.items.item.Item;
import com.example.stockwise.items.item.ItemService;
import com.example.stockwise.items.itemPending.PendingItem;
import com.example.stockwise.rack.Rack;
import com.example.stockwise.rack.RackRepository;
import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WarehouseService warehouseService;
    private final UserService userService;
    private final RackRepository rackRepository;

    private final ItemService itemService;


    @Override
    public Set<Order> getAllOrdersByWarehouseId(Long id) {
        return orderRepository.findAllByWarehouseIdOrderByDeadlineAsc(id);
    }

    @Override
    public void addOrder(Order order, Long warehouseId) throws Exception {
        setWarehouse(order, warehouseId);
        setAssignee(order, warehouseId);
        setAuthor(order);
        setItems(order, warehouseId);
        setCreationDateAndDeadline(order);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId, Long warehouseId) throws Exception {
        Order task = getOrderByIdAndWarehouseId(orderId, warehouseId);

        orderRepository.delete(task);
    }

    @Override
    public Order getOrderByIdAndWarehouseId(Long taskId, Long warehouseId) throws Exception {
        Order order = orderRepository.findById(taskId).orElseThrow(() -> new Exception("Order is not found"));
        if (!order.getWarehouse().getId().equals(warehouseId)) {
            throw new Exception("Order is not found in this warehouse");
        }
        return order;
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId, Long warehouseId) throws Exception {
        Order order = getOrderByIdAndWarehouseId(orderId, warehouseId);
        Set<PendingItem> pendingItems = order.getItems();
        Set<Item> items = pendingItems.stream().map(ItemMapper.INSTANCE::toItem).collect(Collectors.toSet());
        items.forEach(i -> i.setRack(rackRepository.findRackById(i.getRack().getId()).orElse(new Rack(-1L))));
        itemService.saveItems(items);
        orderRepository.delete(order);
    }


    private static void setCreationDateAndDeadline(Order order) throws Exception {

        LocalDateTime deadline = order.getDeadline();
        if (deadline.isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new Exception("The deadline has passed");
        }
        order.setCreationDate(LocalDateTime.now());

    }


    private void setWarehouse(Order task, Long warehouse_id) throws Exception {
        Warehouse warehouse = warehouseService.getWarehouseById(warehouse_id);
        task.setWarehouse(warehouse);
    }

    private void setItems(Order task, Long warehouse_id) throws Exception {
        Set<PendingItem> items = task.getItems();
        for (PendingItem item : items) {
            Rack rack = rackRepository.findRackByNumberAndWarehouseId(
                    item.getRackNumber(),
                    warehouse_id
            ).orElseThrow(() -> new Exception("Rack is not found"));
            item.setRackId(rack.getId());
            item.setTask(task);
        }
    }

    private void setAuthor(Order task) throws Exception {
        User author = userService.getUser();
        task.setAuthor(author);
    }

    private void setAssignee(Order task, Long id) throws Exception {
        User assignee = userService.getUserByUsername(task.getAssignee().getUsername()).orElseThrow(() -> new Exception("User not found (assignee)"));
        if (!userService.getWarehouseUsers(id).contains(assignee)) {
            throw new Exception("User is not allowed to warehouse");
        }
        task.setAssignee(assignee);
    }
}
