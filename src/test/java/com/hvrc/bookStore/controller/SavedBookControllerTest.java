package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.SavedBookDTO;
import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.SavedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.BookService;
import com.hvrc.bookStore.service.SavedBookService;
import com.hvrc.bookStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SavedBookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SavedBookService savedBookService;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private SavedBookController savedBookController;

    private User user;
    private Book book;
    private SavedBook savedBook;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(savedBookController).build();

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        book = new Book();
        book.setId(1L);
        book.setName("Test Book");

        savedBook = new SavedBook();
        savedBook.setId(1L);
        savedBook.setUser(user);
        savedBook.setBook(book);
        savedBook.setLiked(true);
    }

    @Test
    void testGetSavedBooksByUser() throws Exception {
        List<SavedBookDTO> savedBooks = Arrays.asList(new SavedBookDTO(savedBook));

        when(userService.findByUsername("testUser")).thenReturn(user);
        when(savedBookService.getSavedBooksByUser(user)).thenReturn(Arrays.asList(savedBook));

        mockMvc.perform(get("/saved-books")
                        .principal(() -> "testUser")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].bookId", is(1)))
                .andExpect(jsonPath("$[0].liked", is(true)));

        verify(savedBookService, times(1)).getSavedBooksByUser(user);
    }

    @Test
    void testSaveBook() throws Exception {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(savedBookService.saveBook(any(SavedBook.class))).thenReturn(savedBook);

        mockMvc.perform(post("/saved-book/save")
                        .principal(() -> "testUser")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id", is(1)))
                .andExpect(jsonPath("$.user.id", is(1)))
                .andExpect(jsonPath("$.liked", is(true)));

        verify(savedBookService, times(1)).saveBook(any(SavedBook.class));
    }

    @Test
    void testUnsaveBook() throws Exception {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(post("/saved-book/unsave")
                        .principal(() -> "testUser")
                        .param("bookId", "1"))
                .andExpect(status().isOk());

        verify(savedBookService, times(1)).unsaveBook(user, book);
    }
}
