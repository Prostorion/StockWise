package com.example.stockwise.items.itemToExport;

import com.example.stockwise.task.supply.Supply;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long amount;

    private Long item_id;

    @ManyToOne
    @JoinColumn(name = "supply_id", nullable = true)
    @JsonBackReference
    private Supply supply;
}