package com.example.stockwise.items.history;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/api/v1/warehouses/{id}/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping()
    public String warehousesSettings(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(historyService.getHistory(id));
        model.addAttribute(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return "warehouse/history";
    }
}
