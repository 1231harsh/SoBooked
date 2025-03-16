package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.UserBookActivity;
import com.hvrc.bookStore.repository.UserBookActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class UserBookActivityService {

    private final UserBookActivityRepository userBookActivityRepository;

    public UserBookActivityService(UserBookActivityRepository userBookActivityRepository) {
        this.userBookActivityRepository = userBookActivityRepository;
    }

    public List<UserBookActivity> getUserActivities() {

        return userBookActivityRepository.findAll();
    }

    public void save(UserBookActivity userBookActivity) {
        userBookActivityRepository.save(userBookActivity);
    }

    @Transactional
    public void deleteUserBookActivity(Long bookId) {
        userBookActivityRepository.deleteByBookId(bookId);
    }
}
