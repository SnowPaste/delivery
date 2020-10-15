import java.time.LocalDate;

public class CreditCard {

  private String cardNumber;
  private String firstName;
  private String lastName;
  private String securityCode;
  private LocalDate expDate;

  /**
   * Constructor of CreditCard Class
   * @param cardNumber String representing the card number of this object
   * @param firstName String representing the first name of the holder of this object
   * @param lastName String representing the last name of the holder of this object
   * @param securityCode String representing the security code of this object
   * @param expDate LocalDate representing the expiration date of this object
   */
  public CreditCard(String cardNumber, String firstName, String lastName, String securityCode,
      LocalDate expDate) {
    this.cardNumber = cardNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.securityCode = securityCode;
    this.expDate = expDate;
  }

}
