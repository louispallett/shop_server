package com.example.shop_server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Base {
    @Column(nullable = false)
    private String name;

    @Column
    private int year;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String brand;

    @Column(columnDefinition = "character varying(1000)")
    private String description;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private BigDecimal price;
}