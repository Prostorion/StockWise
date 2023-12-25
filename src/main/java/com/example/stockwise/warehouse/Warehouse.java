package com.example.stockwise.warehouse;

import com.example.stockwise.graph.Vertex;
import com.example.stockwise.rack.Rack;
import com.example.stockwise.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;
    private String address;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "warehouses")
    private Set<User> userSet;


    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "warehouse",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Rack> racks;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "warehouse",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Vertex> vertices;

    @Column(nullable = true)
    private Long width;
    @Column(nullable = true)
    private Long height;
    @Column(nullable = true)
    private Long rackWidth;
    @Column(nullable = true)
    private Long rackHeight;

    @Column(nullable = true)
    private boolean hasState = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(id, warehouse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}