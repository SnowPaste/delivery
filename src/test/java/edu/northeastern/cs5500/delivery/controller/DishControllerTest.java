package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DishControllerTest {
    private InMemoryRepository<Dish> inMemoryRepository = new InMemoryRepository<>();
    private Dish dish1 = new Dish(), dish2 = new Dish();
    private DishController dishController = new DishController(inMemoryRepository);

    @BeforeEach
    void setUp() {
        dish1.setId(ObjectId.get());
        dish1.setName("Big Mac");
        dish1.setPrice(4.79);
        dish2.setId(ObjectId.get());
        dish2.setName("Cheeseburger");
        dish2.setPrice(1.39);
        inMemoryRepository.add(dish1);
        inMemoryRepository.add(dish2);
    }

    @Test
    void getDish() {
        assertEquals(dish1.getId(), dishController.getDish(dish1.getId()).getId());
    }

    @Test
    void getDishes() {
        assertEquals(4, dishController.getDishes().size());
    }

    @Test
    void addDish() throws Exception {
        Dish dish = new Dish();
        dish.setId(ObjectId.get());
        dish.setName("Regular Soda");
        dish.setPrice(0.99);
        dishController.addDish(dish);
        assertEquals(5, dishController.getDishes().size());
        assertTrue(dishController.getDishes().contains(dish));
    }

    @Test
    void addDishThrowsInvalidException() throws Exception {
        Dish dish = new Dish();
        assertThrows(
                Exception.class,
                () -> {
                    dishController.addDish(dish);
                },
                "InvalidDishException");
    }

    @Test
    void addDishThrowsDuplicateKeyException() throws Exception {
        assertThrows(
                Exception.class,
                () -> {
                    dishController.addDish(dish1);
                },
                "DuplicateKeyException");
    }

    @Test
    void updateDish() throws Exception {
        dish1.setPrice(5.79);
        dishController.updateDish(dish1);
        assertEquals(5.79, dishController.getDish(dish1.getId()).getPrice());
    }

    @Test
    void deleteDish() throws Exception {
        dishController.deleteDish(dish2.getId());
        assertFalse(dishController.getDishes().contains(dish2));
        assertTrue(dishController.getDishes().contains(dish1));
    }
}
