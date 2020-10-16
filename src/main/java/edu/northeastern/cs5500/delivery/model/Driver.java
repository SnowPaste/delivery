package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Driver implements Model {

  private ObjectId id;
  private String driverID;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String carMaker;
  private String carModel;
  private String carLicence;
  private double rate;

  /** @return true if this delivery is valid */
  @JsonIgnore
  public boolean isValid() {
    return driverID != null && !driverID.isEmpty() &&
        firstName != null && !firstName.isEmpty() &&
        lastName != null && !lastName.isEmpty();
  }


}
