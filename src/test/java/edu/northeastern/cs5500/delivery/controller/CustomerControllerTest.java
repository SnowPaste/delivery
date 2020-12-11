package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;
import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerControllerTest {

  private InMemoryRepository<Customer> inMemoryRepository = new InMemoryRepository<>();
  private ObjectId objectId = ObjectId.get();
  private String accountName = "JaneDoe";
  private String passWord = "abcd123***";
  private String firstName = "Jane";
  private String lastName = "Doe";
  private String email = "janedoe20@gmail.com";
  private String phoneNumber = "2060001234";
  CustomerController customerController = new CustomerController(inMemoryRepository);


  @BeforeEach
  public void setUp() {
    Customer customer = new Customer();
    customer.setId(objectId);
    customer.setAccountName(accountName);
    customer.setPassword(passWord);
    customer.setFirstName(firstName);
    customer.setLastName(lastName);
    customer.setEmail(email);
    customer.setPhoneNumber(phoneNumber);
    inMemoryRepository.add(customer);
  }


  @Test
  void testCanGetCustomer() {
    assertNotNull(customerController.getCustomer(objectId));
  }

  @Test
  void testCanGetCustomers() {
    assertNotEquals(0, customerController.getCustomers().size());
  }

  @Test
  void testCanAddCustomerThrowInvalidCustomer() throws Exception {
    Customer customer1 = new Customer();
    assertThrows(Exception.class, () -> {
          customerController.addCustomer(customer1);
        },
        "InvalidCustomerException");
  }

  @Test
  void testCanAddCustomer() throws Exception {
    Customer customer2 = new Customer();
    ObjectId objectId2 = ObjectId.get();
    String accountName2 = "AnnLee";
    String firstName2 = "Ann";
    String lastName2 = "Lee";
    String passWord2 = "111111";
    String phoneNumber2 = "2065554321";
    customer2.setId(objectId2);
    customer2.setAccountName(accountName2);
    customer2.setFirstName(firstName2);
    customer2.setLastName(lastName2);
    customer2.setPassword(passWord2);
    customer2.setPhoneNumber(phoneNumber2);
    customerController.addCustomer(customer2);
    assertTrue(customerController.getCustomers().contains(customer2));
  }

  @Test
  void testCanAddCustomerThrowAlreadyExists() throws Exception {
    Customer customer2 = new Customer();
    ObjectId objectId2 = ObjectId.get();
    String accountName2 = "AnnLee";
    String firstName2 = "Ann";
    String lastName2 = "Lee";
    String passWord2 = "111111";
    String phoneNumber2 = "2065554321";
    customer2.setId(objectId2);
    customer2.setAccountName(accountName2);
    customer2.setFirstName(firstName2);
    customer2.setLastName(lastName2);
    customer2.setPassword(passWord2);
    customer2.setPhoneNumber(phoneNumber2);
    customerController.addCustomer(customer2);
    assertThrows(Exception.class, () -> {
          customerController.addCustomer(customer2);
        },
        "Customer already exists in database");

  }

  @Test
  void testCanUpdateCustomer() throws Exception {
    Customer customer = customerController.getCustomer(objectId);
    String newAccountName = "JaneDoe0101";
    customer.setAccountName(newAccountName);
    customerController.updateCustomer(customer);
    assertEquals(customerController.getCustomer(objectId).getAccountName(), newAccountName);
  }

  //
  @Test
  void testCanDeleteCustomer() throws Exception {
    Customer customer2 = new Customer();
    ObjectId objectId2 = ObjectId.get();
    String accountName2 = "AnnLee";
    String firstName2 = "Ann";
    String lastName2 = "Lee";
    String passWord2 = "111111";
    String phoneNumber2 = "2065554321";
    customer2.setId(objectId2);
    customer2.setAccountName(accountName2);
    customer2.setFirstName(firstName2);
    customer2.setLastName(lastName2);
    customer2.setPassword(passWord2);
    customer2.setPhoneNumber(phoneNumber2);
    customerController.addCustomer(customer2);
    customerController.deleteCustomer(objectId2);
    assertFalse(customerController.getCustomers().contains(customer2));
    assertEquals(customerController.getCustomers().size(), 1);

  }

  @Test
  void testCanAddAddress() throws Exception {
    Customer customer = customerController.getCustomer(objectId);
    Address address = new Address();
    ObjectId addressObjectId = ObjectId.get();
    String address1 = "4501 8th Ave";
    String address2 = "Apt 603";
    String city = "Seattle";
    String state = "WA";
    String zip = "98001";
    address.setId(addressObjectId);
    address.setAddress1(address1);
    address.setAddress2(address2);
    address.setCity(city);
    address.setState(state);
    address.setZip(zip);
    customerController.addAddress(customer, address);
    assertTrue(customerController.getCustomer(customer.getId()).getAddressList().contains(address));
  }

  @Test
  void testCanUpdateAddress() throws Exception {
    Customer customer = customerController.getCustomer(objectId);
    Address address = new Address();
    ObjectId addressObjectId = ObjectId.get();
    String address1 = "4501 8th Ave";
    String address2 = "Apt 603";
    String city = "Seattle";
    String state = "WA";
    String zip = "98001";
    address.setId(addressObjectId);
    address.setAddress1(address1);
    address.setAddress2(address2);
    address.setCity(city);
    address.setState(state);
    address.setZip(zip);
    customerController.addAddress(customer, address);
    address.setZip("98106");
    customerController.updateAddress(customer, address);
    String after =
        customerController
            .getCustomer(objectId)
            .getAddressList()
            .get(0)
            .getZip();
    assertEquals("98106", after);
  }

  //
  @Test
  void testCanDeleteAddress() throws Exception {
    Customer customer = customerController.getCustomer(objectId);
    Address address = new Address();
    ObjectId addressObjectId = ObjectId.get();
    String address1 = "4501 8th Ave";
    String address2 = "Apt 603";
    String city = "Seattle";
    String state = "WA";
    String zip = "98001";
    address.setId(addressObjectId);
    address.setAddress1(address1);
    address.setAddress2(address2);
    address.setCity(city);
    address.setState(state);
    address.setZip(zip);
    customerController.addAddress(customer, address);
    customerController.deleteAddress(customer, address);
    assertTrue(customerController.getCustomer(objectId).getAddressList().size() == 0);
  }

  @Test
  void testCanAddCard() throws Exception {
    Customer customer = customerController.getCustomer(objectId);
    CreditCard creditCard = new CreditCard();
    ObjectId creditCardObjectId = ObjectId.get();
    String cardNumber = "1234567890123456";
    String firstName = "Jane";
    String lastName = "Doe";
    String securityCode = "123";
    String expDate = "11/2023";
    Address address = new Address();
    ObjectId addressObjectId = ObjectId.get();
    String address1 = "930 14th Ave";
    String address2 = "Apt 603";
    String city = "Seattle";
    String state = "WA";
    String zip = "98109";
    address.setId(addressObjectId);
    address.setAddress1(address1);
    address.setAddress2(address2);
    address.setCity(city);
    address.setState(state);
    address.setZip(zip);
    creditCard.setId(creditCardObjectId);
    creditCard.setCardNumber(cardNumber);
    creditCard.setFirstName(firstName);
    creditCard.setLastName(lastName);
    creditCard.setSecurityCode(securityCode);
    creditCard.setExpDate(expDate);
    creditCard.setBillingAddress(address);
    customerController.addCard(customer, creditCard);
    assertTrue(
        customerController.getCustomer(customer.getId()).getCreditCards().contains(creditCard));
  }

  @Test
  void testCanUpdateCard() throws Exception {
    Customer customer = customerController.getCustomer(objectId);
    CreditCard creditCard = new CreditCard();
    ObjectId creditCardObjectId = ObjectId.get();
    String cardNumber = "1234567890123456";
    String firstName = "Jane";
    String lastName = "Doe";
    String securityCode = "123";
    String expDate = "11/2023";
    Address address = new Address();
    ObjectId addressObjectId = ObjectId.get();
    String address1 = "930 14th Ave";
    String address2 = "Apt 603";
    String city = "Seattle";
    String state = "WA";
    String zip = "98109";
    address.setId(addressObjectId);
    address.setAddress1(address1);
    address.setAddress2(address2);
    address.setCity(city);
    address.setState(state);
    address.setZip(zip);
    creditCard.setId(creditCardObjectId);
    creditCard.setCardNumber(cardNumber);
    creditCard.setFirstName(firstName);
    creditCard.setLastName(lastName);
    creditCard.setSecurityCode(securityCode);
    creditCard.setExpDate(expDate);
    creditCard.setBillingAddress(address);
    customerController.addCard(customer, creditCard);
    String newExpDate = "12/2023";
    creditCard.setExpDate(newExpDate);
    customerController.updateCard(customer, creditCard);
    assertEquals(customerController.getCustomer(objectId).getCreditCards().get(0).getExpDate()
        , newExpDate);
  }

  @Test
  void testCanDeleteCard() throws Exception {
    Customer customer = customerController.getCustomer(objectId);
    CreditCard creditCard = new CreditCard();
    ObjectId creditCardObjectId = ObjectId.get();
    String cardNumber = "1234567890123456";
    String firstName = "Jane";
    String lastName = "Doe";
    String securityCode = "123";
    String expDate = "11/2023";
    Address address = new Address();
    ObjectId addressObjectId = ObjectId.get();
    String address1 = "930 14th Ave";
    String address2 = "Apt 603";
    String city = "Seattle";
    String state = "WA";
    String zip = "98109";
    address.setId(addressObjectId);
    address.setAddress1(address1);
    address.setAddress2(address2);
    address.setCity(city);
    address.setState(state);
    address.setZip(zip);
    creditCard.setId(creditCardObjectId);
    creditCard.setCardNumber(cardNumber);
    creditCard.setFirstName(firstName);
    creditCard.setLastName(lastName);
    creditCard.setSecurityCode(securityCode);
    creditCard.setExpDate(expDate);
    creditCard.setBillingAddress(address);
    customerController.addCard(customer, creditCard);
    customerController.deleteCard(customer, creditCard);
    assertTrue(customerController.getCustomer(objectId).getCreditCards().size() == 0);

  }
}
