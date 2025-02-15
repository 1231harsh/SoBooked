package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.Cart;
import com.hvrc.bookStore.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByCartAndBook(Cart cart, Book book);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItems c WHERE c.book.id = :bookId")
    void deleteByBookId(Long bookId);

}
