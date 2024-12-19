package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.model.Cart;
import com.hvrc.bookStore.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {

    Optional<CartItems> findByCartAndBook(Cart cart, Book book);
}
