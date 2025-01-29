package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.jwt.JwtUtil;
import com.hvrc.bookStore.model.User;
import com.hvrc.bookStore.security.MyUserdetailService;
import com.hvrc.bookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserdetailService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
        @PostMapping("/signup")
        public ResponseEntity<String> signup(@RequestBody User user) {
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRole("USER");
                userService.save(user);
                return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestBody User user) {
//            System.out.println(user.getUsername());
//            System.out.println(user.getPassword());
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                String jwt=jwtUtil.generateToken(user.getUsername());
//                System.out.println(jwt);
                return new ResponseEntity<>(jwt, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
}
