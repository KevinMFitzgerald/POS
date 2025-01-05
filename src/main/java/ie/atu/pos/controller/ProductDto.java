package ie.atu.pos.controller;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private double price;
    private int quantity;
}
