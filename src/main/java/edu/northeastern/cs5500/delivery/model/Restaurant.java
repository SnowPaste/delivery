package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.LinkedList;
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
    private String emailAddress;
    private String name;
    private Address address;
    private String phone;
    private LocalDateTime startDeliverTime;
    private Cuisine cuisine;
    private LinkedList<Dish> menu;
    private Double rating;

    /** @return true if this restaurant is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.emailAddress != null
                && !this.emailAddress.isEmpty()
                && this.name != null
                && !this.name.isEmpty()
                && this.address != null
                && this.phone != null
                && !this.phone.isEmpty()
                && this.menu != null
                && !this.menu.isEmpty();
    }
}
