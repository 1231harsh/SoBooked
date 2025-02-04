package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.CartDTO;
import com.hvrc.bookStore.dto.CartItemsDTO;
import com.hvrc.bookStore.dto.CartMapper;
import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.model.Cart;
import com.hvrc.bookStore.model.CartItems;
import com.hvrc.bookStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart/getBooks")
    public ResponseEntity<CartDTO> getCart(Principal principal) {
        Cart cart = cartService.getCartByUsername(principal.getName());
        CartDTO cartDto= CartMapper.toCartDTO(cart);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PostMapping("/cart/add")
    public ResponseEntity<String> addCart(Principal principal, @RequestParam Long bookId) {
        cartService.addToCart(principal.getName(), bookId);
        return ResponseEntity.ok("Cart Added");
    }

    @PostMapping("/cart/delete")
    public ResponseEntity<String> deleteCart(Principal principal, @RequestParam Long bookId) {
        cartService.removeFromCart(principal.getName(), bookId);
        return ResponseEntity.ok("Cart Deleted");
    }

     @DeleteMapping("/cart/deleteAll")
    public ResponseEntity<String> deleteCart(Principal principal) {
        cartService.removeAll(principal.getName());
        return ResponseEntity.ok("Cart Deleted");
    }
}
