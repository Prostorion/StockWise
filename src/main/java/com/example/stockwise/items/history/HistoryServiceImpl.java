package com.example.stockwise.items.history;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Override
    public Set<HistoryItem> getHistory(Long id) {
        return historyRepository.findAllByWarehouseIdOrderByTimeOfAdditionDesc(id);
    }
}
