package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Cart implements Model {
    private ObjectId id;
    private Customer customer;
    private Restaurant restaurant;
    private ArrayList<Dish> items = new ArrayList<Dish>();
    private Double tip;
    private Double totalPrice = 0.0;

    /** @return true if this cart is valid */
    @JsonIgnore
    public boolean isValid() {
        return customer.isValid() && restaurant.isValid();
    }
}
