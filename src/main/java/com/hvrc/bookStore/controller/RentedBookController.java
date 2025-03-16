package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.RentedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.RentedBookService;
import com.hvrc.bookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/rented-books", produces = "application/json")
public class RentedBookController {


    private final RentedBookService rentedBookService;
    private final UserService userService;


    public RentedBookController(RentedBookService rentedBookService, UserService userService) {
        this.userService = userService;
        this.rentedBookService = rentedBookService;
    }

    @PostMapping("/return/{rentedBookId}")
    public String returnBook(@PathVariable Long rentedBookId, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return rentedBookService.returnBook(rentedBookId, user);
    }

    @GetMapping("/my-rented-books")
    public List<RentedBook> getMyRentedBooks(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return rentedBookService.getRentedBooksForUser(user);
    }

}
