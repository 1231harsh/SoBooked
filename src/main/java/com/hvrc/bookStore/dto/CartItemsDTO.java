package com.hvrc.bookStore.dto;

import lombok.Data;

@Data
public class CartItemsDTO{

    private Long id;
    private Long bookId;
    private String bookName;
    private String author;
    private Double price;
    private Long quantity;
}
