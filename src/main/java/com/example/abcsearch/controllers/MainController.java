package com.example.abcsearch.controllers;

import com.example.abcsearch.Indexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Set;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @PostMapping("/search")
    public String search(@RequestParam("query") String query,
                         Model model) throws IOException, ParseException {
        System.out.println(query);
        Set<Document> set = Indexer.find(query);
        model.addAttribute("docs" , set);
        model.addAttribute("query" , query);


        return "searchResultPage";
    }


}
