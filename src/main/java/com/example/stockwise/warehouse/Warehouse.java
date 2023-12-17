package com.example.stockwise.warehouse;

import com.example.stockwise.rack.Rack;
import com.example.stockwise.user.User;
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

}