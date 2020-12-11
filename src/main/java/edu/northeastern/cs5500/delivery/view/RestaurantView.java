package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.put;

import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DishController;
import edu.northeastern.cs5500.delivery.controller.OrderController;
import edu.northeastern.cs5500.delivery.controller.RestaurantController;
import edu.northeastern.cs5500.delivery.model.Dish;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RestaurantView implements View {
    @Inject
    RestaurantView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject RestaurantController restaurantController;

    @Inject DishController dishController;

    @Inject OrderController orderController;

    @Override
    public void register() {
        log.info("RestaurantView > register");

        get(
            "/restaurants",
            (request, response) -> {
                log.debug("/Restaurant");
                response.type("application/json");
                return restaurantController.getRestaurants();
            },
            jsonTransformer);

        get( // get the information of a certain res
                "/restaurant/:restaurant_id",
                (request, response) -> {
                    final String restaurantIdString = request.params(":restaurant_id");
                    log.debug("/restaurant/:restaurant_id<{}>", restaurantIdString);
                    final ObjectId restaurantId = new ObjectId(restaurantIdString);
                    Restaurant restaurant = restaurantController.getRestaurant(restaurantId);
                    if (restaurant == null) {
                        //                        halt(404);
                        response.status(200);
                        return response;
                    }
                    response.type("application/json");
                    return restaurant;
                },
                jsonTransformer);


        get( // get the menu of a certain res
                "/restaurant/:restaurant_id/get_menu",
                (request, response) -> {
                    final String restaurantIdString = request.params(":restaurant_id");
                    log.debug("/restaurant/:restaurant_id<{}>/get_menu", restaurantIdString);
                    final ObjectId restaurantId = new ObjectId(restaurantIdString);
                    Restaurant restaurant = restaurantController.getRestaurant(restaurantId);
                    if (restaurant == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return restaurant.getMenu();
                },
                jsonTransformer);

        delete( // delete a dish to a restaurant
                "/restaurant/:restaurant_id/delete_a_dish/:dish_id",
                (request, response) -> {
                    final String restaurantIdString = request.params(":restaurant_id");
                    final String dishIdString = request.params(":dish_id");

                    log.debug(
                            "/restaurant/:restaurant_id<{}>/delete_a_dish/:dish_id",
                            restaurantIdString);
                    log.debug(
                            "/restaurant/:restaurant_id/delete_a_dish/:dish_id<{}>", dishIdString);

                    final ObjectId restaurantId = new ObjectId(restaurantIdString);
                    final ObjectId dishId = new ObjectId(dishIdString);

                    Restaurant restaurant = restaurantController.getRestaurant(restaurantId);
                    Dish dish = dishController.getDish(dishId);
                    if (restaurant == null || dish == null) {
                        halt(404);
                    }
                    response.type("application/json");

                    ArrayList<Dish> menu = restaurant.getMenu();
                    boolean flag = false;
                    for (Dish d : menu) {
                        if (d.getName().equals(dish.getName())) {
                            flag = true;
                            dish = d;
                        }
                    }
                    if (flag) {
                        restaurantController.deleteDishes(restaurant, dish);
                        restaurantController.updateRestaurant(restaurant);
                        response.status(200);
                    } else {
                        response.status(400);
                    }

                    return response;
                });

        delete( // delete a restaurant
                "/restaurant/delete_restaurant/:restaurant_id",
                (request, response) -> {
                    final String restaurantIdString = request.params(":restaurant_id");
                    log.debug(
                            "/restaurant/restaurant_order/:restaurant_id<{}>", restaurantIdString);
                    final ObjectId restaurantId = new ObjectId(restaurantIdString);
                    Restaurant restaurant = restaurantController.getRestaurant(restaurantId);
                    if (restaurant == null) {
                        halt(404);
                    }
                    restaurantController.deleteRestaurant(restaurantId);
                    response.type("application/json");
                    response.status(200);
                    return response;
                });

        put( // add a dish to a certain restaurant
                "/restaurant/:restaurant_id/add_a_dish/:dish_id",
                (request, response) -> {
                    final String restaurantIdString = request.params(":restaurant_id");
                    final String dishIdString = request.params(":dish_id");

                    log.debug(
                            "restaurant/:restaurant_id<{}>/add_a_dish/:dish_id",
                            restaurantIdString);
                    log.debug("restaurant/:restaurant_id/add_a_dish/:dish_id<{}>", dishIdString);

                    final ObjectId restaurantId = new ObjectId(restaurantIdString);
                    final ObjectId dishId = new ObjectId(dishIdString);

                    Restaurant restaurant = restaurantController.getRestaurant(restaurantId);
                    Dish dish = dishController.getDish(dishId);

                    if (restaurant == null || !dish.isValid()) {
                        response.status(400);
                        return response;
                    }

                    restaurantController.addDishes(restaurant, dish);
                    restaurantController.updateRestaurant(restaurant);
                    response.type("application/json");
                    return response;
                });

        put( // set an order waiting
                "/restaurant/:restaurant_id/set_order_status_waiting/:order_id",
                (request, response) -> {
                    final String restaurantIdString = request.params(":restaurant_id");
                    final String orderIdString = request.params(":order_id");

                    log.debug(
                            "/restaurant/:restaurant_id<{}>/set_order_status_waiting/:order_id",
                            restaurantIdString);
                    log.debug(
                            "/restaurant/:restaurant_id/set_order_status_waiting/:order_id<{}>",
                            orderIdString);

                    final ObjectId restaurantId = new ObjectId(restaurantIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);

                    Restaurant restaurant = restaurantController.getRestaurant(restaurantId);
                    Order order = orderController.getOrder(orderId);
                    if (restaurant == null || order == null) {
                        halt(404);
                    }

                    restaurantController.waitOrder(order);
                    //                    orderController.updateOrder(order);
                    response.type("application/json");
                    response.status(200);
                    return response;
                });

        put( // set an order preparing
                "/restaurant/:restaurant_id/set_order_status_preparing/:order_id",
                (request, response) -> {
                    final String restaurantIdString = request.params(":restaurant_id");
                    final String orderIdString = request.params(":order_id");

                    log.debug(
                            "/restaurant/:restaurant_id<{}>/set_order_status_preparing/:order_id",
                            restaurantIdString);
                    log.debug(
                            "/restaurant/:restaurant_id/set_order_status_preparing/:order_id<{}>",
                            orderIdString);

                    final ObjectId restaurantId = new ObjectId(restaurantIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);

                    Restaurant restaurant = restaurantController.getRestaurant(restaurantId);
                    Order order = orderController.getOrder(orderId);
                    if (restaurant == null || order == null) {
                        halt(404);
                    }
                    response.type("application/json");

                    restaurantController.prepareOrder(order);
                    // order status is not in database
                    //                    orderController.updateOrder(order);
                    response.status(200);
                    return response;
                });
    }
}
