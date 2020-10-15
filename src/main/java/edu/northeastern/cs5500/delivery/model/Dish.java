package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Dish implements Model {
    private ObjectId id;
    private String name;
    private String description;
    private String picture;
    private Double price;
    private Double rating;

    /** @return true if this dish is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.name != null && !this.name.isEmpty() && this.price != null;
    }
}
