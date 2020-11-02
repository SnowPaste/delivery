package edu.northeastern.cs5500.delivery;

import static spark.Spark.*;

import com.mongodb.client.MongoDatabase;
import edu.northeastern.cs5500.delivery.controller.*;
import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.*;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.util.concurrent.TimeUnit;

public class App {

    static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 5000; // run on port 5000 by default
    }

    public static void main(String[] arg) throws Exception {
        RepositoryModule repositoryModule = new RepositoryModule();
        CartController cartController =
                new CartController(repositoryModule.provideCartRepository());
        OrderController orderController =
                new OrderController(repositoryModule.provideOrderRepository());
        System.out.println("--------------Welcome to SnowPaste Delivery-------------");
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("restaurant1");
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("restaurant2");

        Dish dish1 = new Dish();
        dish1.setName("dish1");
        dish1.setPrice(10.5);
        dish1.setRestaurant(restaurant1);

        Dish dish2 = new Dish();
        dish2.setName("dish2");
        dish2.setPrice(15.8);
        dish2.setRestaurant(restaurant1);

        Dish dish3 = new Dish();
        dish3.setName("dish3");
        dish3.setPrice(19.9);
        dish3.setRestaurant(restaurant2);

        Customer customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Doe");

        Cart cart = new Cart();
        customer.setCart(cart);
        cart.setCustomer(customer);

        cartController.addDish(dish1, cart);
        TimeUnit.SECONDS.sleep(1);
        cartController.addDish(dish2, cart);
        TimeUnit.SECONDS.sleep(1);
        cartController.removeDish(dish1, cart);
        TimeUnit.SECONDS.sleep(1);
        // cartController.addDish(dish3, restaurant2, cart);
        cartController.emptyCart(cart);
        TimeUnit.SECONDS.sleep(1);
        cartController.addDish(dish1, cart);
        TimeUnit.SECONDS.sleep(1);
        cartController.addDish(dish2, cart);
        TimeUnit.SECONDS.sleep(1);
        Order order = orderController.makeOrder(cart);
        TimeUnit.SECONDS.sleep(3);
        orderController.completeOrder(order);

        // // run on port 5000
        // port(getAssignedPort());

        // // Allow all cross-origin requests
        // // Don't do this for real projects!
        // options(
        //         "/*",
        //         (request, response) -> {
        //             String accessControlRequestHeaders =
        //                     request.headers("Access-Control-Request-Headers");
        //             if (accessControlRequestHeaders != null) {
        //                 response.header(
        //                         "Access-Control-Allow-Headers", accessControlRequestHeaders);
        //             }

        //             String accessControlRequestMethod =
        //                     request.headers("Access-Control-Request-Method");
        //             if (accessControlRequestMethod != null) {
        //                 response.header("Access-Control-Allow-Methods",
        // accessControlRequestMethod);
        //             }

        //             return "OK";
        //         });

        // before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // // print all unhandled exceptions
        // exception(Exception.class, (e, req, res) -> e.printStackTrace());

        // // load and start the server
        // DaggerServerComponent.create().server().start();

        MongoDBService service = new MongoDBService();
        MongoDatabase db = service.getMongoDatabase();
        System.out.println(db.getCollection("Restaurant"));
        for (String i : db.listCollectionNames()) {
            System.out.println(i);
        }
    }
}
