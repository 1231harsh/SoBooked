package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    Book findByName(String bookName);

    List<Book> findByCity(String city);

    List<Book> findByCityAndAvailableForRentTrue(String city);
}
