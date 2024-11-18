package com.game_class.controllers.view;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/")
    public String authPage() {
        return "auth";
    }
}
