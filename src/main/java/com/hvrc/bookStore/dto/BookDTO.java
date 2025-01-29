package com.hvrc.bookStore.dto;

import lombok.Data;

@Data
public class BookDTO {

    private Long id;
    private String name;
    private String author;
    private String description;
    private Double price;
    private Long quantity;

    public BookDTO(Long id,String name,String author ,String description ,Double price ,Long quantity) {
    private String category;
    private Double price;
    private Long quantity;


    public BookDTO(Long id,String name,String author ,String description ,String category, Double price ,Long quantity) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }
}