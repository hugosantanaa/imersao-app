package br.com.imersao_app.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard() {
        return "Bem-vindo ao painel administrativo!";
    }

    @GetMapping("/relatorio")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String relatorio() {
        return "Você está acessando um relatório restrito!";
    }
}