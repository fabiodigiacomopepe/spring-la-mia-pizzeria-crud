package org.lessons.java.springlamiapizzeriacrud.repository;

import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Interfaccia che estende la JPA REPOSITORY con in ingresso il Model e il tipo di dato dell'ID
public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
    // Query SQL automatica in base a nome del metodo
    // Con @Query si possono fare anche personalizzate
    List<Pizza> findByNameContainingIgnoreCase(String nameKeyword);
}

