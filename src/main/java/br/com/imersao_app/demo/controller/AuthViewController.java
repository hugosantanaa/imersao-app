package br.com.imersao_app.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/auth") // Rota separada para views
public class AuthViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login"; // Seu template Thymeleaf
    }
    
    @GetMapping("/register")
    public String showRegisterPage() {
        return "auth/register"; // Caminho do template Thymeleaf
    }
}
