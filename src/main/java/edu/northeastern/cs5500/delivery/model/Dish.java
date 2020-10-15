package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;


@Data
public class Dish {
    private ObjectId id;
    private String name;
    private String description;
    private String picture;
    private Double price;
    private Double rating;


    public Dish(ObjectId id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /** @return true if this restaurant is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.name != null && !this.name.isEmpty();
    }
}
