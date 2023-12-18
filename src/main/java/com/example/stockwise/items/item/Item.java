package com.example.stockwise.items.item;

import com.example.stockwise.rack.Rack;
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


}
