package com.hvrc.bookStore.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class BookDTO {

    private Long id;
    private String name;
    private String author;
    private String description;
    private String category;
    private Double rentalPrice;
    private Double buyPrice;
    private String city;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;
    private String phoneNumber;



    private boolean availableForRent;

    public BookDTO(Long id, String name, String author, String description, String category, Double rentalPrice, Double buyPrice, String city, byte[] photo, String phoneNumber, boolean availableForRent) {
        {
            this.id = id;
            this.name = name;
            this.author = author;
            this.description = description;
            this.category=category;
            this.rentalPrice = rentalPrice;
            this.buyPrice = buyPrice;
            this.city = city;
            this.photo = photo;
            this.phoneNumber = phoneNumber;
            this.availableForRent = availableForRent;
        }
    }
}