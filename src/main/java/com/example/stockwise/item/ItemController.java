package com.example.stockwise.item;

import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/warehouses/{id}/items")
public class ItemController {
    ItemService itemservice;
    UserService userService;

    WarehouseService warehouseService;

    public ItemController(ItemService itemservice, UserService userService, WarehouseService warehouseService) {
        this.itemservice = itemservice;
        this.userService = userService;
        this.warehouseService = warehouseService;
    }


    @GetMapping()
    public String getAllWarehouseItemsSorted(@PathVariable Long id,
                                             Model model,
                                             @RequestParam(name = "sort", required = false) String sort)
            throws Exception {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        model.addAttribute(warehouse);
        model.addAttribute(itemservice.getAllWarehouseItemsAndSort(id, sort));
        model.addAttribute("sort", sort);
        return "warehouse/items";
    }

}
