package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Order implements Model {
    public enum Status {
        PROCESSING,
        PREPARING,
        WAITING_FOR_DRIVER,
        PICKED_UP,
        DELIVERED,
        CANCELLED;
    }

    private ObjectId id;
    private LocalDateTime createTime;
    private Customer customer;
    private Restaurant restaurant;
    private Driver driver;
    private Status status;
    private LocalDateTime estDeliverTime;
    private LocalDateTime deliverTime = null;
    private Cart cart;
    private Address address;

    /** @return true if this order is a valid order */
    @JsonIgnore
    public boolean isValid() {
        return customer.isValid() && restaurant.isValid() && cart.isValid();
    }
}
