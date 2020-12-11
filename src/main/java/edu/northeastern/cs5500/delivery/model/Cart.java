package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Cart implements Model {
    private ObjectId id;
    private ArrayList<Dish> items = new ArrayList<Dish>();
    private Double tip = 0.0;
    private Double totalPrice = 0.0;

    /** @return true if this cart is valid */
    @JsonIgnore
    public boolean isValid() {
        return true;
    }
}
