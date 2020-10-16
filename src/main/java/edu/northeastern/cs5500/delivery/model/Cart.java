package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Cart implements Model {
    private ObjectId id;
    private Customer customer;
    private ArrayList<Dish> items;
    private Double tip;
    private Double totalPrice;

    /** @return true if this cart is valid */
    @JsonIgnore
    public boolean isValid() {
        return customer != null;
    }
}
