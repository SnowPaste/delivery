package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.*;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
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
public class CustomerController {
    private final GenericRepository<Customer> customers;
    private static final RepositoryModule repositoryModule = new RepositoryModule();
    private static final MongoDBService mongoDBService = new MongoDBService();

    @Inject
    AddressController addressController =
            new AddressController(repositoryModule.provideAddressRepository(mongoDBService));

    @Inject
    CreditCardController cardController =
            new CreditCardController(repositoryModule.provideCreditCardRepository(mongoDBService));

    @Inject
    CartController cartController =
            new CartController(repositoryModule.provideCartRepository(mongoDBService));

    @Inject
    public CustomerController(GenericRepository<Customer> customerRepository) {
        customers = customerRepository;

        log.info("CustomerController > construct");

        if (customers.count() > 0) {
            return;
        }
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
            throw new Exception("InvalidCustomerException");
        }

        ObjectId id = customer.getId();
        String accountName = customer.getAccountName();
        Boolean exists =
                getCustomers().stream().anyMatch(x -> x.getAccountName().equals(accountName));

        if (customers.get(id) != null || exists) {
            throw new Exception("Customer already exists in database");
        }

        cartController.addCart(customer.getCart());

        return customers.add(customer);
    }

    public Customer updateCustomer(@Nonnull Customer customer) throws Exception {
        log.debug("CustomerController > updateCustomer(...)");
        return customers.update(customer);
    }

    public void deleteCustomer(@Nonnull ObjectId id) throws Exception {
        log.debug("CustomerController > deleteCustomer(...)");
        customers.delete(id);
    }

    public Address addAddress(@Nonnull Customer customer, @Nonnull Address address)
            throws Exception {
        log.debug("CustomerController > addAddress(...)");
        addressController.addAddress(address);
        ArrayList<Address> newAddressList = customer.getAddressList();
        newAddressList.add(address);
        customer.setAddressList(newAddressList);
        updateCustomer(customer);
        return address;
    }

    public Address updateAddress(@Nonnull Customer customer, @Nonnull Address address)
            throws Exception {
        log.debug("CustomerController > updateAddress(...)");
        ArrayList<Address> newAddressList = customer.getAddressList();
        newAddressList.removeIf(x -> x.getId().equals(address.getId()));
        newAddressList.add(address);
        customer.setAddressList(newAddressList);
        addressController.updateAddress(address);
        updateCustomer(customer);
        return address;
    }

    public void deleteAddress(@Nonnull Customer customer, @Nonnull ObjectId address_id)
            throws Exception {
        log.debug("CustomerController > deleteAddress(...)");
        ArrayList<Address> newAddressList = customer.getAddressList();
        try {
            newAddressList.removeIf(x -> x.getId().equals(address_id));
        } catch (Exception e) {
        }
        customer.setAddressList(newAddressList);
        addressController.deleteAddress(address_id);
        updateCustomer(customer);
    }

    public CreditCard addCard(@Nonnull Customer customer, @Nonnull CreditCard card)
            throws Exception {
        log.debug("CustomerController > addCard(...)");
        cardController.addCreditCard(card);
        ArrayList<CreditCard> newCardList = customer.getCreditCards();
        newCardList.add(card);
        customer.setCreditCards(newCardList);
        updateCustomer(customer);
        return card;
    }

    public CreditCard updateCard(@Nonnull Customer customer, @Nonnull CreditCard card)
            throws Exception {
        log.debug("CustomerController > updateCreditCard(...)");
        ArrayList<CreditCard> newCardList = customer.getCreditCards();
        newCardList.removeIf(x -> x.getId().equals(card.getId()));
        newCardList.add(card);
        customer.setCreditCards(newCardList);
        cardController.updateCreditCard(card);
        updateCustomer(customer);
        return card;
    }

    public void deleteCard(@Nonnull Customer customer, @Nonnull ObjectId card_id) throws Exception {
        log.debug("CustomerController > deleteCard(...)");
        ArrayList<CreditCard> newCardList = customer.getCreditCards();
        try {
            newCardList.removeIf(x -> x.getId().equals(card_id));
        } catch (Exception e) {
        }
        customer.setCreditCards(newCardList);
        cardController.deleteCreditCard(card_id);
        updateCustomer(customer);
    }
}
