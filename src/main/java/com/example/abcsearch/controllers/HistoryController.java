package com.example.abcsearch.controllers;

import com.example.abcsearch.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HistoryController {
    @GetMapping("/history")
    public String history(@AuthenticationPrincipal User user,
                          Model model){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, hh:mm");
        Map<Date, String> map = user.getHistory();
        TreeMap<Date, String> map2 = new TreeMap<>((o1, o2) -> o2.compareTo(o1));
        map2.putAll(map);
        model.addAttribute("dateFormat", dateFormat);
        model.addAttribute("user", user);
        model.addAttribute("history", map2);
        return "history";
    }
    @PostMapping("/history/clear")
    public String clearHistory(@AuthenticationPrincipal User user){
        user.setHistory(new HashMap<>()); //
        return "redirect:/history";
    }
}
