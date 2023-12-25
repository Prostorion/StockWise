package com.example.stockwise.rack;

import com.example.stockwise.items.item.Item;
import com.example.stockwise.items.item.ItemRepository;
import com.example.stockwise.items.itemPending.PendingItem;
import com.example.stockwise.items.itemToExport.ExportItem;
import com.example.stockwise.task.order.Order;
import com.example.stockwise.task.order.OrderRepository;
import com.example.stockwise.task.supply.Supply;
import com.example.stockwise.task.supply.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RackServiceImpl implements RackService {

    private final RackRepository rackRepository;
    private final OrderRepository orderRepository;
    private final SupplyRepository supplyRepository;
    private final ItemRepository itemRepository;

    @Override
    public Rack getRack(Long id, Long rackNumber) throws Exception {
        return rackRepository.findRackByNumberAndWarehouseId(rackNumber, id).orElseThrow(() -> new Exception("rack not found"));
    }

    @Override
    public List<Rack> getOrderRacks(Long taskId) {
        Order order = orderRepository.findById(taskId).orElseThrow();
        List<Rack> racks = new ArrayList<>();
        var rackO = order.getItems()
                .stream()
                .map(PendingItem::getRackId)
                .map(rackRepository::findRackById)
                .toList();
        rackO.forEach(r -> r.ifPresent(racks::add));
        return racks;
    }

    @Override
    public List<Rack> getSupplyRacks(Long taskId, Long warehouse_id) {
        Supply supply = supplyRepository.findById(taskId).orElseThrow();
        var rackO = supply.getItems().stream()
                .map(ExportItem::getItem_id)
                .map(itemRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Item::getRack).toList();
        return new ArrayList<>(rackO);
    }

}
