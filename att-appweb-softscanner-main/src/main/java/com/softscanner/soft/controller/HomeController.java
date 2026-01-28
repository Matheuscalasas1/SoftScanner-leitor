package com.softscanner.soft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.softscanner.soft.service.RegistroService;

@Controller
public class HomeController {

    private final RegistroService registroService;

    public HomeController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("registros", registroService.buscarTodos());
        
        // Adiciona estatísticas ao modelo
        model.addAttribute("totalRadios", registroService.getTotalRadiosUnicos());
        model.addAttribute("radiosOcupados", registroService.getRadiosOcupados());
        model.addAttribute("radiosDisponiveis", registroService.getRadiosDisponiveis());
        
        return "home";
    }
}