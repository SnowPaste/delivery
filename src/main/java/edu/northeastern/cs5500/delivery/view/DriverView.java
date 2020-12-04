package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.halt;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DriverController;
import edu.northeastern.cs5500.delivery.controller.OrderController;
import edu.northeastern.cs5500.delivery.model.Driver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Order.Status;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DriverView implements View {

    @Inject
    DriverView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject DriverController driverController;

    @Inject OrderController orderController;

    @Override
    public void register() {
        log.info("DriverView > register");

        put( // driver takes a new order
                "/driver/:driver_id/take_an_order/:order_id",
                (request, response) -> {
                    final String orderIdString = request.params(":order_id");
                    log.debug("/driver/:driver_id/take_an_order/:order_id<{}>", orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
                    if (order == null) {
                        halt(404);
                    }
                    final String driverIdString = request.params(":driver_id");
                    log.debug("/driver/:driver_id<{}>/take_an_order/:order_id", driverIdString);
                    final ObjectId driverId = new ObjectId(driverIdString);
                    Driver driver = driverController.getDriver(driverId);
                    if (driver == null) {
                        halt(404);
                    }
                    if (order.getStatus() != Status.WAITING_FOR_DRIVER) {
                        halt(404);
                    }
                    boolean flag = driverController.takeAnOrder(order, driver);
                    if (!flag) {
                        response.type("application/json");
                        response.status(400);
                        return response;
                    }
                    response.type("application/json");
                    response.status(200);
                    return response;
                },
                jsonTransformer);

        put( // add a new driver
                "/driver/add_new_driver",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Driver driver = mapper.readValue(request.body(), Driver.class);
                    if (!driver.isValid()) {
                        response.status(400);
                        return "Request body invalid";
                    }
                    driverController.addDriver(driver);
                    response.status(200);
                    return response;
                });

        put( // update driver information
                "/driver/update_a_driver/:driver_id",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Driver driver = mapper.readValue(request.body(), Driver.class);
                    if (!driver.isValid()) {
                        response.status(404);
                        return "Request body invalid";
                    }
                    driverController.updateDriver(driver);
                    response.status(200);
                    return response;
                },
                jsonTransformer);

        put( // driver mark  an order as completed
                "/driver/:driver_id/mark_a_completed_order/:order_id",
                (request, response) -> {
                    final String orderIdString = request.params(":order_id");
                    log.debug(
                            "/driver/:driver_id/mark_a_completed_order/:order_id<{}>", orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
                    if (order == null) {
                        halt(404);
                    }
                    orderController.completeOrder(order);
                    response.type("application/json");
                    response.status(200);
                    return response;
                },
                jsonTransformer);

        put( // a driver picked up an order, and set the status of the order to be PICKED_UP
                "/driver/:driver_id/set_order_status_picked_up/:order_id",
                (request, response) -> {
                    final String driverIdString = request.params(":driver_id");
                    log.debug(
                            "/driver/:driver_id<{}>/set_order_status_picked_up/:order_id",
                            driverIdString);
                    final ObjectId driverId = new ObjectId(driverIdString);
                    Driver driver = driverController.getDriver(driverId);
                    if (driver == null) {
                        halt(404);
                    }
                    final String orderIdString = request.params(":order_id");
                    log.debug(
                            "/driver/:driver_id/set_order_status_picked_up/:order_id<{}>",
                            orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
                    if (order == null) {
                        halt(404);
                    }
                    if (!driver.getCurrOrders().contains(order)) {
                        response.type("application/json");
                        response.status(400);
                        return response;
                    }
                    OrderController.setOrderStatusToPickedUp(order);
                    response.type("application/json");
                    response.status(200);
                    return response;
                },
                jsonTransformer);

        delete( // delete a driver
                "/driver/delete_a_driver/:driver_id",
                (request, response) -> {
                    final String paramId = request.params(":driver_id");
                    log.debug("/driver/delete_a_driver/:driver_id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Driver driver = driverController.getDriver(id);
                    if (driver == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    response.status(200);
                    return response;
                },
                jsonTransformer);
    }
}
