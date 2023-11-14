package org.lessons.java.springlamiapizzeriacrud.service;

import org.lessons.java.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {
    // Istanzio automaticamente PizzaRepository tramite Autowired
    @Autowired
    private PizzaRepository pizzaRepository;

    public List<Pizza> getPizzaList(Optional<String> search) {
        // Se è stato passato un parametro di ricerca
        if (search.isPresent()) {
            // Lo prendo con il .GET() e lo utilizzo per farmi ritornare una lista filtrata in base al nome
            return pizzaRepository.findByNameContainingIgnoreCase(search.get());
        } else {
            // Altrimenti ritorno lista completa
            return pizzaRepository.findAll();
        }
    }

    public Pizza getPizzaById(Integer id) throws PizzaNotFoundException {
        // Salvo in result in modo Optional perchè potrebbe non ritornare nulla
        Optional<Pizza> result = pizzaRepository.findById(id);
        // Se il risultato è presente
        if (result.isPresent()) {
            // Ritorno la pizza (result.get())
            return result.get();
        } else {
            // Altrimenti lancio eccezione
            throw new PizzaNotFoundException("Pizza with ID " + id + ": Not Found");
        }
    }

    public Pizza createPizza(Pizza pizza) throws RuntimeException {
        return pizzaRepository.save(pizza);
    }

    public Pizza editPizza(Pizza pizza) throws PizzaNotFoundException {
        // Provo a prendere pizza in base a id
        Pizza pizzaToEdit = getPizzaById(pizza.getId());
        // Valorizzo con i setter i vari parametri passando quelli ricevuti dal form
        pizzaToEdit.setName(pizza.getName());
        pizzaToEdit.setDescription(pizza.getDescription());
        pizzaToEdit.setPhoto(pizza.getPhoto());
        pizzaToEdit.setPrice(pizza.getPrice());
        // Salvo la pizza
        // Metodo .save salva ciò che riceve. Se i campi nel form mancano, li lascia vuoti (non si comporta come update)
        return pizzaRepository.save(pizzaToEdit);
    }

    public void deletePizza(Integer id) {
        pizzaRepository.deleteById(id);
    }
}
