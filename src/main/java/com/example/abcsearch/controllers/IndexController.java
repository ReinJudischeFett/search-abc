package com.example.abcsearch.controllers;

import com.example.abcsearch.logic.Indexer;
import com.example.abcsearch.logic.JsoupParser;
import org.apache.lucene.index.IndexWriter;
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
    public String indexPage() {
        return "indexPage";
    }


    @PostMapping("/index")
    public String index(@RequestParam("link") String url) {

        new Thread(() -> {
            try {
                Indexer.index(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                JsoupParser.parseLinks(url, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return "redirect:/";
    }




}
