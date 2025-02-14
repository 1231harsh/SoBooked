package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.SavedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {
    List<SavedBook> findByUserId(Long userId);
}
