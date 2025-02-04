package com.hvrc.bookStore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    private String description;
    private String category;
    private Double buyPrice;
    private Double rentalPrice;
    private String city;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;
    private String phoneNumber;

    private boolean availableForRent;


    @OneToMany(mappedBy = "book",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CartItems> cartItems;
}
