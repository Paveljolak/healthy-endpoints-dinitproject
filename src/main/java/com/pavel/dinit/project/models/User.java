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
    @Column(name="id")
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="email")
    private String email;

    @Column(name="role")
    private String role; // ADMIN,USER - for admins.

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="verification_code")
    private String verificationCode;

    @OneToMany(mappedBy = "addedByUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Url> urls;

}
