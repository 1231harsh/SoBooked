package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

<<<<<<< HEAD
=======
    Book findByName(String bookName);
>>>>>>> f00bc12 (Added new files to the project)
}
