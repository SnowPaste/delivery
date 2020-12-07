package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

class DishControllerTest {

    @Test
    void testRegisterCreatesDish() {
        DishController dishController = new DishController(new InMemoryRepository<Dish>());
        assertThat(dishController.getDishes()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidDishes() {
        DishController dishController = new DishController(new InMemoryRepository<Dish>());

        for (Dish dish : dishController.getDishes()) {
            assertWithMessage(dish.getName()).that(dish.isValid()).isTrue();
        }
    }

    @Test
    void getDish() {}
}
