package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Address;
import edu.northeastern.cs5500.delivery.model.Cart;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Order.Status;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantControllerTest {
    private InMemoryRepository<Restaurant> inMemoryRepository = new InMemoryRepository<>();
    private RestaurantController restaurantController =
            new RestaurantController(inMemoryRepository);
    private Restaurant restaurant1 = new Restaurant(), restaurant2 = new Restaurant();
    private Dish dishToBeRemoved;

    @BeforeEach
    void setUp() {
        restaurant1.setId(ObjectId.get());
        restaurant1.setName("restaurant1");
        Address address1 = new Address();
        address1.setAddress1("add");
        address1.setCity("city1");
        address1.setState("state1");
        address1.setZip("zip1");
        restaurant1.setAddress(address1);
        restaurant1.setPhone("phone1");
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
        restaurant1.setMenu(menu1);
        this.dishToBeRemoved = dish1;

        restaurant2.setId(ObjectId.get());
        restaurant2.setName("restaurant2");
        Address address2 = new Address();
        address2.setAddress1("add2");
        address2.setCity("city2");
        address2.setState("state2");
        address2.setZip("zip2");
        restaurant2.setAddress(address2);
        restaurant2.setPhone("phone2");
        ArrayList<Dish> menu2 = new ArrayList<>();
        Dish dish3 = new Dish();
        dish3.setId(ObjectId.get());
        dish3.setName("dish3");
        dish3.setPrice(13.49);
        Dish dish4 = new Dish();
        dish4.setId(ObjectId.get());
        dish4.setName("dish4");
        dish4.setPrice(8.79);
        menu2.add(dish3);
        menu2.add(dish4);
        restaurant2.setMenu(menu2);

        inMemoryRepository.add(restaurant1);
        inMemoryRepository.add(restaurant2);
    }

    @Test
    void getRestaurant() {
        assertNotNull(restaurantController.getRestaurant(restaurant1.getId()));
    }

    @Test
    void getRestaurants() {
        assertEquals(3, restaurantController.getRestaurants().size());
    }

    @Test
    void addRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ObjectId.get());
        restaurant.setName("restaurant");
        Address address = new Address();
        address.setAddress1("add");
        address.setCity("city");
        address.setState("state");
        address.setZip("zip");
        restaurant.setAddress(address);
        restaurant.setPhone("phone1");
        ArrayList<Dish> menu = new ArrayList<>();
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setName("dish1");
        dish1.setPrice(17.0);
        Dish dish2 = new Dish();
        dish2.setId(ObjectId.get());
        dish2.setName("dish2");
        dish2.setPrice(28.49);
        menu.add(dish1);
        menu.add(dish2);
        restaurant.setMenu(menu);
        restaurantController.addRestaurant(restaurant);
        assertTrue(restaurantController.getRestaurants().contains(restaurant));
    }

    @Test
    void addRestaurantThrowsInvalidException() {
        Restaurant restaurant = new Restaurant();
        assertThrows(
                Exception.class,
                () -> {
                    restaurantController.addRestaurant(restaurant);
                },
                "InvalidRestaurantException");
    }

    @Test
    void addRestaurantThrowsDuplicatedException() {
        assertThrows(
                Exception.class,
                () -> {
                    restaurantController.addRestaurant(restaurant2);
                },
                "DuplicateKeyException");
    }

    @Test
    void updateRestaurant() throws Exception {
        restaurant1.setPhone("newPhone");
        restaurantController.updateRestaurant(restaurant1);
        assertEquals(
                "newPhone", restaurantController.getRestaurant(restaurant1.getId()).getPhone());
    }

    @Test
    void deleteRestaurant() throws Exception {
        restaurantController.deleteRestaurant(restaurant2.getId());
        assertFalse(restaurantController.getRestaurants().contains(restaurant2));
        assertTrue(restaurantController.getRestaurants().contains(restaurant1));
    }

    @Test
    void addDishes() throws Exception {
        Dish dish = new Dish();
        dish.setId(ObjectId.get());
        dish.setName("newDish");
        dish.setPrice(9.9);
        restaurantController.addDishes(restaurant1, dish);
        assertTrue(
                restaurantController.getRestaurant(restaurant1.getId()).getMenu().contains(dish));
    }

    @Test
    void deleteDishes() throws Exception {
        restaurantController.deleteDishes(restaurant1, this.dishToBeRemoved);
        assertFalse(
                restaurantController
                        .getRestaurant(restaurant1.getId())
                        .getMenu()
                        .contains(this.dishToBeRemoved));
    }

    @Test
    void prepareOrder() throws Exception {
        Order order1 = new Order();
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

        restaurantController.prepareOrder(order1);
        assertEquals(Status.PREPARING, order1.getStatus());
    }

    @Test
    void waitOrder() throws Exception {
        Order order1 = new Order();
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

        restaurantController.prepareOrder(order1);
        restaurantController.waitOrder(order1);
        assertEquals(Status.WAITING_FOR_DRIVER, order1.getStatus());
    }
}
