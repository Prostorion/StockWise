package com.example.stockwise.items.history;

import java.util.Set;

public interface HistoryService {
    Set<HistoryItem> getHistory(Long id);
}
