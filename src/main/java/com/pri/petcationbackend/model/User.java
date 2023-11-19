package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue
    @Column(name = "User_id")
    private Long userId;

    @Column(name = "First_name")
    private String firstName;

    @Column(name = "Last_name")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password", length = 60)
    private String password;

    @Column(name = "Enabled")
    private boolean enabled;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_role_id"))
    private Set<Role> roles;

    public User() {
        super();
        this.enabled = false;
    }

    @ManyToOne
    @JoinColumn(name="Address_id", nullable=false)
    private Address address;
}
