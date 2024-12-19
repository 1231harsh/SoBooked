package com.hvrc.bookStore.dto;

import com.hvrc.bookStore.model.Cart;
import com.hvrc.bookStore.model.CartItems;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartDTO toCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCartItems(cart.getCartItems().stream()
                .map(CartMapper::toCartItemsDTO)
                .collect(Collectors.toList()));
        cartDTO.setUser(UserMapper.toUserDTO(cart.getUser()));
        return cartDTO;
    }

    public static CartItemsDTO toCartItemsDTO(CartItems cartItems) {
        CartItemsDTO cartItemsDTO = new CartItemsDTO();
        cartItemsDTO.setId(cartItems.getId());
        cartItemsDTO.setBookId(cartItems.getBook().getId());
        cartItemsDTO.setBookName(cartItems.getBook().getName());
        cartItemsDTO.setAuthor(cartItems.getBook().getAuthor());
        cartItemsDTO.setPrice(cartItems.getBook().getPrice());
        cartItemsDTO.setQuantity(cartItems.getQuantity());
        return cartItemsDTO;
    }
}
