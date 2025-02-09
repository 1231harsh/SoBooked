package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.model.UserBookActivity;
import com.hvrc.bookStore.service.UserBookActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-activity")
public class UserBookActivityController {

    @Autowired
    private UserBookActivityService userBookActivityService;

    @GetMapping("/export/user-activity")
    public List<UserBookActivity> getUserActivities() {
        return userBookActivityService.getUserActivities();
    }

}
