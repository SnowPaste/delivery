package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import lombok.Data;

@Data
public class Address implements Model {
    private ObjectId id;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;

    @JsonIgnore
    public boolean isValid() {
        return address1 != null && city != null && state != null && zip != null;
    }
}
