package com.hvrc.bookStore.service;

import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.model.Cart;
import com.hvrc.bookStore.model.CartItems;
import com.hvrc.bookStore.repository.CartItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemsService {

    @Autowired
    private CartItemsRepository cartItemsRepository;

    public void save(CartItems cartItem) {
        cartItemsRepository.save(cartItem);
    }

    public Optional<CartItems> getCartItemsByCartAndBook(Cart cart, Book book) {
        return cartItemsRepository.findByCartAndBook(cart, book);
    }
}
