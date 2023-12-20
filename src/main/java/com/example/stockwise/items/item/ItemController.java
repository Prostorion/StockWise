package com.example.stockwise.items.item;

import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{item_id}")
    @ResponseBody
    public ResponseEntity<?> getItem(@PathVariable Long id, @PathVariable Long item_id){
        try {
            Item item = itemservice.getItem(item_id, id);
            return new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
