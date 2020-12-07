package com.example.abcsearch.controllers;

import com.example.abcsearch.logic.IndexFinder;
import com.example.abcsearch.models.User;
import com.example.abcsearch.repositories.UserRepository;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    private UserRepository userRepository;
    @Autowired
    public SearchController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user,
                       Model model){
        if(user != null){
            model.addAttribute("user" , user);
        }
        return "search";
    }

    @GetMapping("/search")
    public String search(@AuthenticationPrincipal User user,
                         @RequestParam("query") String query,
                         Model model) throws IOException, ParseException {
         
        if(user != null) {
            Map<Date, String> map = user.getHistory();
            map.put(new Date(), query);
            user.setHistory(map);
            userRepository.save(user);
        }

        List<Document> list = IndexFinder.find(query);
        model.addAttribute("docs" , list);
        model.addAttribute("query" , query);
        model.addAttribute("user" , user);
        return "searchResults";
    }






}
