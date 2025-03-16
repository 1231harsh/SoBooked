package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.*;
import com.hvrc.bookStore.repository.CartRepository;
import com.hvrc.bookStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemsService cartItemsService;
    private final BookService bookService;

    public CartService(CartRepository cartRepository, UserService userService, CartItemsService cartItemsService, BookService bookService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartItemsService = cartItemsService;
        this.bookService = bookService;
    }

    @Transactional
    public Cart getCartByUsername(String username) {
        User user = userService.findByUsername(username);
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow();
    } // findById()

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart = cartRepository.save(cart);
        cartRepository.flush();
        return cart;
    }

    public boolean addToCart(String username, Long bookId,boolean isRenting) {
        Cart cart = getCartByUsername(username);
        Book book = bookService.getBookById(bookId);

        Optional<CartItems> cartItemsOptional=cartItemsService.getCartItemsByCartAndBook(cart, book);
        if(cartItemsOptional.isPresent()){
            return false;
        }
        else {
            CartItems cartItem = new CartItems();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setRenting(isRenting);
            cartItemsService.save(cartItem);
        }
        return true;
    }

    public boolean removeFromCart(String username, Long bookId) {
        Cart cart= getCartByUsername(username);
        Book book = bookService.getBookById(bookId);

        Optional<CartItems> cartItemOptional = cartItemsService.getCartItemsByCartAndBook(cart, book);

        if (cartItemOptional.isPresent()) {
            cartItemsService.delete(cartItemOptional.get());
            return true;
        }

        return false;
    }

    public boolean removeAll(String name) {
        User user = userService.findByUsername(name);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return true;
    }

    @Transactional
    public void clearCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void deleteCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }

}
