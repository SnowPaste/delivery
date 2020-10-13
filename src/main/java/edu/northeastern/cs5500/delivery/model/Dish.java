package edu.northeastern.cs5500.delivery.model;

public class Dish {
    private String name;
    private String description;
    private String picture;
    private Double price;
    private Double rating;


    public Dish(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
