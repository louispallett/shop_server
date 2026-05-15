package com.example.shop_server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends Base {
    @Column(unique = true, nullable = false)
    private String name;
}