package com.example.stockwise.rack;

import com.example.stockwise.graph.Vertex;
import com.example.stockwise.items.item.Item;
import com.example.stockwise.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public Rack(Long number) {
        this.number = number;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long number;
    private Long x;
    private Long y;
    private String direction;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vertex_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private Vertex vertex;


    @OneToMany(mappedBy = "rack",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Item> items;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;


}
