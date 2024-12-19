package com.hvrc.bookStore.service;

import com.hvrc.bookStore.model.User;
import com.hvrc.bookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

<<<<<<< HEAD
    public User save(User user) {
        return userRepository.save(user);
=======
    public boolean save(User user) {
        userRepository.save(user);
        return true;
>>>>>>> f00bc12 (Added new files to the project)
    }
}
