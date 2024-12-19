package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.CartDTO;
<<<<<<< HEAD
import com.hvrc.bookStore.dto.CartItemsDTO;
import com.hvrc.bookStore.dto.CartMapper;
import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.model.Cart;
import com.hvrc.bookStore.model.CartItems;
=======
import com.hvrc.bookStore.dto.CartMapper;
import com.hvrc.bookStore.model.Cart;
>>>>>>> f00bc12 (Added new files to the project)
import com.hvrc.bookStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

import java.security.Principal;
import java.util.List;
=======
import java.security.Principal;
>>>>>>> f00bc12 (Added new files to the project)

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
<<<<<<< HEAD
        cartService.addToCart(principal.getName(), bookId);
        return ResponseEntity.ok("Cart Added");
    }

    @PostMapping("/cart/delete")
    public ResponseEntity<String> deleteCart(Principal principal, @RequestParam Long bookId) {
        cartService.removeFromCart(principal.getName(), bookId);
        return ResponseEntity.ok("Cart Deleted");
=======
        if(cartService.addToCart(principal.getName(), bookId)){
            return ResponseEntity.ok("Cart Added");
        }else{
            return ResponseEntity.ok("Cart Not Added");
        }
    }

    @PostMapping("/cart/updateQuantity")
    public ResponseEntity<String> updateQuantity(Principal principal, @RequestParam Long bookId, @RequestParam String operation) {
        boolean updated = cartService.updateQuantity(principal.getName(), bookId, operation);
        if (updated) {
            return ResponseEntity.ok("Quantity updated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating quantity");
        }
    }
    @DeleteMapping("/cart/removeAll")
    public ResponseEntity<String> removeAll(Principal principal) {
        boolean removed = cartService.removeAll(principal.getName());
        if (removed) {
            return ResponseEntity.ok("Cart removed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error removing cart");
        }
>>>>>>> f00bc12 (Added new files to the project)
    }
}
