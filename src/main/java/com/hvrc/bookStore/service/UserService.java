package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User save(User user) {
        return userRepository.save(user);
    }
}
