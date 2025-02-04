package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.kafka.BookEventProducer;
import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookEventProducer bookEventProducer;


    public boolean save(Book book) {
        Book savedBook = bookRepository.save(book);

        String eventMessage = "New book added: " + book.getName() + " by " + book.getAuthor();
        bookEventProducer.sendBookAddedEvent(eventMessage);

        return true;
    }

    public List<BookDTO> getBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOS = books.stream().map(book -> new BookDTO(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getCategory(),
                book.getBuyPrice(),
                book.getRentalPrice(),
                book.getCity(),
                book.getPhoto(),
                book.getPhoneNumber(),
                book.isAvailableForRent())).collect(Collectors.toList());
        return bookDTOS;
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Book> getBooksByCity(String city) {
        return bookRepository.findByCity(city);
    }

    public List<Book> getBooksForRentByCity(String city) {
        return bookRepository.findByCityAndAvailableForRentTrue(city);
    }

    public void sellBook(Long bookId) {
        if (bookRepository.existsById(bookId)) {
            Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
            String ownerPhoneNumber = book.getPhoneNumber();
            bookRepository.deleteById(bookId);
            String eventMessage = "SELL," + book.getName() + "," + ownerPhoneNumber;
            bookEventProducer.sendBookInventoryEvent(eventMessage);
        } else {
            throw new RuntimeException("Book not found");
        }
    }

    public void rentBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.isAvailableForRent()) {
            String ownerPhoneNumber = book.getPhoneNumber();
            book.setAvailableForRent(false);
            bookRepository.save(book);
            String eventMessage = "RENT," + book.getName() + "," + ownerPhoneNumber;
            bookEventProducer.sendBookInventoryEvent(eventMessage);
        } else {
            throw new RuntimeException("Book is already rented");
        }
    }
}
