package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class CreditCard implements Model {

    private ObjectId id;
    private String cardNumber;
    private String firstName;
    private String lastName;
    private String securityCode;
    private String expDate;
    private Address billingAddress;

    /** @return true if this delivery is valid */
    @JsonIgnore
    public boolean isValid() {
        return cardNumber != null
                && !cardNumber.isEmpty()
                && firstName != null
                && !firstName.isEmpty()
                && lastName != null
                && !lastName.isEmpty()
                && securityCode != null
                && !securityCode.isEmpty()
                && expDate != null
                && billingAddress != null;
    }
}
