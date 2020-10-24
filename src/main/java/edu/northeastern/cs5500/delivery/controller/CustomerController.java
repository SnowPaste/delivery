package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CustomerController {
  private final GenericRepository<Customer> customers;

  @Inject
  CustomerController(GenericRepository<Customer> customerRepository) {
    customers = customerRepository;

    log.info("CustomerController > construct");
  }

  @Nullable
  public Customer getCustomer(@Nonnull ObjectId uuid) {
    log.debug("CustomerController > getCustomer({})", uuid);
    return customers.get(uuid);
  }

  @Nonnull
  public Collection<Customer> getCustomers() {
    log.debug("CustomerController > getCustomers()");
    return customers.getAll();
  }

  @Nonnull
  public Customer addCustomer(@Nonnull Customer customer) throws Exception {
    log.debug("CustomerController > addCustomer(...)");
    if (!customer.isValid()) {
      // TODO: replace with a real invalid object exception
      // probably not one exception per object type though...
      throw new Exception("InvalidCustomerException");
    }

    ObjectId id = customer.getId();

    if (id != null && customers.get(id) != null) {
      // TODO: replace with a real duplicate key exception
      throw new Exception("DuplicateKeyException");
    }

    return customers.add(customer);
  }

  public void updateCustomer(@Nonnull Customer customer) throws Exception {
    log.debug("CustomerController > updateCustomer(...)");
    customers.update(customer);
  }

  public void deleteCustomer(@Nonnull ObjectId id) throws Exception {
    log.debug("CustomerController > deleteCustomer(...)");
    customers.delete(id);
  }

//  public void makeOrder(@Nonnull Customer customer) throws Exception {
//    log.debug("CustomerController > makeOrder(...)");
//    // TODO: Cart should have attribute "Restaurant"?
//    // TODO: restaurantController.acceptOrder()?
//    // TODO: Order should have a method to publish an order and ask for drivers?
//
//  }
}