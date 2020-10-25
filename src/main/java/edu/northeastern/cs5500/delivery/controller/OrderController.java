package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.model.Order.Status;
import edu.northeastern.cs5500.delivery.repository.*;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class OrderController {
    private final GenericRepository<Order> orders;
    private final RepositoryModule repositoryModule = new RepositoryModule();

    @Inject
    DriverController driverController =
            new DriverController(repositoryModule.provideDriverRepository());

    @Inject
    RestaurantController restaurantController =
            new RestaurantController(repositoryModule.provideRestaurantRepository());

    @Inject
    public OrderController(GenericRepository<Order> orderRepository) {
        orders = orderRepository;

        log.info("OrderController > construct");

        if (orders.count() > 0) {
            return;
        }

        log.info("OrderController > construct > adding default order");

        final Order defaultOrder = new Order();
        final Customer defaultCustomer = new Customer();
        final Cart defaultCart = new Cart();
        defaultCustomer.setFirstName("Jane");
        defaultCustomer.setLastName("Doe");
        defaultCart.setCustomer(defaultCustomer);
        defaultOrder.setCreateTime(LocalDateTime.now());
        defaultOrder.setCustomer(defaultCustomer);
        defaultOrder.setCart(defaultCart);

        // try {
        //     addOrder(defaultOrder);
        // } catch (Exception e) {
        //     log.error("OrderController > construct > adding default order > failure>");
        //     e.printStackTrace();
        // }
    }

    @Nullable
    public Order getOrder(@Nonnull ObjectId uuid) {
        log.debug("OrderController > getOrder({})", uuid);
        return orders.get(uuid);
    }

    @Nonnull
    public Collection<Order> getOrders() {
        log.debug("OrderController > getOrders()");
        return orders.getAll();
    }

    @Nonnull
    public Order addOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > addOrder(...)");
        if (!order.isValid()) {
            throw new Exception("Order is invalid");
        }
        ObjectId id = order.getId();
        if (id != null && orders.get(id) != null) {
            throw new Exception("Order already exists");
        }
        return orders.add(order);
    }

    public void updateOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > updateDelivery(...)");
        orders.update(order);
    }

    public void deleteOrder(@Nonnull ObjectId id) throws Exception {
        log.debug("OrderController > deleteOrder(...)");
        orders.delete(id);
    }

    public Status getOrderStatus(@Nonnull Order order) {
        return order.getStatus();
    }

    public LocalDateTime getOrderEstDeliverTime(@Nonnull Order order) throws Exception {
        LocalDateTime est = order.getCreateTime().plusHours(1);
        order.setEstDeliverTime(est);
        updateOrder(order);
        return order.getEstDeliverTime();
    }

    private void assignDriver(@Nonnull Order order) throws Exception {
        Driver assigned_driver = null;
        for (Driver d : driverController.getDrivers()) {
            if (d.isAvailable()) {
                assigned_driver = d;
                break;
            }
        }
        if (assigned_driver != null) {
            order.setDriver(assigned_driver);
            System.out.println(
                    "Assigned driver "
                            + assigned_driver.getFirstName()
                            + " for order "
                            + order.getId());
            // log.info("Assigned driver {} for order {}",
            // assigned_driver.getFirstName(),order.getId());
        } else {
            System.out.println("No available driver");
            throw new Exception("No available driver");
        }
    }

    private void notifyRestaurant(@Nonnull Order order) throws Exception {
        restaurantController.finishOrder(order);
    }

    public Order makeOrder(@Nonnull Cart cart) throws Exception {
        try {
            Order order = new Order();
            order.setRestaurant(cart.getRestaurant());
            order.setCart(cart);
            order.setCustomer(cart.getCustomer());

            order.setCreateTime(LocalDateTime.now());
            System.out.println(
                    "Order was placed at "
                            + order.getCreateTime().getHour()
                            + ":"
                            + order.getCreateTime().getMinute());
            getOrderEstDeliverTime(order);
            System.out.println(
                    "The estimated deliver time is "
                            + order.getEstDeliverTime().getHour()
                            + ":"
                            + order.getEstDeliverTime().getMinute());
            assignDriver(order);
            System.out.println(
                    "The driver assigned for this order is " + order.getDriver().getFirstName());
            order.setStatus(Status.PROCESSING);
            System.out.println("Order placed successfully!");
            System.out.println(
                    "Restaurant " + order.getRestaurant().getName() + " is preparing your order");
            notifyRestaurant(order);
            return order;
        } catch (Exception e) {
            log.error("OrderController > makeOrder failure");
            e.printStackTrace();
        }
        return null;
    }

    public void cancelOrder(@Nonnull Order order) throws Exception {
        if (order.getStatus() != Status.PROCESSING) {
            throw new Exception(
                    "The restaurant has started preparing your order, your order can't be cancelled");
        } else {
            order.setStatus(Status.CANCELLED);
            notifyRestaurant(order);
            driverController.manageCompletedOrder(order.getDriver(), order);
            System.out.println("Your order has been cancelled");
        }
    }

    public void completeOrder(@Nonnull Order order) throws Exception {
        driverController.manageCompletedOrder(order.getDriver(), order);
        notifyCustomer(order);
        System.out.println("The order has completed");
    }

    private void notifyCustomer(@Nonnull Order order) {
        System.out.println("Your order " + order.getId() + " has been delivered, enjoy!");
    }
}
