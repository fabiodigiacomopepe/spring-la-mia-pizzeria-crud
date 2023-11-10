package org.lessons.java.springlamiapizzeriacrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    // Faccio il redirect se l'utente atterra su "/"
    @GetMapping
    public String home() {
        return "redirect:/pizzas";
    }
}
