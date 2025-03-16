package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testAddBook() {
        Book book = new Book();
        ResponseEntity<String> response = adminController.addBook(book);

        verify(bookService, times(1)).save(book);
        assertEquals("Book added successfully", response.getBody());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateBook() {
        Long bookId = 1L;
        Book updatedBook = new Book();

        ResponseEntity<String> response = adminController.updateBook(bookId, updatedBook);

        verify(bookService, times(1)).updateBook(bookId, updatedBook);
        assertEquals("Book updated successfully", response.getBody());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteBook() {
        Long bookId = 1L;

        ResponseEntity<String> response = adminController.deleteBook(bookId);

        verify(bookService, times(1)).deleteBook(bookId);
        assertEquals("Book deleted successfully", response.getBody());
    }
}
