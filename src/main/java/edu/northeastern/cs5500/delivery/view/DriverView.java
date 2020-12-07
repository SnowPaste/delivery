package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.halt;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.get;

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
                      halt(404, "No such driver");
                    }
                    if (order.getStatus() != Status.WAITING_FOR_DRIVER) {
                        halt(404, "Order not ready for taking");
                    }
                    boolean flag = driverController.takeAnOrder(order, driver);
                    if (!flag) {
                        halt(400, "Driver cannot take more orders currently");
                    }
                    OrderController.setOrderStatusToPickedUp(order);
//                    orderController.updateOrder(order);
                    response.status(200);
                    return response;
                });

        post( // add a new driver
                "/driver/add_new_driver/",
                (request, response) -> {
                    log.info(request.body());
                    ObjectMapper mapper = new ObjectMapper();
                    Driver driver = mapper.readValue(request.body(), Driver.class);
                    if (!driver.isValid()) {
                        response.status(400);
                        return "Request body invalid";
                    }
                    driver.setId(null);
                  response.status(200);
                  return "New driver successfully added";
                });

        put( // update driver information
                "/driver/update_a_driver/:driver_id",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Driver driver = mapper.readValue(request.body(), Driver.class);
                    final String driverIdString = request.params(":driver_id");
                    ObjectId driverId = new ObjectId(driverIdString);
                    driver.setId(driverId);
                    if (!driver.isValid()) {
                        response.status(404);
                        return "Request body invalid";
                    }
                    driverController.updateDriver(driver);
                    response.status(200);
                    return "Driver information successfully updated";
                });

        put( // driver mark an order as completed
                "/driver/:driver_id/mark_a_completed_order/:order_id",
                (request, response) -> {
                    final String orderIdString = request.params(":order_id");
                    log.debug(
                            "/driver/:driver_id/mark_a_completed_order/:order_id<{}>", orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
////                    final String driverIdString = request.params(":driver_id");
////                    final ObjectId driverId = new ObjectId(driverIdString);
////                    Driver driver = driverController.getDriver(driverId);
                    if (order == null) {
                        halt(400);
                        return "No such order";
                    }
                    if (order.getStatus() != Status.PICKED_UP) {
                      halt(400);
                      return "Current order status cannot be marked as completed";
                    }
                    orderController.completeOrder(order);
                    orderController.updateOrder(order);
                    response.status(200);
                    return "The order has been marked as completed";
                });

//        put( // a driver picked up an order, and set the status of the order to be PICKED_UP
//                "/driver/:driver_id/set_order_status_picked_up/:order_id",
//                (request, response) -> {
//                    final String driverIdString = request.params(":driver_id");
//                    log.debug(
//                            "/driver/:driver_id<{}>/set_order_status_picked_up/:order_id",
//                            driverIdString);
//                    final ObjectId driverId = new ObjectId(driverIdString);
//                    Driver driver = driverController.getDriver(driverId);
//                    if (driver == null) {
//                        halt(404);
//                    }
//                    final String orderIdString = request.params(":order_id");
//                    log.debug(
//                            "/driver/:driver_id/set_order_status_picked_up/:order_id<{}>",
//                            orderIdString);
//                    final ObjectId orderId = new ObjectId(orderIdString);
//                    Order order = orderController.getOrder(orderId);
//                    if (order == null) {
//                        halt(404);
//                        return "No such order";
//                    }
//                    if (!driver.getCurrOrders().contains(order)) {
////                        response.type("application/json");
//                        response.status(400);
//                        return "Current driver doesn't have such order in current order list";
//                    }
//                    OrderController.setOrderStatusToPickedUp(order);
////                    response.type("application/json");
//                    response.status(200);
//                    return response;
//                });

        delete( // delete a driver
                "/driver/delete_a_driver/:driver_id",
                (request, response) -> {
                    final String paramId = request.params(":driver_id");
                    log.debug("/driver/delete_a_driver/:driver_id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Driver driver = driverController.getDriver(id);
                    if (driver == null) {
                        halt(404);
                        return "No such driver";
                    }
                    driverController.deleteDriver(id);
                    response.status(200);
                    return response;
                });
    }
}
