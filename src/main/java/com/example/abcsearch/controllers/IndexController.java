package com.example.abcsearch.controllers;

import com.example.abcsearch.Indexer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String indexPage() throws IOException {
        return "indexPage";
    }


    @PostMapping("/index")
    public String index(@RequestParam("link") String url) throws IOException, ParseException {
        System.out.println(new Date());
        Indexer.index(url);
        Indexer.parseLinks(url);
        return "redirect:/";
    }




}
