package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.RentedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.entity.UserBookActivity;
import com.hvrc.bookStore.repository.BookRepository;
import com.hvrc.bookStore.repository.UserRepository;
import com.hvrc.bookStore.smsService.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBookActivityService userBookActivityService;

    @Mock
    private SmsService smsService;

    @Mock
    private CartItemsService cartItemsService;

    @Mock
    @Lazy
    private RentedBookService rentedBookService;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setCategory("Fiction");
        book.setStatus("AVAILABLE");
        book.setAvailableForRent(true);
    }

    @Test
    void testSaveBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        boolean result = bookService.save(book);

        assertTrue(result);
        assertEquals("AVAILABLE", book.getStatus());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = new Book();
        updatedBook.setName("Updated Name");
        updatedBook.setAuthor("Updated Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.updateBook(1L, updatedBook);

        assertEquals("Updated Name", book.getName());
        assertEquals("Updated Author", book.getAuthor());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testGetBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByStatusNot("SOLD")).thenReturn(books);

        List<BookDTO> bookDTOs = bookService.getBooks();

        assertEquals(1, bookDTOs.size());
        assertEquals("Test Book", bookDTOs.get(0).getName());
        verify(bookRepository, times(1)).findByStatusNot("SOLD");
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals(1L, foundBook.getId());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testSellBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        bookService.sellBook(1L, 1L);

        assertEquals("SOLD", book.getStatus());
        verify(userBookActivityService, times(1)).save(any(UserBookActivity.class));
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testRentBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(rentedBookService).save(any(RentedBook.class));

        bookService.rentBook(1L, 1L);

        assertFalse(book.isAvailableForRent());
        verify(rentedBookService, times(1)).save(any(RentedBook.class));
        verify(userBookActivityService, times(1)).save(any(UserBookActivity.class));
        verify(bookRepository, times(1)).save(book);
    }
}
