package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.RentedBook;
import com.hvrc.bookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RentedBookRepository extends JpaRepository<RentedBook, Long> {
    List<RentedBook> findByUserAndReturnedFalse(User user);
}