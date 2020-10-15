package edu.northeastern.cs5500.delivery.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Order implements Model {
    public enum Status {
        PROCESSING,
        PREPARING,
        WAITING_FOR_DRIVER,
        PICKED_UP,
        DELIVERED
    }

    private ObjectId id;
    private LocalDateTime createTime;
    private Customer customer;
    private Restaurant restaurant;
    private Driver driver;
    private Status status;
    private LocalDateTime estDeliverTime;
    private LocalDateTime deliverTime;
    private Cart cart;
    private String note;

    /** @return true if this order is a valid order */
    @JsonIgnore
    public boolean isValid() {
        return customer != null && restaurant != null && driver != null && cart != null;
    }
}
