package org.lessons.java.springlamiapizzeriacrud.controller;

import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
    // Attributi
    @Autowired
    private PizzaRepository pizzaRepository;

    // Metodo che mostra la lista di tutte le pizze
    @GetMapping
    public String index(Model model) {
        // pizzaRepository recupera da database la lista di tutte le pizze
        List<Pizza> pizzaList = pizzaRepository.findAll();
        // Passo al template la lista delle pizze
        model.addAttribute("pizzaList", pizzaList);
        return "pizzas/list";
    }
}
