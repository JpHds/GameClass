package com.game_class.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping("/auth")
    public String authPage(@RequestParam(value = "loginError", required = false) String loginError, @RequestParam(name = "registerError", required = false) String registerError, Model model) {
        if (loginError != null) {
            model.addAttribute("loginError", "Usuário e/ou senha inválidos.");
        }

        if(registerError != null) {
            model.addAttribute("registerError", "Nome de usuário e/ou email já está em uso.");
        }
        
        return "auth";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/my")
    public String myPublishesPage() {
        return "my-publishes";
    }
}
