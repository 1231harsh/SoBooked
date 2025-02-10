package com.hvrc.bookStore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/test")
    ResponseEntity<String> test() {
        System.out.println("insdie test");
        return ResponseEntity.ok("Hello"); } // <1>
}
