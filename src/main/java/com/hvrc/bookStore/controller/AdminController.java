package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/books")
public class AdminController {

    private final BookService bookService;
    private final OrderItemService orderItemService;
    private final RentedBookService rentedBookService;
    private final SavedBookService savedBookService;
    private final CartItemsService cartItemsService;
    private final UserBookActivityService userBookActivityService;

    public AdminController(BookService bookService, OrderItemService orderItemService, RentedBookService rentedBookService, SavedBookService savedBookService, CartItemsService cartItemsService, UserBookActivityService userBookActivityService) {
        this.bookService = bookService;
        this.orderItemService = orderItemService;
        this.rentedBookService = rentedBookService;
        this.savedBookService = savedBookService;
        this.cartItemsService = cartItemsService;
        this.userBookActivityService = userBookActivityService;
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        bookService.save(book);
        return ResponseEntity.ok("Book added successfully");
    }

    @PutMapping("/updateBook/{bookId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateBook(@PathVariable Long bookId, @RequestBody Book updatedBook) {
        bookService.updateBook(bookId, updatedBook);
        return ResponseEntity.ok("Book updated successfully");
    }

    @DeleteMapping("/deleteBook/{bookId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        orderItemService.deleteOrderItem(bookId);
        rentedBookService.deleteRentedBook(bookId);
        savedBookService.deleteSavedBook(bookId);
        cartItemsService.deleteCartItems(bookId);
        userBookActivityService.deleteUserBookActivity(bookId);

        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
