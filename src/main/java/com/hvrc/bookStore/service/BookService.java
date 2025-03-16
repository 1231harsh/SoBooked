package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.RentedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.entity.UserBookActivity;
import com.hvrc.bookStore.repository.BookRepository;
import com.hvrc.bookStore.repository.UserRepository;
import com.hvrc.bookStore.smsService.SmsService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookActivityService userBookActivityService;
    private final SmsService smsService;
    private final CartItemsService cartItemsService;
    private final RentedBookService rentedBookService;

    private static final int RENTAL_PERIOD_DAYS = 14;

    public BookService(BookRepository bookRepository, UserRepository userRepository,
                       UserBookActivityService userBookActivityService, SmsService smsService,
                       CartItemsService cartItemsService, @Lazy RentedBookService rentedBookService) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userBookActivityService = userBookActivityService;
        this.smsService = smsService;
        this.cartItemsService = cartItemsService;
        this.rentedBookService = rentedBookService;
    }


    public boolean save(Book book) {
        if (book.getStatus() == null) {
            book.setStatus("AVAILABLE");
        }
        bookRepository.save(book);
        return true;
    }

    public void updateBook(Long bookId,Book book) {
        Book book1 = bookRepository.findById((bookId)).orElseThrow(() -> new RuntimeException("Book not found"));
        book1.setName(book.getName());
        book1.setAuthor(book.getAuthor());
        book1.setDescription(book.getDescription());
        book1.setCategory(book.getCategory());
        book1.setRentalPrice(book.getRentalPrice());
        book1.setBuyPrice(book.getBuyPrice());
        book1.setCity(book.getCity());
        book1.setPhoto(book.getPhoto());
        book1.setPhoneNumber(book.getPhoneNumber());
        book1.setAvailableForRent(book.isAvailableForRent());
        bookRepository.save(book1);
    }

    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }

    public List<BookDTO> getBooks() {
        List<Book> books = bookRepository.findByStatusNot("SOLD");
        return books.stream().map(book -> new BookDTO(
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
                book.isAvailableForRent())).collect(Collectors.toList());
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Transactional
    public void sellBook(Long userId, Long bookId) {
        if (bookRepository.existsById(bookId)) {
            Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
            User user = userRepository.findById(userId).orElseThrow();
//            String ownerPhoneNumber = book.getPhoneNumber();
            userBookActivityService.save(new UserBookActivity(user, book, "BUY"));
//            smsService.sendSms(ownerPhoneNumber, "Your book has been sold");
            book.setStatus("SOLD");
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Book not found");
        }
    }

    @Transactional
    public void rentBook(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(userId).orElseThrow();
        if (book.isAvailableForRent()) {
            book.setAvailableForRent(false);
            bookRepository.save(book);

            RentedBook rentedBook = new RentedBook();
            rentedBook.setUser(user);
            rentedBook.setBook(book);
            rentedBook.setRentalDate(LocalDate.now());
            rentedBook.setDueDate(LocalDate.now().plusDays(RENTAL_PERIOD_DAYS));
            rentedBook.setReturned(false);
            rentedBook.setPenalty(0);

            rentedBookService.save(rentedBook);

            userBookActivityService.save(new UserBookActivity(user, book, "RENT"));
            // smsService.sendSms(book.getPhoneNumber(), "Your book has been rented");
        } else {
            throw new RuntimeException("Book is already rented");
        }
    }

    public List<Book> getBooksByCategory(String category, Long id) {
        return bookRepository.findByCategoryAndIdNot(category, id);
    }
}
