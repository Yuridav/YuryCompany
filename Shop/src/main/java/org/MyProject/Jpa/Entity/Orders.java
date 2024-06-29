package org.MyProject.Jpa.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    public Products products;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customers_id")
    public Customers customers;

    public Orders(){}

    public Orders(Products products, Customers customers){
        this.products = products;
        this.customers = customers;
    }

}
