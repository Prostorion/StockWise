package com.example.stockwise.graph;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "firstVertex_id")
    private Vertex firstVertex;

    @ManyToOne
    @JoinColumn(name = "secondVertex_id")
    private Vertex secondVertex;
}
