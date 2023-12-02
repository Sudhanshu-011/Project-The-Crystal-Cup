package com.cup.Wrapper;

import lombok.Data;

@Data
public class ProductWrapper {

    private int id;

    private String name;

    private String description;

    private int price;

    private String status;

    private int categoryId;

    private String categoryName;

    public ProductWrapper() {}

    public ProductWrapper(int id, String name, String description, int price, String status, int categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ProductWrapper(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductWrapper(int id, String name, String description, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
