public class Customer {

  private String accountName;
  private String passWord;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private ArrayList<Order> orderHistory;
  private Cart cart;
  private ArrayList<Address> addressList;

  /**
   * Constructor of Customer Class
   *
   * @param accountName  String representing the account name of this object
   * @param passWord     String representing the password of this object
   * @param firstName    String representing the first name of this object
   * @param lastName     String representing the last name of this object
   * @param email        String representing the email of this object
   * @param phoneNumber  String representing the first name of this object
   * @param orderHistory ArrayList of Order objects representing the orders this object has made
   *                     before
   * @param cart         Cart representing current status of this object's cart
   * @param addressList  ArrayList of Address representing the addresses of this object
   */
  public Customer(String accountName, String passWord, String firstName, String lastName,
      String email, String phoneNumber, ArrayList<Order> orderHistory, Cart cart,
      ArrayList<Address> addressList) {
    this.accountName = accountName;
    this.passWord = passWord;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.orderHistory = orderHistory;
    this.cart = cart;
    this.addressList = addressList;
  }

}
