package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Address;
import edu.northeastern.cs5500.delivery.model.Cart;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.model.Driver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Order.Status;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderControllerTest {
    private InMemoryRepository<Order> inMemoryRepository = new InMemoryRepository<>();
    private Order order1 = new Order(), order2 = new Order();
    private OrderController orderController = new OrderController(inMemoryRepository);

    @BeforeEach
    void setUp() {
        order1.setId(ObjectId.get());
        Customer customer1 = new Customer();
        Address restaurantAddress1 = new Address();
        ArrayList<Dish> menu1 = new ArrayList<>();
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setName("dish1");
        dish1.setPrice(9.9);
        Dish dish2 = new Dish();
        dish2.setId(ObjectId.get());
        dish2.setName("dish2");
        dish2.setPrice(19.9);
        menu1.add(dish1);
        menu1.add(dish2);
        restaurantAddress1.setId(ObjectId.get());
        restaurantAddress1.setAddress1("address1");
        restaurantAddress1.setCity("city");
        restaurantAddress1.setState("state");
        restaurantAddress1.setZip("zip");
        customer1.setId(ObjectId.get());
        customer1.setAccountName("accountName");
        customer1.setFirstName("firstName");
        customer1.setLastName("lastName");
        customer1.setPassword("password");
        customer1.setPhoneNumber("phoneNum");
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(ObjectId.get());
        restaurant1.setName("name");
        restaurant1.setAddress(restaurantAddress1);
        restaurant1.setPhone("phone");
        restaurant1.setMenu(menu1);
        Cart cart1 = new Cart();
        cart1.setId(ObjectId.get());
        order1.setCustomer(customer1);
        order1.setRestaurant(restaurant1);
        order1.setCart(cart1);

        order2.setId(ObjectId.get());
        Customer customer2 = new Customer();
        Address restaurantAddress2 = new Address();
        ArrayList<Dish> menu2 = new ArrayList<>();
        Dish dish3 = new Dish();
        dish3.setId(ObjectId.get());
        dish3.setName("dish3");
        dish3.setPrice(9.9);
        Dish dish4 = new Dish();
        dish4.setId(ObjectId.get());
        dish4.setName("dish4");
        dish4.setPrice(19.9);
        menu2.add(dish3);
        menu2.add(dish4);
        restaurantAddress2.setId(ObjectId.get());
        restaurantAddress2.setAddress1("address2");
        restaurantAddress2.setCity("city2");
        restaurantAddress2.setState("state2");
        restaurantAddress2.setZip("zip2");
        customer2.setId(ObjectId.get());
        customer2.setAccountName("accountName2");
        customer2.setFirstName("firstName2");
        customer2.setLastName("lastName2");
        customer2.setPassword("password2");
        customer2.setPhoneNumber("phoneNum2");
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(ObjectId.get());
        restaurant2.setName("name2");
        restaurant2.setAddress(restaurantAddress2);
        restaurant2.setPhone("phone2");
        restaurant2.setMenu(menu2);
        Cart cart2 = new Cart();
        cart2.setId(ObjectId.get());
        order2.setCustomer(customer2);
        order2.setRestaurant(restaurant2);
        order2.setCart(cart2);

        inMemoryRepository.add(order1);
        inMemoryRepository.add(order2);
    }

    @Test
    void getOrder() {
        assertNotNull(orderController.getOrder(order1.getId()));
    }

    @Test
    void getOrders() {
        assertEquals(2, orderController.getOrders().size());
    }

    @Test
    void addOrder() throws Exception {
        Order order = new Order();
        order.setId(ObjectId.get());
        Customer customer = new Customer();
        Address restaurantAddress = new Address();
        ArrayList<Dish> menu = new ArrayList<>();
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setName("dish3");
        dish1.setPrice(9.9);
        Dish dish2 = new Dish();
        dish2.setId(ObjectId.get());
        dish2.setName("dish4");
        dish2.setPrice(19.9);
        menu.add(dish1);
        menu.add(dish2);
        restaurantAddress.setId(ObjectId.get());
        restaurantAddress.setAddress1("address2");
        restaurantAddress.setCity("city2");
        restaurantAddress.setState("state2");
        restaurantAddress.setZip("zip2");
        customer.setId(ObjectId.get());
        customer.setAccountName("accountName2");
        customer.setFirstName("firstName2");
        customer.setLastName("lastName2");
        customer.setPassword("password2");
        customer.setPhoneNumber("phoneNum2");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ObjectId.get());
        restaurant.setName("name2");
        restaurant.setAddress(restaurantAddress);
        restaurant.setPhone("phone2");
        restaurant.setMenu(menu);
        Cart cart = new Cart();
        cart.setId(ObjectId.get());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setCart(cart);
        order.setId(ObjectId.get());
        orderController.addOrder(order);
        assertTrue(orderController.getOrders().contains(order));
    }

    @Test
    void addOrderThrowsInvalidException() {
        Order order = new Order();
        assertThrows(
                Exception.class,
                () -> {
                    orderController.addOrder(order);
                },
                "OrderController > getOrders()");
    }

    @Test
    void addOrderThrowsAlreadyExistsException() {
        assertThrows(
                Exception.class,
                () -> {
                    orderController.addOrder(order2);
                },
                "Order already exists");
    }

    @Test
    void updateOrder() {
        order1.setStatus(Status.CANCELLED);
        assertEquals(Status.CANCELLED, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void deleteOrder() throws Exception {
        orderController.deleteOrder(order1.getId());
        assertFalse(orderController.getOrders().contains(order1));
        assertTrue(orderController.getOrders().contains(order2));
    }

    @Test
    void getOrderStatus() throws Exception {
        assertNull(OrderController.getOrderStatus(order1));
        order1.setStatus(Status.CANCELLED);
        orderController.updateOrder(order1);
        assertEquals(Status.CANCELLED, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void getOrderEstDeliverTime() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        order1.setCreateTime(now);
        orderController.getOrderEstDeliverTime(order1);
        assertEquals(
                now.plusHours(1), orderController.getOrder(order1.getId()).getEstDeliverTime());
    }

    @Test
    void getOrderEstDeliverTimeThrowsOrderNotCreatedException() {
        assertThrows(
                Exception.class,
                () -> {
                    orderController.getOrderEstDeliverTime(order1);
                },
                "Your order has not been created yet...");
    }

    @Test
    void setOrderStatusToProcessing() {
        orderController.setOrderStatusToProcessing(order1);
        assertEquals(Status.PROCESSING, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void setOrderStatusToPreparing() {
        OrderController.setOrderStatusToPreparing(order1);
        assertEquals(Status.PREPARING, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void setOrderStatusToWaiting() {
        OrderController.setOrderStatusToWaiting(order1);
        assertEquals(
                Status.WAITING_FOR_DRIVER, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void setOrderStatusToPickedUp() {
        OrderController.setOrderStatusToPickedUp(order1);
        assertEquals(Status.PICKED_UP, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void setOrderStatusToDelivered() {
        OrderController.setOrderStatusToDelivered(order1);
        assertEquals(Status.DELIVERED, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void setOrderStatusToCancelled() {
        OrderController.setOrderStatusToCancelled(order1);
        assertEquals(Status.CANCELLED, orderController.getOrder(order1.getId()).getStatus());
    }

    @Test
    void setOrderDriver() {
        Driver driver1 = new Driver();
        driver1.setId(ObjectId.get());
        driver1.setFirstName("firstName1");
        driver1.setLastName("lastName1");
        driver1.setPhoneNumber("phoneNum1");
        driver1.setCarMaker("carMaker1");
        driver1.setCarModel("carModel1");
        driver1.setCarLicence("license1");
        orderController.setOrderDriver(order1, driver1);
        assertEquals(driver1.getId(), orderController.getOrder(order1.getId()).getDriver().getId());
    }

    @Test
    void makeOrder() throws Exception {
        Customer customer = new Customer();
        customer.setId(ObjectId.get());
        customer.setAccountName("accountName");
        customer.setPassword("passWord");
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setEmail("email");
        customer.setPhoneNumber("phoneNumber");

        Address restaurantAddress = new Address();
        restaurantAddress.setId(ObjectId.get());
        restaurantAddress.setAddress1("address1");
        restaurantAddress.setCity("city");
        restaurantAddress.setState("state");
        restaurantAddress.setZip("zip");

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

        Restaurant restaurant = new Restaurant();
        restaurant.setId(ObjectId.get());
        restaurant.setName("name");
        restaurant.setAddress(restaurantAddress);
        restaurant.setPhone("phone");
        restaurant.setMenu(menu);

        Cart cart = new Cart();
        cart.setId(ObjectId.get());
        ArrayList<Dish> items = cart.getItems();
        items.add(dish1);
        cart.setItems(items);
        customer.setCart(cart);

        Order order = orderController.makeOrder(customer, restaurant);
        assertNotNull(order);
        assertEquals(customer.getId(), order.getCustomer().getId());
        assertEquals(restaurant.getId(), order.getRestaurant().getId());
    }

    @Test
    void cancelOrder() throws Exception {
        order1.setStatus(Status.PROCESSING);
        orderController.cancelOrder(order1);
        assertEquals(Status.CANCELLED, order1.getStatus());
    }

    @Test
    void cancelOrderThrowsException() {
        order1.setStatus(Status.PREPARING);
        assertThrows(
                Exception.class,
                () -> {
                    orderController.cancelOrder(order1);
                },
                "The restaurant has started preparing your order, your order can't be cancelled");
    }
}
