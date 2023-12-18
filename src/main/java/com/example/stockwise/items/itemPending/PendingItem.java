package com.example.stockwise.items.itemPending;

import com.example.stockwise.task.Task;
import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String measurement;
    private Long amount;
    private Long rackId;
    private Long rackNumber;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = true)
    private Task task;
}
