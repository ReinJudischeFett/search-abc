package com.example.abcsearch.controllers;

import com.example.abcsearch.logic.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class IndexController {
    private Indexer indexer;

    @Autowired
    public IndexController(Indexer indexer){
        this.indexer = indexer;
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @PostMapping("/index")
    public String index(@RequestParam("link") String url) throws IOException {
        indexer.indexAndParse(url);
        return "redirect:/";
    }
}
