package com.cup.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

import java.io.Serial;
import java.io.Serializable;

@NamedQuery(name = "Bill.getAllBills", query = "SELECT b FROM Bill b ORDER BY b.id DESC")
@NamedQuery(name = "Bill.getBillByUserName", query = "SELECT b FROM Bill b WHERE b.createdBy = :username ORDER BY b.id DESC")

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill")
public class Bill implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "contactnumber")
    private String contactNumber;

    @Column(name = "paymentmethod")
    private String paymentMethod;

    @Column(name = "total")
    private int total;

    @Column(name = "productdetail", columnDefinition = "json")
    private String productDetail;

    @Column(name = "createdby")
    private String createdBy;
}
