package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Address;
import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
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

    @Inject
    RestaurantController(GenericRepository<Restaurant> RestaurantRepository) {
        restaurants = RestaurantRepository;

        log.info("RestaurantController > construct");

        if (restaurants.count() > 0) {
            return;
        }

        log.info("RestaurantController > construct > adding default Restaurants");

        final Restaurant defaultRestaurant1 = new Restaurant();
        defaultRestaurant1.setName("Little Chengdu");
        defaultRestaurant1.setEmailAddress("lcd@gmail.com");
        defaultRestaurant1.setCuisine(Restaurant.Cuisine.INDIAN);

        LinkedList<Dish> menu1 = new LinkedList<>();
        final Dish defaultDish1 = new Dish();
        defaultDish1.setName("Hot dog");
        defaultDish1.setPrice(5.00);
        menu1.add(defaultDish1);

        defaultRestaurant1.setMenu(menu1);
        defaultRestaurant1.setPhone("206-779-9780");

        final LocalDateTime t1 = LocalDateTime.now();
        defaultRestaurant1.setStartDeliverTime(t1);

        final Address a1 = new Address();
        defaultRestaurant1.setAddress(a1);
        //
        //        final Restaurant defaultRestaurant2 = new Restaurant();
        //        final Dish defaultDish2 = new Dish();
        //        defaultDish2.setName("A steak");
        //        defaultDish2.setPrice(25.00);
        //        defaultRestaurant2.setName("A steak");
        //        defaultRestaurant2.setPrice(25.00);

        try {
            addRestaurant(defaultRestaurant1);
            //            addRestaurant(defaultRestaurant2);
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
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidRestaurantException");
        }

        ObjectId id = restaurant.getId();

        if (id != null && restaurants.get(id) != null) {
            // TODO: replace with a real duplicate key exception
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
}
