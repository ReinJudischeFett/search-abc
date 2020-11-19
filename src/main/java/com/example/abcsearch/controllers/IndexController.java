package com.example.abcsearch.controllers;

import com.example.abcsearch.Indexer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String indexPage() throws IOException {
        return "indexPage";
    }


    @PostMapping("/index")
    public String index(@RequestParam("link") String url) throws IOException, ParseException {
        // Indexer.linkParser(url, 1);
        System.out.println(url);
        Indexer.index(url);
        return "redirect:/";
    }




}
