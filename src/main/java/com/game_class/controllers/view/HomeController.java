package com.game_class.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/auth")
    public String authPage() {
        return "auth";
    
    }
    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
}
