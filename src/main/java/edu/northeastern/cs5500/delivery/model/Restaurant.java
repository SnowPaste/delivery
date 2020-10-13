package edu.northeastern.cs5500.delivery.model;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Restaurant {
    private Integer ID;
    private String emailAddress;
    private String name;
    private Address address;
    private String phone;
    private LocalDateTime startDeliverTime;
    private Cuisine cuisine;
    private LinkedList<Dish> menu;
    private Double rating;


    public Restaurant(Integer id, String emailAddress, String name, Address address, String phone, LocalDateTime startDeliverTime, Cuisine cuisine, LinkedList<Dish> menu) {
        ID = id;
        this.emailAddress = emailAddress;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.startDeliverTime = startDeliverTime;
        this.cuisine = cuisine;
        this.menu = menu;
    }
}
