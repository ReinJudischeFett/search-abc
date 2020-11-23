package com.example.abcsearch.controllers;

import com.example.abcsearch.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HistoryController {
    @GetMapping("/history")
    public String history(@AuthenticationPrincipal User user,
                          Model model){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, hh:mm");
        Map<Date, String> map = user.getHistory();
        TreeMap<Date, String> map2 = new TreeMap<>(new Comparator<Date>() {  // optimize?
            @Override
            public int compare(Date o1, Date o2) {
                return o2.compareTo(o1) ;
            }
        });
        map2.putAll(map);


        model.addAttribute("dateFormat", dateFormat);
        model.addAttribute("history", map2); // map

        return "history";
    }
}
