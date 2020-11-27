package com.example.abcsearch.controllers;

import com.example.abcsearch.logic.IndexFinder;
import com.example.abcsearch.models.User;
import com.example.abcsearch.repositories.UserRepository;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

   /** @GetMapping("/search")
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
    } **/
   @GetMapping("/search")
   public String search(@AuthenticationPrincipal User user,
                        @RequestParam("query") String query,
                        @RequestParam(required = false, name = "page") Integer pageNumber,
                        Model model) throws IOException, ParseException {

       if(user != null) {
           Map<Date, String> map = user.getHistory();
           map.put(new Date(), query);
           user.setHistory(map);
           userRepository.save(user);
       }
       Page<Document> docs = null;
       docs = IndexFinder.find(query, PageRequest.of(pageNumber == null ? 0 : pageNumber - 1, 4));

       model.addAttribute("docs" , docs);
       model.addAttribute("query" , query);
       model.addAttribute("user" , user);
       return "searchResults";
   }





}
