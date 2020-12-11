package edu.northeastern.cs5500.delivery;

import static spark.Spark.*;

import edu.northeastern.cs5500.delivery.controller.*;
import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.*;
import edu.northeastern.cs5500.delivery.service.MongoDBService;

public class App {
    static RepositoryModule repositoryModule = new RepositoryModule();
    static MongoDBService mongoDBService = new MongoDBService();
    static CartController cartController =
            new CartController(repositoryModule.provideCartRepository());
    static OrderController orderController =
            new OrderController(repositoryModule.provideOrderRepository(mongoDBService));
    static CustomerController customerController =
            new CustomerController(repositoryModule.provideCustomerRepository(mongoDBService));
    static RestaurantController restaurantController =
            new RestaurantController(repositoryModule.provideRestaurantRepository(mongoDBService));

    static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 5000; // run on port 5000 by default
    }

    public static void main(String[] arg) throws Exception {
        System.out.println("--------------Welcome to SnowPaste Delivery-------------");

        // Restaurant restaurant = (Restaurant) restaurantController.getRestaurants().toArray()[0];
        // Dish dish1 = restaurant.getMenu().get(0);
        // Dish dish2 = restaurant.getMenu().get(1);

        // Customer customer = (Customer) customerController.getCustomers().toArray()[0];
        // customer.setCart(new Cart());

        // cartController.addCart(customer.getCart());
        // Cart cart = customer.getCart();

        // cartController.addDish(dish1, cart);
        // TimeUnit.SECONDS.sleep(1);
        // cartController.removeDish(dish1, cart);
        // TimeUnit.SECONDS.sleep(1);
        // cartController.addDish(dish1, cart);
        // TimeUnit.SECONDS.sleep(1);
        // cartController.addDish(dish2, cart);
        // TimeUnit.SECONDS.sleep(1);
        // Order order = orderController.makeOrder(customer, restaurant);
        // TimeUnit.SECONDS.sleep(3);
        // orderController.completeOrder(order);

        // run on port 5000
        port(getAssignedPort());

        // Allow all cross-origin requests
        // Don't do this for real projects!
        options(
                "/*",
                (request, response) -> {
                    String accessControlRequestHeaders =
                            request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header(
                                "Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod =
                            request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // print all unhandled exceptions
        exception(Exception.class, (e, req, res) -> e.printStackTrace());

        // load and start the server
        DaggerServerComponent.create().server().start();
    }
}
