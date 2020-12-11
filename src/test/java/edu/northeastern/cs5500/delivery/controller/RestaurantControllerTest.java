package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

class RestaurantControllerTest {

    @Test
    void testRegisterCreatesRestaurants() {
        RestaurantController restaurantController =
                new RestaurantController(new InMemoryRepository<Restaurant>());
        assertThat(restaurantController.getRestaurants()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidRestaurants() {
        RestaurantController restaurantController =
                new RestaurantController(new InMemoryRepository<Restaurant>());

        for (Restaurant restaurant : restaurantController.getRestaurants()) {
            assertWithMessage(restaurant.getName()).that(restaurant.isValid()).isTrue();
        }
    }

    @Test
    void getRestaurants() {}

    @Test
    void addDishes() {
        RestaurantController restaurantController =
                new RestaurantController(new InMemoryRepository<Restaurant>());

        for (Restaurant restaurant : restaurantController.getRestaurants()) {
            assertWithMessage(restaurant.getName()).that(restaurant.isValid()).isTrue();
        }
    }

    @Test
    void deleteDishes() throws Exception {
        RestaurantController restaurantController =
                new RestaurantController(new InMemoryRepository<Restaurant>());

        for (Restaurant restaurant : restaurantController.getRestaurants()) {
            int num = restaurant.getMenu().size();
            Dish dish = restaurant.getMenu().get(0);
            restaurantController.deleteDishes(restaurant, dish);
            num -= 1;
            assertEquals(num, restaurant.getMenu().size());
        }
    }

    @Test
    void prepareOrder() {
        OrderController orderController = new OrderController(new InMemoryRepository<Order>());

        //        for (Order order : orderController.getorders()) {
        //            assertWithMessage(order.getName()).that(restaurant.isValid()).isTrue();
        //        }
    }

    @Test
    void waitOrder() {}
}
