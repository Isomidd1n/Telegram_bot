package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {
    private Integer id;
    private Integer categoryId;
    private String name;
    private Double price;
    private String image;
    private String description;
    private boolean deleted = false;
    private boolean activated = false;
    private Integer quantity;

    private static int generalId = 0;

    public Product(Integer id, Integer categoryId, String name, Double price, String description,Integer quantity, String image, boolean deleted) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.deleted = deleted;
        this.quantity = quantity;

    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product() {
    }
}
