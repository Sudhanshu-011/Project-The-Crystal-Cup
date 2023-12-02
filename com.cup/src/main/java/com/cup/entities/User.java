package com.cup.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "SELECT u FROM User u WHERE u.email = :email")
@NamedQuery(name = "User.getAllUser", query = "SELECT NEW com.cup.Wrapper.UserWrapper(u.id, u.name, u.contactNumber, u.email, u.role) FROM User u WHERE u.role = 'user'")
@NamedQuery(name = "User.updateStatus", query = "UPDATE User u SET u.status = :status WHERE u.id = :id")
@NamedQuery(name = "User.getAllAdmin", query = "SELECT u.email FROM User u WHERE u.role = 'admin' ")

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private String status;
}
