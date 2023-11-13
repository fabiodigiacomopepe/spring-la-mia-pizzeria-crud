package org.lessons.java.springlamiapizzeriacrud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// Entity indica che la classe è una tabella
// Table serve per indicare il nome della tabella se è diverso da nome della classe
@Entity
@Table(name = "pizzas")
public class Pizza {
    // Attributi (in questo caso nomi delle colonne)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Per auto-increment
    private Integer id;
    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(max = 255, message = "Il nome non può essere più lungo di 255 caratteri")
    private String name;
    @NotBlank(message = "La descrizione non può essere vuota")
    @Size(max = 255, message = "La descrizione non può essere più lunga di 255 caratteri")
    private String description;
    @NotBlank(message = "La foto non può essere vuota")
    @Size(max = 255, message = "La foto non può essere più lunga di 255 caratteri")
    private String photo;
    @NotNull(message = "Il prezzo non può essere vuoto")
    @DecimalMin(value = "0.01", message = "Il prezzo non può essere uguale/inferiore zero")
    private BigDecimal price;

    // GETTER E SETTER (FONDAMENTALI)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
