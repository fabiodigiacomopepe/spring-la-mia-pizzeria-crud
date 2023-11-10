package org.lessons.java.springlamiapizzeriacrud.model;

import jakarta.persistence.*;

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
    private String name;
    private String description;
    private String photo;
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
