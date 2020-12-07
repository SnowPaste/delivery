package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.RepositoryModule;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public class CustomerControllerTest {
    private static final RepositoryModule repositoryModule = new RepositoryModule();
    private static final MongoDBService mongoDBService = new MongoDBService();
    CustomerController customerController =
            new CustomerController(repositoryModule.provideCustomerRepository(mongoDBService));

    @Test
    void testRegisterCreatesCustomers() {
        CustomerController customerController =
                new CustomerController(repositoryModule.provideCustomerRepository(mongoDBService));
        assertThat(customerController.getCustomers()).isNotEmpty();
    }

    @Test
    void testCanGetCustomer() {
        ObjectId testId = new ObjectId("5f9fc4461ec87e4d750e958c");
        Customer testCustomer = customerController.getCustomer(testId);
        assertNotNull(testCustomer);
    }

    @Test
    void testCanGetCustomers() {
        assertNotEquals(0, customerController.getCustomers().size());
    }

    @Test
    void testCanAddCustomer() throws Exception {
        int before = customerController.getCustomers().size();
        Customer customer = new Customer();
        customer.setAccountName("swang");
        customer.setPassWord("sw123");
        customer.setFirstName("Steve");
        customer.setLastName("Wang");
        customer.setEmail("swang@mail.com");
        customer.setPhoneNumber("9178765234");
        customer.setOrderHistory(new ArrayList<ObjectId>());
        customer.setCart(new Cart());
        customer.setAddressList(new ArrayList<Address>());
        customer.setCreditCards(new ArrayList<CreditCard>());
        Customer testCustomer = customerController.addCustomer(customer);
        int after = customerController.getCustomers().size();
        assertEquals(1, after - before);
        customerController.deleteCustomer(testCustomer.getId());
    }

    @Test
    void testCanUpdateCustomer() throws Exception {
        Customer customer = customerController.getCustomers().iterator().next();
        String name = customer.getFirstName();
        customer.setFirstName("Ben");
        ObjectId id = customer.getId();
        customerController.updateCustomer(customer);
        assertEquals("Ben", customerController.getCustomer(id).getFirstName());
        customer.setFirstName(name);
        customerController.updateCustomer(customer);
    }

    @Test
    void testCanDeleteCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setAccountName("swang");
        customer.setPassWord("sw123");
        customer.setFirstName("Steve");
        customer.setLastName("Wang");
        customer.setEmail("swang@mail.com");
        customer.setPhoneNumber("9178765234");
        customer.setOrderHistory(new ArrayList<ObjectId>());
        customer.setCart(new Cart());
        customer.setAddressList(new ArrayList<Address>());
        customer.setCreditCards(new ArrayList<CreditCard>());
        Customer testCustomer = customerController.addCustomer(customer);
        int before = customerController.getCustomers().size();
        customerController.deleteCustomer(testCustomer.getId());
        int after = customerController.getCustomers().size();
        assertEquals(1, before - after);
        assertNull(customerController.getCustomer(testCustomer.getId()));
    }

    @Test
    void testCanAddAddress() throws Exception {
        Customer customer =
                customerController.getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"));
        int before =
                customerController
                        .getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"))
                        .getAddressList()
                        .size();
        Address address = new Address();
        address.setAddress1("401 Terry Ave");
        address.setCity("Seattle");
        address.setState("WA");
        address.setZip("98101");
        customerController.addAddress(customer, address);
        int after =
                customerController
                        .getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"))
                        .getAddressList()
                        .size();
        assertEquals(1, after - before);
    }

    @Test
    void testCanUpdateAddress() throws Exception {
        // Customer customer =
        //         customerController.getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"));
        // Address address = customer.getAddressList().get(0);
        // address.setZip("98106");
        // customerController.updateAddress(customer, address);
        // String after =
        //         customerController
        //                 .getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"))
        //                 .getAddressList()
        //                 .get(0)
        //                 .getZip();
        // assertEquals("98106", after);
    }

    @Test
    void testCanDeleteAddress() throws Exception {
        // Customer customer =
        //         customerController.getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"));
        // int before = customer.getAddressList().size();
        // Address address = customer.getAddressList().get(0);
        // customerController.deleteAddress(customer, address);
        // int after = customer.getAddressList().size();
        // assertEquals(1, before - after);
    }

    @Test
    void testCanAddCard() throws Exception {
        Customer customer =
                customerController.getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"));
        int before =
                customerController
                        .getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"))
                        .getCreditCards()
                        .size();
        System.out.println(before);
        CreditCard card = new CreditCard();
        card.setCardNumber("4039827394859403");
        card.setFirstName(customer.getFirstName());
        card.setLastName(customer.getLastName());
        card.setExpDate(LocalDate.now());
        card.setSecurityCode("123");
        card.setBillingAddress(customer.getAddressList().get(0));
        customerController.addCard(customer, card);
        int after =
                customerController
                        .getCustomer(new ObjectId("5f9fc4461ec87e4d750e958c"))
                        .getAddressList()
                        .size();
        System.out.println(customer.getCreditCards());
        assertEquals(1, after - before);
    }

    @Test
    void testCanUpdateCard() {}

    @Test
    void testCanDeleteCard() {}
}
