package br.com.imersao_app.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
	public class AuthPageController {

	    @GetMapping("/login")
	    public String loginPage() {
	        return "login";
	    }

	    @GetMapping("/register")
	    public String registerPage() {
	        return "register";
	    }
	}

