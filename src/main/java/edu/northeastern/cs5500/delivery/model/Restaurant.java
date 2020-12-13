package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Restaurant implements Model {
    public enum Cuisine {
        JAPANESE,
        CHINESE,
        VEGAN,
        KOREAN,
        ITALIAN,
        BRITISH,
        INDIAN,
        THAI,
        MEXICAN,
        AMERICAN,
        FRENCH
    }

    //    private Integer ID;
    private ObjectId id;
    private String name;
    private Address address;
    private String phone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Cuisine cuisine;
    private ArrayList<Dish> menu;
    private Double rating;

    /** @return true if this restaurant is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.name != null
                && !this.name.isEmpty()
                && this.address != null
                && this.phone != null
                && !this.phone.isEmpty()
                && this.menu != null
                && !this.menu.isEmpty();
    }
}
