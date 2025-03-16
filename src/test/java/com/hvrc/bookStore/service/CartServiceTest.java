package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.Cart;
import com.hvrc.bookStore.entity.CartItems;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartItemsService cartItemsService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Cart cart;
    private Book book;
    private CartItems cartItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        book = new Book();
        book.setId(1L);

        cartItem = new CartItems();
        cartItem.setCart(cart);
        cartItem.setBook(book);
    }

    @Test
    void testGetCartByUsername_CartExists() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Cart retrievedCart = cartService.getCartByUsername("testUser");

        assertNotNull(retrievedCart);
        assertEquals(cart.getId(), retrievedCart.getId());
        verify(cartRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetCartByUsername_CartDoesNotExist() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart retrievedCart = cartService.getCartByUsername("testUser");

        assertNotNull(retrievedCart);
        verify(cartRepository, times(1)).findByUser(user);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testGetCartById() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart retrievedCart = cartService.getCartById(1L);

        assertNotNull(retrievedCart);
        assertEquals(cart.getId(), retrievedCart.getId());
        verify(cartRepository, times(1)).findById(1L);
    }

    @Test
    void testAddToCart_ItemNotPresent() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookService.getBookById(1L)).thenReturn(book);
        when(cartItemsService.getCartItemsByCartAndBook(cart, book)).thenReturn(Optional.empty());

        boolean result = cartService.addToCart("testUser", 1L, true);

        assertTrue(result);
        verify(cartItemsService, times(1)).save(any(CartItems.class));
    }

    @Test
    void testAddToCart_ItemAlreadyPresent() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookService.getBookById(1L)).thenReturn(book);
        when(cartItemsService.getCartItemsByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));

        boolean result = cartService.addToCart("testUser", 1L, true);

        assertFalse(result);
        verify(cartItemsService, never()).save(any(CartItems.class));
    }

    @Test
    void testRemoveFromCart_ItemPresent() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookService.getBookById(1L)).thenReturn(book);
        when(cartItemsService.getCartItemsByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));

        boolean result = cartService.removeFromCart("testUser", 1L);

        assertTrue(result);
        verify(cartItemsService, times(1)).delete(cartItem);
    }

    @Test
    void testRemoveFromCart_ItemNotPresent() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookService.getBookById(1L)).thenReturn(book);
        when(cartItemsService.getCartItemsByCartAndBook(cart, book)).thenReturn(Optional.empty());

        boolean result = cartService.removeFromCart("testUser", 1L);

        assertFalse(result);
        verify(cartItemsService, never()).delete(any(CartItems.class));
    }

    @Test
    void testClearCartByUserId() {
        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        cartService.clearCartByUserId(1L);

        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testDeleteCartByUserId() {
        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        cartService.deleteCartByUserId(1L);

        verify(cartRepository, times(1)).delete(cart);
    }
}
