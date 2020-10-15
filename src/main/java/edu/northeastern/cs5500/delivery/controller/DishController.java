package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DishController {
    private final GenericRepository<Dish> dishes;

    @Inject
    DishController(GenericRepository<Dish> dishRepository) {
        dishes = dishRepository;

        log.info("DishController > construct");

        if (dishes.count() > 0) {
            return;
        }

        log.info("DishController > construct > adding default dishes");

        final Dish defaultDish1 = new Dish();
        defaultDish1.setName("Hot dog");
        defaultDish1.setPrice(5.00);

        final Dish defaultDish2 = new Dish();
        defaultDish2.setName("A steak");
        defaultDish2.setPrice(25.00);

        try {
            addDish(defaultDish1);
            addDish(defaultDish2);
        } catch (Exception e) {
            log.error("DishController > construct > adding default dishes > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Dish getDish(@Nonnull ObjectId uuid) {
        log.debug("DishController > getDish({})", uuid);
        return dishes.get(uuid);
    }

    @Nonnull
    public Collection<Dish> getDishes() {
        log.debug("DeliveryController > getDishes()");
        return dishes.getAll();
    }

    @Nonnull
    public Dish addDish(@Nonnull Dish dish) throws Exception {
        log.debug("DishController > addDish(...)");
        if (!dish.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidDishException");
        }

        ObjectId id = dish.getId();

        if (id != null && dishes.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return dishes.add(dish);
    }

    public void updateDish(@Nonnull Dish delivery) throws Exception {
        log.debug("DishController > updateDish(...)");
        dishes.update(delivery);
    }

    public void deleteDish(@Nonnull ObjectId id) throws Exception {
        log.debug("DishController > deleteDish(...)");
        dishes.delete(id);
    }
}
