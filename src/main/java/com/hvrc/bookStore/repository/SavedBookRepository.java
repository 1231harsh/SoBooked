package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.SavedBook;
import com.hvrc.bookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {

    void deleteByUserAndBook(User user, Book book);

    List<SavedBook> findByUser(User user);
}
