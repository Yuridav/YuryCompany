package org.MyProject.Jpa.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "productname")
    public String productname;

    @Column(name="price")
    public double price;

    @Transient
    @OneToMany(mappedBy = "products")
    public List<Orders> orders;

    public Products(){}


}
