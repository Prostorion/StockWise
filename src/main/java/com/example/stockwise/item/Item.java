package com.example.stockwise.item;

import com.example.stockwise.rack.Rack;
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
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String measurement;
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "rack_id")
    private Rack rack;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

}
