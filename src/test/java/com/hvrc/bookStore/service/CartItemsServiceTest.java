package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.Cart;
import com.hvrc.bookStore.entity.CartItems;
import com.hvrc.bookStore.repository.CartItemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemsServiceTest {

    @Mock
    private CartItemsRepository cartItemsRepository;

    @InjectMocks
    private CartItemsService cartItemsService;

    private Cart cart;
    private Book book;
    private CartItems cartItem;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        book = new Book();
        book.setId(1L);
        cartItem = new CartItems();
        cartItem.setCart(cart);
        cartItem.setBook(book);
    }

    @Test
    void testSaveCartItem() {
        when(cartItemsRepository.save(any(CartItems.class))).thenReturn(cartItem);

        cartItemsService.save(cartItem);

        verify(cartItemsRepository, times(1)).save(cartItem);
    }

    @Test
    void testGetCartItemsByCartAndBook() {
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));

        Optional<CartItems> retrievedItem = cartItemsService.getCartItemsByCartAndBook(cart, book);

        assertTrue(retrievedItem.isPresent());
        verify(cartItemsRepository, times(1)).findByCartAndBook(cart, book);
    }

    @Test
    void testDeleteCartItem() {
        doNothing().when(cartItemsRepository).delete(cartItem);

        cartItemsService.delete(cartItem);

        verify(cartItemsRepository, times(1)).delete(cartItem);
    }
}
