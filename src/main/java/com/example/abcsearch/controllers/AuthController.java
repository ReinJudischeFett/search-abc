package com.example.abcsearch.controllers;

import com.example.abcsearch.models.User;
import com.example.abcsearch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

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
    public String addUser(@ModelAttribute("user") @Valid User userForm,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam("passwordConfirm" )String password) {

        if(bindingResult.hasErrors()){
            return "auth/registration";
        }

        if(!userForm.getPassword().equals(password)){
            model.addAttribute("passwordError", "Password doesn't match password confirmation");
            return "auth/registration";
        }

        if (!userService.saveUser(userForm)){
            model.addAttribute("emailError", "Email already used");
            return "auth/registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model){
        if(userService.loadUserByUsername(username) == null){
            model.addAttribute("nohUserError", "Wrong email or password");
            return "/login";
        }
        return "redirect:/";
    }




}
