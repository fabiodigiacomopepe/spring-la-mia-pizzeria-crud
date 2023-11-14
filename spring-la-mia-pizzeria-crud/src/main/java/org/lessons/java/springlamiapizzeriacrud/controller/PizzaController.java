package org.lessons.java.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.java.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
    @Autowired
    private PizzaService pizzaService;

    // Rotta "/pizzas"
    // Parametro di ricerca è OPZIONALE perchè alla rotta si può accedere sia normalmente sia tramite ricerca
    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model) {
        // Passo il risultato al model
        model.addAttribute("pizzaList", pizzaService.getPizzaList(search));
        return "pizzas/list";
    }

    // Rotta "/pizzas/show/id <---(dinamico)"
    @GetMapping("/show/{id}")
    // Prendo l'id dal path
    public String show(@PathVariable Integer id, Model model) {
        try {
            Pizza pizza = pizzaService.getPizzaById(id);
            model.addAttribute("pizza", pizza);
            return "pizzas/show";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Rotta "/pizzas/create" (GET)
    @GetMapping("/create")
    public String createGet(Model model) {
        // Istanzio un nuovo oggetto Pizza e lo passo con il model
        model.addAttribute("pizza", new Pizza());
        return "pizzas/create_update";
    }

    // Rotta "/pizzas/create" (POST)
    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {
        // Controllo se ci sono errori
        if (bindingResult.hasErrors()) {
            // Se ci sono ricarico la pagina mantendendo i dati (grazie al model)
            return "pizzas/create_update";
        }
        // Recupero l'oggetto Pizza dal model e lo salvo in formPizza
        // Creo una nuovo oggetto Pizza chiamato savedPizza e passo i dati dal form (formPizza)
        Pizza savedPizza = pizzaService.createPizza(formPizza);
        return "redirect:/pizzas/show/" + savedPizza.getId();
    }

    // Rotta "/pizzas/edit/id <---(dinamico)" (GET)
    @GetMapping("/edit/{id}")
    public String editGet(@PathVariable Integer id, Model model) {
        try {
            // Passo la pizza con il model
            model.addAttribute("pizza", pizzaService.getPizzaById(id));
            return "/pizzas/create_update";
        } catch (PizzaNotFoundException e) {
            // Altrimenti lancio eccezione
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Rotta "/pizzas/edit/id <---(dinamico)" (POST)
    @PostMapping("/edit/{id}")
    // Parametri in ingresso:
    // @PathVariable Integer id -> per gestire quale elemento modifcare
    // @Valid -> per validazioni
    // @ModelAttribute("book") -> per ritornare stessa pizza in caso di errori
    // Pizza formPizza -> cioò che ricevo dal form
    // BindingResult bindingResult -> mappa errori
    public String editPost(
            @PathVariable Integer id,
            @Valid
            @ModelAttribute("pizza")
            Pizza formPizza,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Se ci sono errori ricarico la pagina
            return "/pizzas/create_update";
        }
        try {
            Pizza savedPizza = pizzaService.editPizza(formPizza);
            return "redirect:/pizzas/show/" + savedPizza.getId();
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Rotta "/pizzas/delete/id <---(dinamico)" (POST)
    @PostMapping("/delete/{id}")
    // Parametri in ingresso:
    // @PathVariable Integer id -> per gestire quale elemento eliminare
    // RedirectAttributes redirectAttributes -> attributi che ci sono solo nel redirect
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Provo a prendere pizza in base a id
            Pizza pizzaToDelete = pizzaService.getPizzaById(id);
            // Elimino pizza per id
            pizzaService.deletePizza(id);
            // Passo il messaggio durante il redirect
            redirectAttributes.addFlashAttribute("message", "Pizza '" + pizzaToDelete.getName() + "' eliminata correttamente!");
            return "redirect:/pizzas";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
