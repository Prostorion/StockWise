package com.example.stockwise.task;

import com.example.stockwise.item.Item;
import com.example.stockwise.user.User;
import com.example.stockwise.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private Long id;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Item> items;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "taskType_id", nullable = false)
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "assignee")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private boolean completed;
}
