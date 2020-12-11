package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Customer implements Model {
    private ObjectId id;
    private String accountName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private ArrayList<ObjectId> orderHistory = new ArrayList<ObjectId>();
    private Cart cart = new Cart();
    private ArrayList<Address> addressList = new ArrayList<Address>();
    private ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();

    /** @return true if this delivery is valid */
    @JsonIgnore
    public boolean isValid() {
        return accountName != null
                && !accountName.isEmpty()
                && firstName != null
                && !firstName.isEmpty()
                && lastName != null
                && !lastName.isEmpty()
                && password != null
                && !password.isEmpty()
                && phoneNumber != null
                && !phoneNumber.isEmpty();
    }
}
