package com.hvrc.bookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.dto.BookResponseDTO;
import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/api/test")
    public String test() {
        return "Test";
    }

    @PostMapping("/api/add")
    public ResponseEntity<String> addBook(@RequestParam("book") String bookJson, @RequestParam("file") MultipartFile file) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Book book = objectMapper.readValue(bookJson, Book.class);
            book.setPhoto(file.getBytes());
            boolean isSaved = bookService.save(book);

            if (isSaved) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Book added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to add book.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid book JSON format.");
        }
    }


    @GetMapping("/api/getBooks")
    public ResponseEntity<List<BookDTO>> getBooks() {
        List<BookDTO> books = bookService.getBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/api/getBook/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);

        if (book == null) {  // Handle null case properly
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        BookDTO bookDTO = new BookDTO(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getCategory(),
                book.getRentalPrice(),
                book.getBuyPrice(),
                book.getCity(),
                book.getPhoto(),
                book.getPhoneNumber(),
                book.isAvailableForRent()
        );

        List<Book> similarBooks = bookService.getBooksByCategory(book.getCategory(), id);

        List<BookDTO> similarBooksDTO = similarBooks.stream().map(b -> new BookDTO(
                b.getId(),
                b.getName(),
                b.getAuthor(),
                b.getDescription(),
                b.getCategory(),
                b.getRentalPrice(),
                b.getBuyPrice(),
                b.getCity(),
                b.getPhoto(),
                b.getPhoneNumber(),
                b.isAvailableForRent()
        )).toList();

        BookResponseDTO response = new BookResponseDTO(bookDTO, similarBooksDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
