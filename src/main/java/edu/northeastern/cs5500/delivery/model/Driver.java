package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Driver implements Model {

    private boolean isAvailable = true;
    private ObjectId id;
    private String driverID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String carMaker;
    private String carModel;
    private String carLicence;
    private double rate = 5.0;
    private ArrayList<ObjectId> completeOrders = new ArrayList<ObjectId>();
    private ArrayList<ObjectId> currOrders = new ArrayList<ObjectId>();
    private int maxOrderNum = 10;

    /** @return true if this delivery is valid */
    @JsonIgnore
    public boolean isValid() {
        return firstName != null
                && !firstName.isEmpty()
                && lastName != null
                && !lastName.isEmpty()
                && phoneNumber != null
                && !phoneNumber.isEmpty()
                && carMaker != null
                && !carMaker.isEmpty()
                && carModel != null
                && !carModel.isEmpty()
                && carLicence != null
                && !carLicence.isEmpty();
    }
}
