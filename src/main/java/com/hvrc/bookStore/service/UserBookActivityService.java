package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.UserBookActivity;
import com.hvrc.bookStore.repository.UserBookActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
