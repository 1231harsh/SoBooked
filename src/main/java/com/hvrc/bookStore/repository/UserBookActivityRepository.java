package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.model.UserBookActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookActivityRepository extends JpaRepository<UserBookActivity, Long> {

}
