package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Customer implements Model{

  private ObjectId id;
  private String accountName;
  private String passWord;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private ArrayList<Order> orderHistory;
  private Cart cart;
  private ArrayList<Address> addressList;

  /** @return true if this delivery is valid */
  @JsonIgnore
  public boolean isValid() {
    return accountName != null && !accountName.isEmpty() &&
        firstName != null && !firstName.isEmpty() &&
        lastName != null && !lastName.isEmpty() &&
        passWord != null && !passWord.isEmpty() &&
        phoneNumber != null && !phoneNumber.isEmpty();
  }


}
