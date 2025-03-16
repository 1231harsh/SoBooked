package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.SavedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.repository.SavedBookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedBookService {

    private final SavedBookRepository savedBookRepository;

    public SavedBookService(SavedBookRepository savedBookRepository) {
        this.savedBookRepository = savedBookRepository;
    }

    public List<SavedBook> getSavedBooksByUser(User user) {
        return savedBookRepository.findByUser(user);
    }

    public SavedBook saveBook(SavedBook savedBook) {
        return savedBookRepository.save(savedBook);
    }

    @Transactional
    public void unsaveBook(User user, Book book) {
        savedBookRepository.deleteByUserAndBook(user, book);
    }

    @Transactional
    public void deleteSavedBook(Long bookId) {
        savedBookRepository.deleteByBookId(bookId);
    }
}
