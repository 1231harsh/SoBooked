package com.hvrc.bookStore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
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
    private Double rentalPrice;
    private Double buyPrice;
    private String city;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;
    private String phoneNumber;

    private boolean availableForRent;

    private int rentalCount;
    private int purchaseCount;

    private String status;

    @OneToMany(mappedBy = "book",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CartItems> cartItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<SavedBook> savedByUsers = new ArrayList<>();
}
