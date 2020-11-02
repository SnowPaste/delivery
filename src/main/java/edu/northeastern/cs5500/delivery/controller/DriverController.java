package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Driver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
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
public class DriverController {

    private final GenericRepository<Driver> drivers;

    @Inject
    DriverController(GenericRepository<Driver> driverRepository) {
        drivers = driverRepository;

        log.info("DriverController > construct");

        final Driver defaultDriver = new Driver();
        defaultDriver.setFirstName("Jane");
        defaultDriver.setLastName("Doe");
        defaultDriver.setDriverID("123asdf");
        defaultDriver.setAvailable(true);

        try {
            addDriver(defaultDriver);
        } catch (Exception e) {
            log.error("DriverController > construct > adding default driver failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Driver getDriver(@Nonnull ObjectId uuid) {
        log.debug("DriverController > getDriver({})", uuid);
        return drivers.get(uuid);
    }

    @Nonnull
    public Collection<Driver> getDrivers() {
        log.debug("DriverController > getDrivers()");
        return drivers.getAll();
    }

    @Nonnull
    public Driver addDriver(@Nonnull Driver driver) throws Exception {
        log.debug("DriverController > addDriver(...)");
        if (!driver.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidDriverException");
        }

        ObjectId id = driver.getId();

        if (id != null && drivers.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return drivers.add(driver);
    }

    public void updateDriver(@Nonnull Driver driver) throws Exception {
        log.debug("DriverController > updateDriver(...)");
        drivers.update(driver);
    }

    public void deleteDriver(@Nonnull ObjectId id) throws Exception {
        log.debug("DriverController > deleteDriver(...)");
        drivers.delete(id);
    }

    /**
     * Method to add another order to the driver's currOrder
     *
     * @param order the order to be taken
     * @param driver the driver object
     * @return true if take the order successfully, otherwise false
     * @throws Exception when any of the two inputs is null
     */
    public boolean takeAnOrder(@Nonnull Order order, @Nonnull Driver driver) throws Exception {
        log.debug("DriverController > takeAnOrder(...)");
        if (canTakeMoreOrder(driver)) {
            ArrayList<Order> orderList = driver.getCurrOrders();
            orderList.add(order);
            driver.setCurrOrders(orderList);
            OrderController.setOrderStatusToPickedUp(order);
            if (driver.getCurrOrders().size() < driver.getMaxOrderNum()) {
                driver.setAvailable(true);
            } else {
                driver.setAvailable(false);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean canTakeMoreOrder(Driver driver) {
        // check if the number of the orders taken by the driver has exceed capacity
        return driver.isAvailable();
    }

    /**
     * Method to pop a completed order from currOrder and add it to the completedOrder
     *
     * @param driver the driver object which is responsible for the input order
     * @param order an order which is completed
     * @throws Exception when any of the two inputs is null
     */
    public void manageCompletedOrder(@Nonnull Driver driver, @Nonnull Order order)
            throws Exception {
        log.debug("DriverController > popOutCompletedOrder(...)");

        driver.getCurrOrders().remove(order);
        driver.getCompleteOrders().add(order);
        if (driver.getCurrOrders().size() < driver.getMaxOrderNum()) {
            driver.setAvailable(true);
        } else {
            driver.setAvailable(false);
        }
    }
}
