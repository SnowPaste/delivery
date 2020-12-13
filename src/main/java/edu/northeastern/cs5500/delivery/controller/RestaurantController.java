package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.*;
import edu.northeastern.cs5500.delivery.repository.RepositoryModule;
import edu.northeastern.cs5500.delivery.service.MongoDBService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RestaurantController {
    private final GenericRepository<Restaurant> restaurants;
    private static final RepositoryModule repositoryModule = new RepositoryModule();
    private static final MongoDBService mongoDBService = new MongoDBService();

    @Inject
    DriverController driverController =
            new DriverController(repositoryModule.provideDriverRepository(mongoDBService));

    @Inject
    public RestaurantController(GenericRepository<Restaurant> RestaurantRepository) {
        restaurants = RestaurantRepository;

        log.info("RestaurantController > construct");

        if (restaurants.count() > 0) {
            return;
        }

        log.info("RestaurantController > construct > adding default Restaurants");

               final Restaurant defaultRestaurant1 = new Restaurant();
               defaultRestaurant1.setName("Little Chengdu");
               defaultRestaurant1.setCuisine(Restaurant.Cuisine.INDIAN);
        
               ArrayList<Dish> menu1 = new ArrayList<>();
               final Dish defaultDish1 = new Dish();
               defaultDish1.setName("Hot dog");
               defaultDish1.setPrice(5.00);
               menu1.add(defaultDish1);
        
               defaultRestaurant1.setMenu(menu1);
               defaultRestaurant1.setPhone("206-779-9780");
        
               final LocalDateTime t1 = LocalDateTime.now();
               defaultRestaurant1.setStartTime(t1);
        
               final Address a1 = new Address();
               defaultRestaurant1.setAddress(a1);

               try {
                   addRestaurant(defaultRestaurant1);
               } catch (Exception e) {
                   log.error("RestaurantController > construct > adding default Restaurants > failure?");
                   e.printStackTrace();
               }
    }

    @Nullable
    public Restaurant getRestaurant(@Nonnull ObjectId uuid) {
        log.debug("RestaurantController > getRestaurant({})", uuid);
        return restaurants.get(uuid);
    }

    @Nonnull
    public Collection<Restaurant> getRestaurants() {
        log.debug("RestaurantController > getRestaurants()");
        return restaurants.getAll();
    }

    @Nonnull
    public Restaurant addRestaurant(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > addRestaurant(...)");
        if (!restaurant.isValid()) {
            throw new Exception("InvalidRestaurantException");
        }

        ObjectId id = restaurant.getId();

        if (id != null && restaurants.get(id) != null) {
            throw new Exception("DuplicateKeyException");
        }

        return restaurants.add(restaurant);
    }

    public void updateRestaurant(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > updateRestaurant(...)");
        restaurants.update(restaurant);
    }

    public void deleteRestaurant(@Nonnull ObjectId id) throws Exception {
        log.debug("RestaurantController > deleteRestaurant(...)");
        restaurants.delete(id);
    }

    public void addDishes(@Nonnull Restaurant restaurant, @Nonnull Dish dish) throws Exception {
        log.debug("RestaurantController > deleteRestaurant(...)");
        ArrayList<Dish> menu = restaurant.getMenu();
        menu.add(dish);
    }

    public void deleteDishes(@Nonnull Restaurant restaurant, @Nonnull Dish dish) throws Exception {
        log.debug("RestaurantController > deleteRestaurant(...)");
        restaurant.getMenu().remove(dish);
    }

    public void prepareOrder(@Nonnull Order order) throws Exception {
        log.debug("RestaurantController > finishingOrder(...)");
        OrderController.setOrderStatusToPreparing(order);
    }

    public void waitOrder(@Nonnull Order order) throws Exception {
        log.debug("RestaurantController > finishingOrder(...)");
        OrderController.setOrderStatusToWaiting(order);
    }
}
