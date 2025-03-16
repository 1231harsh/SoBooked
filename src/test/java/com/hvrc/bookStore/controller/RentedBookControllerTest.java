package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.RentedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.RentedBookService;
import com.hvrc.bookStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RentedBookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RentedBookService rentedBookService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RentedBookController rentedBookController;

    @Mock
    private Principal principal;

    private User user;
    private RentedBook rentedBook1;
    private RentedBook rentedBook2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rentedBookController).build();

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        rentedBook1 = new RentedBook();
        rentedBook1.setId(1L);

        rentedBook2 = new RentedBook();
        rentedBook2.setId(2L);
    }

    @Test
    void testReturnBook() throws Exception {
        when(principal.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(rentedBookService.returnBook(anyLong(), any(User.class))).thenReturn("Book returned successfully");

        mockMvc.perform(post("/rented-books/return/1")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book returned successfully"));
    }

    @Test
    void testGetMyRentedBooks() throws Exception {
        List<RentedBook> rentedBooks = Arrays.asList(rentedBook1, rentedBook2);

        when(principal.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(rentedBookService.getRentedBooksForUser(user)).thenReturn(rentedBooks);

        mockMvc.perform(get("/rented-books/my-rented-books")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}
