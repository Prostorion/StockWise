package com.example.stockwise.graph;

import com.example.stockwise.rack.Rack;
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
public class Vertex {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Double x;
    private Double y;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToOne(mappedBy = "vertex")
    private Rack rack;

    @OneToMany(mappedBy = "firstVertex",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Edge> edgesStart;

    @OneToMany(mappedBy = "secondVertex",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Edge> edgesEnd;

    @Column(nullable = true)
    private String start;
}
