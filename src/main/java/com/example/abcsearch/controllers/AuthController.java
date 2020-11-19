package com.example.abcsearch.controllers;

import com.example.abcsearch.models.User;
import com.example.abcsearch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "auth/registration";
    }


    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user")  User userForm,
                          Model model) {

        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "User with such username is already exists");
            return "auth/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }




}
