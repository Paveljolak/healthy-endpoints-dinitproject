package com.pavel.dinit.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "userstb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @Column()
    private String username;

    @Column()
    private String password;

    @Column()
    private String role; // ADMIN,USER - for admins.

    @OneToMany(mappedBy = "addedByUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Url> urls;

}
