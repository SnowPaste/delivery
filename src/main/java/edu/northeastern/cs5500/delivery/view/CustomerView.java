package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.*;
import edu.northeastern.cs5500.delivery.model.*;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CustomerView implements View {

    @Inject
    CustomerView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject CartController cartController;

    @Inject CustomerController customerController;

    @Inject AddressController addressController;

    @Inject CreditCardController cardController;

    @Inject DishController dishController;

    @Inject OrderController orderController;

    @Inject RestaurantController restaurantController;

    @Override
    public void register() {
        log.info("CustomerView > register");

        get(
                "/customer",
                (request, response) -> {
                    log.debug("/customer");
                    response.type("application/json");
                    return customerController.getCustomers();
                },
                jsonTransformer);

        get(
                "/customer/:customer_id",
                (request, response) -> {
                    final String param_id = request.params(":customer_id");
                    final ObjectId id = new ObjectId(param_id);
                    Customer customer = customerController.getCustomer(id);
                    System.out.println(customer);
                    if (customer == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return customer;
                },
                jsonTransformer);

        post(
                "/customer/add_customer",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Customer customer = mapper.readValue(request.body(), Customer.class);
                    if (!customer.isValid()) {
                        response.status(400);
                        return "";
                    }

                    customer.setId(null);
                    customer = customerController.addCustomer(customer);
                    response.redirect(
                            String.format("/customer/%s", customer.getId().toHexString()), 301);
                    return customer;
                });

        put(
                "/customer/:customer_id/update_customer",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    ObjectMapper mapper = new ObjectMapper();
                    Customer customer = mapper.readValue(request.body(), Customer.class);
                    if (!customer.isValid()) {
                        response.status(400);
                        return "";
                    }

                    customer.setId(customer_id);
                    customerController.updateCustomer(customer);
                    return customer;
                });

        delete(
                "/customer/:customer_id/delete_customer",
                (request, response) -> {
                    final String paramId = request.params(":customer_id");
                    final ObjectId id = new ObjectId(paramId);
                    Customer customer = customerController.getCustomer(id);

                    customerController.deleteCustomer(id);
                    return customer;
                });

        put(
                "/customer/:customer_id/add_address",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Address address = mapper.readValue(request.body(), Address.class);
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        halt(404);
                    }
                    customerController.addAddress(customer, address);
                    response.type("application/json");
                    return address;
                });

        put(
                "/customer/:customer_id/update_address/:address_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    final String address_param_id = request.params(":address_id");
                    final ObjectId address_id = new ObjectId(address_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    Address old_address = addressController.getAddress(address_id);
                    ObjectMapper mapper = new ObjectMapper();
                    Address address = mapper.readValue(request.body(), Address.class);
                    if (!address.isValid()) {
                        response.status(400);
                        return "";
                    }

                    address.setId(old_address.getId());
                    customerController.updateAddress(customer, address);
                    response.type("application/json");
                    return address;
                });

        delete(
                "/customer/:customer_id/delete_address/:address_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    final String address_param_id = request.params(":address_id");
                    final ObjectId address_id = new ObjectId(address_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    Address address = addressController.getAddress(address_id);
                    if (customer == null || address == null) {
                        halt(404);
                    }
                    customerController.deleteAddress(customer, address);
                    response.type("application/json");
                    return address;
                });

        put(
                "/customer/:customer_id/add_card",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    CreditCard card = mapper.readValue(request.body(), CreditCard.class);
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        halt(404);
                    }
                    customerController.addCard(customer, card);
                    response.type("application/json");
                    return card;
                });

        put(
                "/customer/:customer_id/update_card/:card_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    final String card_param_id = request.params(":card_id");
                    final ObjectId card_id = new ObjectId(card_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    CreditCard old_card = cardController.getCreditCard(card_id);
                    ObjectMapper mapper = new ObjectMapper();
                    CreditCard card = mapper.readValue(request.body(), CreditCard.class);
                    if (!card.isValid()) {
                        response.status(400);
                        return "";
                    }

                    card.setId(old_card.getId());
                    customerController.updateCard(customer, card);
                    response.type("application/json");
                    return card;
                });

        delete(
                "/customer/:customer_id/delete_card/:card_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    final String card_param_id = request.params(":card_id");
                    final ObjectId card_id = new ObjectId(card_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    CreditCard card = cardController.getCreditCard(card_id);
                    if (customer == null || card == null) {
                        halt(404);
                    }
                    customerController.deleteCard(customer, card);
                    response.type("application/json");
                    return card;
                });

        put(
                "/customer/:customer_id/add_dish/:dish_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    final String dish_param_id = request.params(":dish_id");
                    final ObjectId dish_id = new ObjectId(dish_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        response.status(400);
                        return "";
                    }
                    Dish dish = dishController.getDish(dish_id);
                    if (dish == null) {
                        response.status(400);
                        return "";
                    }

                    Cart cart = customer.getCart();
                    if (cart == null) {
                        cart = new Cart();
                        cartController.addCart(cart);
                    }

                    try {
                        Cart newCart = cartController.addDish(dish, cart);
                        customer.setCart(newCart);
                        customerController.updateCustomer(customer);
                        return newCart;
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                },
                jsonTransformer);

        put(
                "/customer/:customer_id/remove_dish/:dish_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    final String dish_param_id = request.params(":dish_id");
                    final ObjectId dish_id = new ObjectId(dish_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        response.status(400);
                        return "";
                    }
                    Dish dish = dishController.getDish(dish_id);
                    if (dish == null) {
                        response.status(400);
                        return "";
                    }

                    Cart cart = customer.getCart();
                    Cart newCart = cartController.removeDish(dish, cart);
                    customer.setCart(newCart);
                    customerController.updateCustomer(customer);
                    return newCart;
                },
                jsonTransformer);

        put(
                "/customer/:customer_id/cancel_order/:order_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    final String order_param_id = request.params(":order_id");
                    final ObjectId order_id = new ObjectId(order_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        response.status(400);
                        return "";
                    }
                    Order order = orderController.getOrder(order_id);
                    if (order == null) {
                        response.status(400);
                        return "";
                    }

                    orderController.cancelOrder(order);
                    ArrayList<ObjectId> history = customer.getOrderHistory();
                    history.add(order_id);
                    customer.setOrderHistory(history);
                    customerController.updateCustomer(customer);
                    return order;
                },
                jsonTransformer);

        put(
                "/customer/:customer_id/empty_cart",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        halt(404);
                    }
                    Cart cart = customer.getCart();
                    Cart newCart = cartController.emptyCart(cart);
                    customer.setCart(newCart);
                    System.out.println(customer.getCart());
                    customerController.updateCustomer(customer);
                    return newCart;
                });

        get(
                "/customer/:customer_id/get_cart",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        halt(404);
                    }
                    Cart cart = customer.getCart();
                    response.type("application/json");
                    return cart;
                },
                jsonTransformer);

        put(
                "/customer/:customer_id/set_tip",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    ObjectMapper mapper = new ObjectMapper();
                    Cart tempCart = mapper.readValue(request.body(), Cart.class);
                    Double tip = tempCart.getTip();
                    Customer customer = customerController.getCustomer(customer_id);
                    if (customer == null) {
                        halt(404);
                    }
                    Cart cart = customer.getCart();
                    Cart newCart = cartController.setCartTip(cart, tip);
                    customer.setCart(newCart);
                    customerController.updateCustomer(customer);
                    response.type("application/json");
                    return newCart;
                },
                jsonTransformer);

        post(
                "/customer/:customer_id/make_order/:restaurant_id",
                (request, response) -> {
                    final String customer_param_id = request.params(":customer_id");
                    final ObjectId customer_id = new ObjectId(customer_param_id);
                    Customer customer = customerController.getCustomer(customer_id);
                    final String restaurant_param_id = request.params(":restaurant_id");
                    final ObjectId restaurant_id = new ObjectId(restaurant_param_id);
                    Restaurant restaurant = restaurantController.getRestaurant(restaurant_id);
                    if (customer == null || restaurant == null) {
                        halt(404);
                    }
                    Cart cart = customer.getCart();
                    if (cart.getItems().size() == 0) {
                        response.status(400);
                        return "";
                    }

                    Order order = orderController.makeOrder(customer, restaurant);
                    orderController.addOrder(order);
                    response.type("application/json");
                    return order;
                },
                jsonTransformer);
    }
}
