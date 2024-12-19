package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public boolean save(Book book) {
        bookRepository.save(book);
        return true;
    }

    public List<BookDTO> getBooks() {
        List<Book> books= bookRepository.findAll();
<<<<<<< HEAD
        List<BookDTO> bookDTOS = books.stream().map(book -> new BookDTO(
=======
        List<BookDTO> bookDTOS;
        bookDTOS = books.stream().map(book -> new BookDTO(
>>>>>>> f00bc12 (Added new files to the project)
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
<<<<<<< HEAD
=======
                book.getCategory(),
>>>>>>> f00bc12 (Added new files to the project)
                book.getPrice(),
                book.getQuantity())).collect(Collectors.toList());
        return bookDTOS;
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public void updateBook(Long bookId, Long quantity) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            Long currentQuantity = book.getQuantity();
            Long updatedQuantity = currentQuantity - quantity;

            if (updatedQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }

            book.setQuantity(updatedQuantity);
            bookRepository.save(book);
        }else{
            throw new RuntimeException("Book not found");
        }
    }

<<<<<<< HEAD
=======
    public BookDTO getBook(String bookName) {
        Book book = bookRepository.findByName(bookName);
        return new BookDTO(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getCategory(),
                book.getPrice(),
                book.getQuantity());
    }
>>>>>>> f00bc12 (Added new files to the project)
}
