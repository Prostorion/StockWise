package com.example.stockwise.task;

import com.example.stockwise.item.Item;
import com.example.stockwise.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Item> items;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "taskType_id", nullable = false)
    TaskType taskType;
}
