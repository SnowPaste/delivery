package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;

import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.CustomerController;
import edu.northeastern.cs5500.delivery.controller.OrderController;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Order;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class OrderView implements View {

    @Inject
    OrderView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject OrderController orderController;

    @Inject CustomerController customerController;

    @Override
    public void register() {
        log.info("OrderView > register");

        get( // get the information of a certain order
                "/order/get_an_order/:order_id",
                (request, response) -> {
                    final String orderIdString = request.params(":order_id");
                    log.debug("/order/get_an_order/:order_id<{}>", orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
                    if (order == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return order;
                },
                jsonTransformer);

        get( // get the current status of an order
                "/order/get_order_status/:order_id",
                (request, response) -> {
                    final String orderIdString = request.params(":order_id");
                    log.debug("/order/get_order_status/:order_id<{}>", orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
                    if (order == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return order.getStatus().toString();
                },
                jsonTransformer);

        get( // get all orders of a user
                "/order/get_orders/:user_id",
                (request, response) -> {
                    final String userIdString = request.params(":user_id");
                    log.debug("/order/get_orders/:user_id<{}>", userIdString);
                    final ObjectId userId = new ObjectId(userIdString);
                    Customer customer = customerController.getCustomer(userId);
                    if (customer == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    response.status(200);
                    return customer.getOrderHistory();
                },
                jsonTransformer);

        get( // get the estimated deliver time of an order
                "/order/:order_id/get_order_est_deliver_time",
                (request, response) -> {
                    final String orderIdString = request.params(":order_id");
                    log.debug("/order/:order_id<{}>/get_order_est_deliver_time", orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
                    if (order == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return order.getEstDeliverTime().toString();
                },
                jsonTransformer);

        delete( // delete an order
                "/order/delete_order/:order_id",
                (request, response) -> {
                    final String orderIdString = request.params(":order_id");
                    log.debug("/order/delete_order/:order_id<{}>", orderIdString);
                    final ObjectId orderId = new ObjectId(orderIdString);
                    Order order = orderController.getOrder(orderId);
                    if (order == null) {
                        halt(404);
                    }
                    orderController.deleteOrder(orderId);
                    response.status(200);
                    return response;
                });
    }
}
