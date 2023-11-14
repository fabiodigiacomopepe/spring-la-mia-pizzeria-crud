package org.lessons.java.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Pizza savedPizza = pizzaRepository.save(formPizza);
        return "redirect:/pizzas/show/" + savedPizza.getId();
    }

    // Rotta "/pizzas/edit/id <---(dinamico)" (GET)
    @GetMapping("/edit/{id}")
    public String editGet(@PathVariable Integer id, Model model) {
        // Salvo in result in modo Optional perchè potrebbe non ritornare nulla
        Optional<Pizza> result = pizzaRepository.findById(id);
        // Se il risultato è presente
        if (result.isPresent()) {
            // Passo la pizza con il model (result.get())
            model.addAttribute("pizza", result.get());
            return "/pizzas/create_update";
        } else {
            // Altrimenti lancio eccezione
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with ID " + id + ": Not Found");
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

        // Provo a prendere pizza in base a id, in caso tiro eccezione
        Pizza pizzaToEdit = pizzaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Valorizzo con i setter i vari parametri passando quelli ricevuti dal form
        pizzaToEdit.setName(formPizza.getName());
        pizzaToEdit.setDescription(formPizza.getDescription());
        pizzaToEdit.setPhoto(formPizza.getPhoto());
        pizzaToEdit.setPrice(formPizza.getPrice());

        // Salvo la pizza
        // Metodo .save salva ciò che riceve. Se i campi nel form mancano, li lascia vuoti (non si comporta come update)
        Pizza savedPizza = pizzaRepository.save(pizzaToEdit);
        return "redirect:/pizzas/show/" + savedPizza.getId();
    }

    // Rotta "/pizzas/delete/id <---(dinamico)" (POST)
    @PostMapping("/delete/{id}")
    // Parametri in ingresso:
    // @PathVariable Integer id -> per gestire quale elemento eliminare
    // RedirectAttributes redirectAttributes -> attributi che ci sono solo nel redirect
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // Provo a prendere pizza in base a id, in caso tiro eccezione
        Pizza pizzaToDelete = pizzaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Elimino pizza per id
        pizzaRepository.deleteById(id);
        // Passo il messaggio durante il redirect
        redirectAttributes.addFlashAttribute("message", "Pizza '" + pizzaToDelete.getName() + "' eliminata correttamente!");
        return "redirect:/pizzas";
    }
}
