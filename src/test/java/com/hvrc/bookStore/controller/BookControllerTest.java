package com.hvrc.bookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.dto.BookResponseDTO;
import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddBook_Success() throws Exception {
        // Create a book object
        Book book = new Book();
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setDescription("A test book description.");
        book.setCategory("Fiction");
        book.setRentalPrice(100.0);
        book.setBuyPrice(500.0);
        book.setCity("Mumbai");
        book.setPhoneNumber("1234567890");
        book.setAvailableForRent(true);

        String bookJson = objectMapper.writeValueAsString(book);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});

        when(bookService.save(any(Book.class))).thenReturn(true);

        ResponseEntity<String> response = bookController.addBook(bookJson, file);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Book added successfully.", response.getBody());

        verify(bookService, times(1)).save(any(Book.class));
    }

    @Test
    void testAddBook_Failure() throws Exception {
        Book book = new Book();
        book.setName("Test Book");

        String bookJson = objectMapper.writeValueAsString(book);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});

        when(bookService.save(any(Book.class))).thenReturn(false);

        ResponseEntity<String> response = bookController.addBook(bookJson, file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to add book.", response.getBody());

        verify(bookService, times(1)).save(any(Book.class));
    }

    @Test
    void testGetBooks_Success() {
        List<BookDTO> bookList = new ArrayList<>();
        bookList.add(new BookDTO(1L, "Book 1", "Author 1", "Desc 1", "Category 1", 100.0, 500.0, "City 1", new byte[]{}, "1234567890", true));
        bookList.add(new BookDTO(2L, "Book 2", "Author 2", "Desc 2", "Category 2", 150.0, 600.0, "City 2", new byte[]{}, "0987654321", false));

        when(bookService.getBooks()).thenReturn(bookList);

        ResponseEntity<List<BookDTO>> response = bookController.getBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Book 1", response.getBody().get(0).getName());

        verify(bookService, times(1)).getBooks();
    }

    @Test
    void testGetBookById_Success() {
        Book book = new Book();
        book.setId(1L);
        book.setName("Book 1");
        book.setAuthor("Author 1");
        book.setDescription("Desc 1");
        book.setCategory("Category 1");
        book.setRentalPrice(100.0);
        book.setBuyPrice(500.0);
        book.setCity("City 1");
        book.setPhoneNumber("1234567890");
        book.setAvailableForRent(true);

        when(bookService.getBookById(1L)).thenReturn(book);
        when(bookService.getBooksByCategory("Category 1", 1L)).thenReturn(new ArrayList<>());

        ResponseEntity<BookResponseDTO> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Book 1", response.getBody().getBook().getName());

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookService.getBookById(999L)).thenReturn(null);

        ResponseEntity<BookResponseDTO> response = bookController.getBookById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
