package com.example.stockwise.user;

import com.example.stockwise.role.Role;
import com.example.stockwise.task.Task;
import com.example.stockwise.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;
    private String firstname;
    private String lastname;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_warehouses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "warehouse_id")
    )
    private Set<Warehouse> warehouses = new HashSet<>();

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "assignee",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<Task> assigned;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "author",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<Task> author;
}
