package com.wesleybertipaglia.products.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class ProductResponseDto extends RepresentationModel<ProductResponseDto> {

    private UUID id;
    private String name;
    private String description;
    private double price;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
