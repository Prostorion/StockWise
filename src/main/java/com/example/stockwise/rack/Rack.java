package com.example.stockwise.rack;

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
public class Rack {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long number;


    @OneToMany(mappedBy = "rack",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<Item> items;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

}
