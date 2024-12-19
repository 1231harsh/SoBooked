package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.model.Cart;
import com.hvrc.bookStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

   public Optional<Cart> findByUser(User user);
}
