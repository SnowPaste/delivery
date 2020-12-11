package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.get;
import static spark.Spark.halt;

import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DishController;
import edu.northeastern.cs5500.delivery.model.Dish;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DishView implements View {

    @Inject
    DishView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject DishController dishController;

    @Override
    public void register() {
        log.info("DishView > register");

        get( // get the information of a certain dish
                "/dish/get_dish/:dish_id",
                (request, response) -> {
                    final String dishIdString = request.params(":dish_id");
                    log.debug("/dish/get_dish/:dish_id<{}>", dishIdString);
                    final ObjectId dishId = new ObjectId(dishIdString);
                    Dish dish = dishController.getDish(dishId);
                    if (dish == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return dish;
                },
                jsonTransformer);

        //        get(
        //                "/dish",
        //                (request, response) -> {
        //                    log.debug("/dish");
        //                    response.type("application/json");
        //                    return dishController.getDishes();
        //                },
        //                jsonTransformer);
        //
        //        get(
        //                "/dish/:id",
        //                (request, response) -> {
        //                    final String paramId = request.params(":id");
        //                    log.debug("/dish/:id<{}>", paramId);
        //                    final ObjectId id = new ObjectId(paramId);
        //                    Dish dish = dishController.getDish(id);
        //                    if (dish == null) {
        //                        halt(404);
        //                    }
        //                    response.type("application/json");
        //                    return dish;
        //                },
        //                jsonTransformer);
        //
        //        post(
        //                "/dish",
        //                (request, response) -> {
        //                    ObjectMapper mapper = new ObjectMapper();
        //                    Dish dish = mapper.readValue(request.body(), Dish.class);
        //                    if (!dish.isValid()) {
        //                        response.status(400);
        //                        return "";
        //                    }
        //
        //                    // Ignore the user-provided ID if there is one
        //                    dish.setId(null);
        //                    dish = dishController.addDish(dish);
        //
        //                    response.redirect(String.format("/dish/{}",
        // dish.getId().toHexString()), 301);
        //                    return dish;
        //                });
        //
        //        put(
        //                "/dish",
        //                (request, response) -> {
        //                    ObjectMapper mapper = new ObjectMapper();
        //                    Dish dish = mapper.readValue(request.body(), Dish.class);
        //                    if (!dish.isValid()) {
        //                        response.status(400);
        //                        return "";
        //                    }
        //
        //                    dishController.updateDish(dish);
        //                    return dish;
        //                });
        //
        //        delete(
        //                "/dish",
        //                (request, response) -> {
        //                    ObjectMapper mapper = new ObjectMapper();
        //                    Dish dish = mapper.readValue(request.body(), Dish.class);
        //
        //                    dishController.deleteDish(dish.getId());
        //                    return dish;
        //                });
    }
}
