package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.CartDTO;
import com.hvrc.bookStore.dto.CartMapper;
import com.hvrc.bookStore.entity.Cart;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");
    }

    @Test
    void testGetCart() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setUser(mockUser);

        CartDTO mockCartDto = CartMapper.toCartDTO(mockCart);

        when(cartService.getCartByUsername("testUser")).thenReturn(mockCart);

        ResponseEntity<CartDTO> response = cartController.getCart(mockPrincipal);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCartDto, response.getBody());
    }

    @Test
    void testAddCart() {
        Long bookId = 1L;
        boolean isRenting = true;

        when(cartService.addToCart("testUser", bookId, isRenting)).thenReturn(true);

        ResponseEntity<Boolean> response = cartController.addCart(mockPrincipal, bookId, isRenting);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testDeleteCart() {
        Long bookId = 1L;

        ResponseEntity<String> response = cartController.deleteCart(mockPrincipal, bookId);

        verify(cartService, times(1)).removeFromCart("testUser", bookId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cart Deleted", response.getBody());
    }

    @Test
    void testDeleteAllCart() {
        ResponseEntity<String> response = cartController.deleteCart(mockPrincipal);

        verify(cartService, times(1)).removeAll("testUser");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cart Deleted", response.getBody());
    }
}
