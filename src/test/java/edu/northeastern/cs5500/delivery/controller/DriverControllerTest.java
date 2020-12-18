package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Address;
import edu.northeastern.cs5500.delivery.model.Cart;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.model.Driver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DriverControllerTest {
    private InMemoryRepository<Driver> inMemoryRepository = new InMemoryRepository<>();
    private Driver driver1 = new Driver(), driver2 = new Driver();
    private DriverController driverController = new DriverController(inMemoryRepository);

    @BeforeEach
    void setUp() {
        driver1.setId(ObjectId.get());
        driver1.setFirstName("firstName1");
        driver1.setLastName("lastName1");
        driver1.setPhoneNumber("phoneNum1");
        driver1.setCarMaker("carMaker1");
        driver1.setCarModel("carModel1");
        driver1.setCarLicence("license1");
        driver2.setId(ObjectId.get());
        driver2.setFirstName("firstName2");
        driver2.setLastName("lastName2");
        driver2.setPhoneNumber("phoneNum2");
        driver2.setCarMaker("carMaker2");
        driver2.setCarModel("carModel2");
        driver2.setCarLicence("license2");
        inMemoryRepository.add(driver1);
        inMemoryRepository.add(driver2);
    }

    @Test
    void getDriver() {
        assertNotNull(driverController.getDriver(driver1.getId()));
        assertNotNull(driverController.getDriver(driver2.getId()));
    }

    @Test
    void getDrivers() {
        assertEquals(2, driverController.getDrivers().size());
    }

    @Test
    void addDriver() throws Exception {
        Driver driver = new Driver();
        driver.setId(ObjectId.get());
        driver.setFirstName("firstName");
        driver.setLastName("lastName");
        driver.setPhoneNumber("phoneNum");
        driver.setCarMaker("carMaker");
        driver.setCarModel("carModel");
        driver.setCarLicence("license");
        driverController.addDriver(driver);
        assertTrue(driverController.getDrivers().contains(driver));
    }

    @Test
    void addDriverThrowsInvalidException() {
        Driver driver = new Driver();
        driver.setId(ObjectId.get());
        assertThrows(
                Exception.class,
                () -> {
                    driverController.addDriver(driver);
                },
                "InvalidDriverException");
    }

    @Test
    void addDriverDuplicatedKeyException() {
        assertThrows(
                Exception.class,
                () -> {
                    driverController.addDriver(driver1);
                },
                "DuplicateKeyException");
    }

    @Test
    void updateDriver() throws Exception {
        driver1.setFirstName("Emily");
        driverController.updateDriver(driver1);
        assertEquals("Emily", driverController.getDriver(driver1.getId()).getFirstName());
    }

    @Test
    void deleteDriver() throws Exception {
        driverController.deleteDriver(driver1.getId());
        assertTrue(driverController.getDrivers().contains(driver2));
        assertFalse(driverController.getDrivers().contains(driver1));
    }

    @Test
    void takeAnOrder() throws Exception {
        Order order = new Order();
        order.setId(ObjectId.get());
        Customer customer = new Customer();
        Address restaurantAddress = new Address();
        ArrayList<Dish> menu = new ArrayList<>();
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setName("dish1");
        dish1.setPrice(9.9);
        Dish dish2 = new Dish();
        dish2.setId(ObjectId.get());
        dish2.setName("dish2");
        dish2.setPrice(19.9);
        menu.add(dish1);
        menu.add(dish2);
        restaurantAddress.setId(ObjectId.get());
        restaurantAddress.setAddress1("address1");
        restaurantAddress.setCity("city");
        restaurantAddress.setState("state");
        restaurantAddress.setZip("zip");
        customer.setId(ObjectId.get());
        customer.setAccountName("accountName");
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setPassword("password");
        customer.setPhoneNumber("phoneNum");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ObjectId.get());
        restaurant.setName("name");
        restaurant.setAddress(restaurantAddress);
        restaurant.setPhone("phone");
        restaurant.setMenu(menu);
        Cart cart = new Cart();
        cart.setId(ObjectId.get());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setCart(cart);
        driverController.takeAnOrder(order, driver1);
        assertTrue(
                driverController
                        .getDriver(driver1.getId())
                        .getCurrOrders()
                        .contains(order.getId()));
    }

    @Test
    void takeAnOrderReturnFalse() throws Exception {
        driver1.setAvailable(false);
        Order order = new Order();
        order.setId(ObjectId.get());
        Customer customer = new Customer();
        Address restaurantAddress = new Address();
        ArrayList<Dish> menu = new ArrayList<>();
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setName("dish1");
        dish1.setPrice(9.9);
        Dish dish2 = new Dish();
        dish2.setId(ObjectId.get());
        dish2.setName("dish2");
        dish2.setPrice(19.9);
        menu.add(dish1);
        menu.add(dish2);
        restaurantAddress.setId(ObjectId.get());
        restaurantAddress.setAddress1("address1");
        restaurantAddress.setCity("city");
        restaurantAddress.setState("state");
        restaurantAddress.setZip("zip");
        customer.setId(ObjectId.get());
        customer.setAccountName("accountName");
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setPassword("password");
        customer.setPhoneNumber("phoneNum");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ObjectId.get());
        restaurant.setName("name");
        restaurant.setAddress(restaurantAddress);
        restaurant.setPhone("phone");
        restaurant.setMenu(menu);
        Cart cart = new Cart();
        cart.setId(ObjectId.get());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setCart(cart);
        driverController.takeAnOrder(order, driver1);
        assertFalse(
                driverController
                        .getDriver(driver1.getId())
                        .getCurrOrders()
                        .contains(order.getId()));
    }

    @Test
    void manageCompletedOrder() throws Exception {
        Order order = new Order();
        order.setId(ObjectId.get());
        Customer customer = new Customer();
        Address restaurantAddress = new Address();
        ArrayList<Dish> menu = new ArrayList<>();
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setName("dish1");
        dish1.setPrice(9.9);
        Dish dish2 = new Dish();
        dish2.setId(ObjectId.get());
        dish2.setName("dish2");
        dish2.setPrice(19.9);
        menu.add(dish1);
        menu.add(dish2);
        restaurantAddress.setId(ObjectId.get());
        restaurantAddress.setAddress1("address1");
        restaurantAddress.setCity("city");
        restaurantAddress.setState("state");
        restaurantAddress.setZip("zip");
        customer.setId(ObjectId.get());
        customer.setAccountName("accountName");
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setPassword("password");
        customer.setPhoneNumber("phoneNum");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ObjectId.get());
        restaurant.setName("name");
        restaurant.setAddress(restaurantAddress);
        restaurant.setPhone("phone");
        restaurant.setMenu(menu);
        Cart cart = new Cart();
        cart.setId(ObjectId.get());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setCart(cart);
        driverController.takeAnOrder(order, driver1);
        driverController.manageCompletedOrder(driver1, order);
        assertFalse(
                driverController
                        .getDriver(driver1.getId())
                        .getCurrOrders()
                        .contains(order.getId()));
        assertTrue(
                driverController
                        .getDriver(driver1.getId())
                        .getCompleteOrders()
                        .contains(order.getId()));
    }
}
