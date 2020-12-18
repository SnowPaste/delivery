package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.model.Order.Status;
import edu.northeastern.cs5500.delivery.repository.*;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private static final RepositoryModule repositoryModule = new RepositoryModule();
    private static final MongoDBService mongoDBService = new MongoDBService();

    @Inject
    DriverController driverController =
            new DriverController(repositoryModule.provideDriverRepository(mongoDBService));

    @Inject
    RestaurantController restaurantController =
            new RestaurantController(repositoryModule.provideRestaurantRepository(mongoDBService));

    @Inject
    CustomerController customerController =
            new CustomerController(repositoryModule.provideCustomerRepository(mongoDBService));

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
        final Cart defaultCart = defaultCustomer.getCart();
        final Restaurant defaultRestaurant =
                restaurantController.getRestaurants().iterator().next();
        final Dish defaultDish = defaultRestaurant.getMenu().get(0);
        final Driver defaultDriver = driverController.getDrivers().iterator().next();
        defaultCustomer.setFirstName("John");
        defaultCustomer.setLastName("Doe");
        ArrayList<Dish> select = new ArrayList<>();
        select.add(defaultDish);
        defaultCart.setItems(select);
        defaultOrder.setCreateTime(LocalDateTime.now());
        defaultOrder.setCustomer(defaultCustomer);
        defaultOrder.setCart(defaultCart);
        defaultOrder.setRestaurant(defaultRestaurant);
        defaultOrder.setDriver(defaultDriver);
        defaultOrder.setEstDeliverTime(defaultOrder.getCreateTime().plusHours(1));
        defaultOrder.setStatus(Status.WAITING_FOR_DRIVER);

        final Order defaultOrder1 = new Order();
        final Customer defaultCustomer1 = new Customer();
        final Cart defaultCart1 = defaultCustomer.getCart();
        final Restaurant defaultRestaurant1 =
                restaurantController.getRestaurants().iterator().next();
        final Dish defaultDish1 = defaultRestaurant.getMenu().get(0);
        final Driver defaultDriver1 = driverController.getDrivers().iterator().next();
        defaultCustomer1.setFirstName("Jane");
        defaultCustomer1.setLastName("Doe");
        ArrayList<Dish> select1 = new ArrayList<>();
        select1.add(defaultDish1);
        defaultCart1.setItems(select1);
        defaultOrder1.setCreateTime(LocalDateTime.now());
        defaultOrder1.setCustomer(defaultCustomer1);
        defaultOrder1.setCart(defaultCart1);
        defaultOrder1.setRestaurant(defaultRestaurant1);
        defaultOrder1.setDriver(defaultDriver1);
        defaultOrder1.setEstDeliverTime(defaultOrder1.getCreateTime().plusHours(1));
        defaultOrder1.setStatus(Status.PROCESSING);

        final Order defaultOrder2 = new Order();
        final Customer defaultCustomer2 = new Customer();
        final Cart defaultCart2 = defaultCustomer.getCart();
        final Restaurant defaultRestaurant2 =
                restaurantController.getRestaurants().iterator().next();
        final Dish defaultDish2 = defaultRestaurant.getMenu().get(0);
        final Driver defaultDriver2 = driverController.getDrivers().iterator().next();
        defaultCustomer2.setFirstName("June");
        defaultCustomer2.setLastName("Doe");
        ArrayList<Dish> select2 = new ArrayList<>();
        select2.add(defaultDish2);
        defaultCart2.setItems(select2);
        defaultOrder2.setCreateTime(LocalDateTime.now());
        defaultOrder2.setCustomer(defaultCustomer2);
        defaultOrder2.setCart(defaultCart2);
        defaultOrder2.setRestaurant(defaultRestaurant2);
        defaultOrder2.setDriver(defaultDriver2);
        defaultOrder2.setEstDeliverTime(defaultOrder2.getCreateTime().plusHours(1));
        defaultOrder2.setStatus(Status.PREPARING);

        final Order defaultOrder3 = new Order();
        final Customer defaultCustomer3 = new Customer();
        final Cart defaultCart3 = defaultCustomer.getCart();
        final Restaurant defaultRestaurant3 =
                restaurantController.getRestaurants().iterator().next();
        final Dish defaultDish3 = defaultRestaurant.getMenu().get(0);
        final Driver defaultDriver3 = driverController.getDrivers().iterator().next();
        defaultCustomer3.setFirstName("Julia");
        defaultCustomer3.setLastName("Doe");
        ArrayList<Dish> select3 = new ArrayList<>();
        select3.add(defaultDish3);
        defaultCart3.setItems(select3);
        defaultOrder3.setCreateTime(LocalDateTime.now());
        defaultOrder3.setCustomer(defaultCustomer3);
        defaultOrder3.setCart(defaultCart3);
        defaultOrder3.setRestaurant(defaultRestaurant3);
        defaultOrder3.setDriver(defaultDriver3);
        defaultOrder3.setEstDeliverTime(defaultOrder3.getCreateTime().plusHours(1));
        defaultOrder3.setStatus(Status.PICKED_UP);

        try {
            addOrder(defaultOrder);
            addOrder(defaultOrder1);
            addOrder(defaultOrder2);
            addOrder(defaultOrder3);
        } catch (Exception e) {
            log.error("OrderController > construct > adding default order > failure>");
            e.printStackTrace();
        }
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

    public static Status getOrderStatus(@Nonnull Order order) {
        return order.getStatus();
    }

    /** Calculate the order est deliver time */
    public LocalDateTime getOrderEstDeliverTime(@Nonnull Order order) throws Exception {
        if (order.getCreateTime() == null) {
            throw new Exception("Your order has not been created yet...");
        }
        if (order.getEstDeliverTime() == null) {
            LocalDateTime est = order.getCreateTime().plusHours(1);
            order.setEstDeliverTime(est);
        }
        System.out.println(
                "The estimated deliver time is "
                        + order.getEstDeliverTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        updateOrder(order);
        return order.getEstDeliverTime();
    }

    /** Set order status to PROCESSING */
    public void setOrderStatusToProcessing(@Nonnull Order order) {
        order.setStatus(Status.PROCESSING);
        System.out.println("Your order is being processed...");
    }

    /** Set order status to PREPARING */
    public static void setOrderStatusToPreparing(@Nonnull Order order) {
        order.setStatus(Status.PREPARING);
        System.out.println(
                "Your order is being prepared by " + order.getRestaurant().getName() + " ...");
    }

    /** Set order status to WAITING_FOR_DRIVER */
    public static void setOrderStatusToWaiting(@Nonnull Order order) {
        order.setStatus(Status.WAITING_FOR_DRIVER);
        System.out.println("Your order is ready and waiting for driver...");
    }

    /** Set order status to PICKED_UP */
    public static void setOrderStatusToPickedUp(@Nonnull Order order) {
        order.setStatus(Status.PICKED_UP);
        System.out.println("Your order has been picked up by driver...");
    }

    /** Set order status to DELIVERED */
    public static void setOrderStatusToDelivered(@Nonnull Order order) {
        order.setStatus(Status.DELIVERED);
    }

    /** Set order status to CANCELLED */
    public static void setOrderStatusToCancelled(@Nonnull Order order) {
        order.setStatus(Status.CANCELLED);
        System.out.println("Your order has been cancelled...");
    }

    /** Assign the given driver to the order */
    public void setOrderDriver(@Nonnull Order order, @Nonnull Driver driver) {
        order.setDriver(driver);
        System.out.println(
                "Assigned driver " + driver.getFirstName() + " for order " + order.getId());
        ;
    }

    /** Find an available driver for the order */
    private void assignDriver(@Nonnull Order order) throws Exception {
        Driver assigned_driver = null;
        for (Driver d : driverController.getDrivers()) {
            if (d.isAvailable()) {
                assigned_driver = d;
                break;
            }
        }
        if (assigned_driver != null) {
            setOrderDriver(order, assigned_driver);
        } else {
            throw new Exception("No available driver");
        }
    }

    /** Place an order with the given cart */
    public Order makeOrder(@Nonnull Customer customer, @Nonnull Restaurant restaurant)
            throws Exception {
        try {
            Order order = new Order();
            order.setRestaurant(restaurant);
            order.setCart(customer.getCart());
            order.setCustomer(customer);

            order.setCreateTime(LocalDateTime.now(ZoneId.of("America/Los_Angeles")));
            System.out.println(
                    "Order was placed at "
                            + order.getCreateTime().format(DateTimeFormatter.ofPattern("HH:mm")));

            getOrderEstDeliverTime(order);
            assignDriver(order);
            System.out.println("Order placed successfully!");
            setOrderStatusToProcessing(order);
            addOrder(order);
            customer.setCart(new Cart());
            customerController.updateCustomer(customer);
            return order;
        } catch (Exception e) {
            log.error("OrderController > makeOrder failure");
            e.printStackTrace();
        }
        return null;
    }

    /** Cancel order */
    public void cancelOrder(@Nonnull Order order) throws Exception {
        if (order.getStatus() != Status.PROCESSING) {
            throw new Exception(
                    "The restaurant has started preparing your order, your order can't be cancelled");
        } else {
            setOrderStatusToCancelled(order);
            updateOrder(order);
            System.out.println("Your order has been cancelled");
        }
    }

    public void completeOrder(@Nonnull Order order) throws Exception {
        driverController.manageCompletedOrder(order.getDriver(), order);
        order.setDeliverTime(LocalDateTime.now(ZoneId.of("America/Los_Angeles")));
        Customer customer = order.getCustomer();
        ArrayList<ObjectId> orderHistory = customer.getOrderHistory();
        orderHistory.add(order.getId());
        customer.setOrderHistory(orderHistory);
        customerController.updateCustomer(customer);
        setOrderStatusToDelivered(order);
        notifyCustomer(order);
        updateOrder(order);
    }

    private void notifyCustomer(@Nonnull Order order) {
        System.out.println("Your order has been delivered! Enjoy:)");
    }
}
