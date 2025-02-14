package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.Cart;
import com.hvrc.bookStore.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {

    Optional<CartItems> findByCartAndBook(Cart cart, Book book);
}
