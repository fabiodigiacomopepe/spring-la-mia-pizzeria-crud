package org.lessons.java.springlamiapizzeriacrud.controller;

import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
    // Istanzio automaticamente PizzaRepository tramite Autowired
    @Autowired
    private PizzaRepository pizzaRepository;

    // Rotta "/pizzas"
    // Parametro di ricerca è OPZIONALE perchè alla rotta si può accedere sia normalmente sia tramite ricerca
    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model) {
        // Inizializzo lista
        List<Pizza> pizzaList;
        // Se è stato passato un parametro di ricerca
        if (search.isPresent()) {
            // Lo prendo con il .GET() e lo utilizzo per farmi ritornare una lista filtrata in base al nome
            pizzaList = pizzaRepository.findByNameContainingIgnoreCase(search.get());
        } else {
            // Altrimenti ritorno lista completa
            pizzaList = pizzaRepository.findAll();
        }
        // Passo il risultato al model
        model.addAttribute("pizzaList", pizzaList);
        return "pizzas/list";
    }

    // Rotta "/pizzas/show/id <---(dinamico)"
    @GetMapping("/show/{id}")
    // Prendo l'id dal path
    public String show(@PathVariable Integer id, Model model) {
        // Passo al model l'oggetto pizza recuperato tramite repository
        // Se non lo trovo lancio una eccezione con messaggio
        model.addAttribute("pizza", pizzaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pizza with ID " + id + ": Not Found")));
        return "pizzas/show";
    }
}
