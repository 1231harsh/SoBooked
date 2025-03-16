package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.UserBookActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserBookActivityRepository extends JpaRepository<UserBookActivity, Long> {

    void deleteByBookId(Long bookId);
}
