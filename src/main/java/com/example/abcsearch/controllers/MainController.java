package com.example.abcsearch.controllers;

import com.example.abcsearch.Indexer;
import com.example.abcsearch.models.User;
import com.example.abcsearch.repositories.UserRepository;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @PostMapping("/search")
    public String search(@AuthenticationPrincipal User user,
                         @RequestParam("query") String query,
                         Model model) throws IOException, ParseException {

        Map<Date, String> map = user.getHistory();
        map.put(new Date() , query);
        user.setHistory(map);
        userRepository.save(user);

        System.out.println(query);
        Set<Document> set = Indexer.find(query); // List or SortedSet
        model.addAttribute("docs" , set);
        model.addAttribute("query" , query);
        return "searchResultPage";
    }


}
