package com.example.stockwise.task;

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
public class TaskType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    private String type;

    @ToString.Exclude
    @OneToMany(mappedBy = "taskType",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<Task> tasks;

}
