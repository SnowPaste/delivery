package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;


import java.time.LocalDateTime;
import java.util.LinkedList;


@Data
public class Restaurant {
    public enum Cuisine {
        JAPANESE,
        CHINESE,
        VEGAN,
        KOREAN,
        ITALIAN,
        BRITISH,
        INDIAN,
        THAI,
        MEXICAN

    }

//    private Integer ID;
    private ObjectId id;
    private String emailAddress;
    private String name;
    private Address address;
    private String phone;
    private LocalDateTime startDeliverTime;
    private Cuisine cuisine;
    private LinkedList<Dish> menu;
    private Double rating;


    public Restaurant(ObjectId id, String emailAddress, String name, Address address, String phone, LocalDateTime startDeliverTime, Cuisine cuisine, LinkedList<Dish> menu) {
//        ID = id;
        this.id = id;
        this.emailAddress = emailAddress;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.startDeliverTime = startDeliverTime;
        this.cuisine = cuisine;
        this.menu = menu;
    }

    /** @return true if this restaurant is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.emailAddress != null && !this.emailAddress.isEmpty();
    }
}
