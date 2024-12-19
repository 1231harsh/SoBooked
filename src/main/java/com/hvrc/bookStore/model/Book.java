package com.hvrc.bookStore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

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
    private String publisher;
    private String publishMonth;
    private String publishYear;
    private Double price;
    private Long quantity;

    @OneToMany(mappedBy = "book",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CartItems> cartItems;
}
