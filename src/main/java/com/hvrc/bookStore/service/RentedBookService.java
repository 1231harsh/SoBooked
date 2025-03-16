package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.RentedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.repository.RentedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentedBookService {

    private final RentedBookRepository rentedBookRepository;

    private final BookService bookService;

    private static final double PENALTY_RATE_PER_DAY = 10.0;


    public RentedBookService(RentedBookRepository rentedBookRepository, BookService bookService) {
        this.rentedBookRepository = rentedBookRepository;
        this.bookService = bookService;
    }

    public List<RentedBook> getRentedBooksForUser(User user) {
        return rentedBookRepository.findByUserAndReturnedFalse(user);
    }

    @Transactional
    public String returnBook(Long rentedBookId, User user) {
        RentedBook rentedBook = rentedBookRepository.findById(rentedBookId)
                .orElseThrow(() -> new RuntimeException("Rented book not found"));

        if (!rentedBook.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to return this book.");
        }

        if (rentedBook.isReturned()) {
            return "Book is already returned.";
        }

        LocalDate today = LocalDate.now();
        rentedBook.setReturnDate(today);
        rentedBook.setReturned(true);

        if (today.isAfter(rentedBook.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(rentedBook.getDueDate(), today);
            double penalty = daysLate * PENALTY_RATE_PER_DAY;
            rentedBook.setPenalty(penalty);
        } else {
            rentedBook.setPenalty(0);
        }

        Book book = rentedBook.getBook();
        book.setAvailableForRent(true);
        bookService.save(book);

        rentedBookRepository.save(rentedBook);
        return "Book returned successfully. Penalty: ₹" + rentedBook.getPenalty();
    }


    public void save(RentedBook rentedBook) {
        rentedBookRepository.save(rentedBook);
    }

    @Transactional
    public void deleteRentedBook(Long bookId) {
        rentedBookRepository.deleteByBookId(bookId);
    }
}