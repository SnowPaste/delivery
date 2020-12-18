package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Cart;
import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CartControllerTest {
    private InMemoryRepository<Cart> inMemoryRepository = new InMemoryRepository<>();
    private Cart cart1 = new Cart(), cart2 = new Cart();
    private CartController cartController = new CartController(inMemoryRepository);
    private ObjectId restaurantId;
    private Dish removeDish;

    @BeforeEach
    void setUp() {
        ObjectId restaurantId = ObjectId.get();
        this.restaurantId = restaurantId;
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setRestaurantId(restaurantId);
        dish1.setName("dish1");
        dish1.setDescription("description1");
        dish1.setPicture("picture1");
        dish1.setPrice(12.5);
        dish1.setRating(4.5);
        Dish dish2 = new Dish();
        dish2.setId(ObjectId.get());
        dish2.setRestaurantId(restaurantId);
        dish2.setName("dish2");
        dish2.setDescription("description2");
        dish2.setPicture("picture2");
        dish2.setPrice(9.9);
        dish2.setRating(4.8);
        Dish dish3 = new Dish();
        dish3.setId(ObjectId.get());
        dish3.setRestaurantId(restaurantId);
        dish3.setName("dish3");
        dish3.setDescription("description3");
        dish3.setPicture("picture3");
        dish3.setPrice(24.0);
        dish3.setRating(4.9);
        ObjectId cart1Id = ObjectId.get();
        ArrayList<Dish> items1 = new ArrayList<>();
        items1.add(dish1);
        items1.add(dish3);
        cart1.setId(cart1Id);
        cart1.setItems(items1);
        ObjectId cart2Id = ObjectId.get();
        ArrayList<Dish> items2 = new ArrayList<>();
        items2.add(dish1);
        items2.add(dish2);
        items2.add(dish3);
        cart2.setId(cart2Id);
        cart2.setItems(items2);
        this.removeDish = dish2;
        inMemoryRepository.add(cart1);
        inMemoryRepository.add(cart2);
    }

    @Test
    void getCart() {
        assertEquals(cart1.getId(), cartController.getCart(cart1.getId()).getId());
    }

    @Test
    void getCarts() {
        assertEquals(3, cartController.getCarts().size());
    }

    @Test
    void addCart() throws Exception {
        Cart cart = new Cart();
        cart.setId(ObjectId.get());
        Dish dish1 = new Dish();
        dish1.setId(ObjectId.get());
        dish1.setRestaurantId(ObjectId.get());
        dish1.setName("dish1");
        dish1.setDescription("description1");
        dish1.setPicture("picture1");
        dish1.setPrice(12.5);
        dish1.setRating(4.5);
        ArrayList<Dish> arrayList = new ArrayList<>();
        arrayList.add(dish1);
        cart.setItems(arrayList);
        cartController.addCart(cart);
        assertTrue(cartController.getCarts().contains(cart));
    }

    //  @Test
    //  void addCartThrowsInvalidException() throws Exception {
    //    Cart cart = new Cart();
    //    cart.setId(ObjectId.get());
    //    assertThrows(
    //        Exception.class,
    //        () -> {
    //          cartController.addCart(cart);
    //        },
    //        "Invalid Cart"
    //    );
    //  }

    @Test
    void addCartThrowsExistsException() throws Exception {
        assertThrows(
                Exception.class,
                () -> {
                    cartController.addCart(cart1);
                },
                "Cart already exists");
    }

    @Test
    void updateCart() throws Exception {
        cart1.setTip(5.0);
        cartController.updateCart(cart1);
        assertEquals(5.0, cartController.getCart(cart1.getId()).getTip());
    }

    @Test
    void deleteCart() throws Exception {
        cartController.deleteCart(cart1.getId());
        assertFalse(cartController.getCarts().contains(cart1));
        assertTrue(cartController.getCarts().contains(cart2));
    }



    @Test
    void addDishThrowsDifferentRestaurantException() throws Exception {
        Dish dish = new Dish();
        dish.setId(ObjectId.get());
        dish.setRestaurantId(ObjectId.get());
        dish.setName("dish");
        dish.setDescription("description");
        dish.setPicture("picture");
        dish.setPrice(23.0);
        dish.setRating(4.5);
        assertThrows(
                Exception.class,
                () -> {
                    cartController.addDish(dish, cart1);
                },
                "Only dishes from the same restaurant can be added");
    }

    @Test
    void addDish() throws Exception {
        Dish dish = new Dish();
        dish.setId(ObjectId.get());
        dish.setRestaurantId(this.restaurantId);
        dish.setName("dish");
        dish.setDescription("description");
        dish.setPicture("picture");
        dish.setPrice(23.0);
        dish.setRating(4.5);
        cartController.addDish(dish, cart2);
        assertTrue(cartController.getCart(cart2.getId()).getItems().contains(dish));
        assertEquals(4, cartController.getCart(cart2.getId()).getItems().size());
    }

    @Test
    void removeDish() throws Exception {
        cartController.removeDish(this.removeDish, cart2);
        assertFalse(cartController.getCart(cart2.getId()).getItems().contains(this.removeDish));
    }

    @Test
    void removeDishThrowsNotExistsException() throws Exception {
        assertThrows(
                Exception.class,
                () -> {
                    cartController.removeDish(this.removeDish, cart1);
                },
                "Dish doesn't exists");
    }

    @Test
    void emptyCart() throws Exception {
        Cart cart = cartController.emptyCart(cart1);
        assertEquals(0, cart.getItems().size());
    }
}
