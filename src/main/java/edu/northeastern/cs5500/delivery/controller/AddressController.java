package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.*;
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
public class AddressController {
    private final GenericRepository<Address> addresses;

    @Inject
    public AddressController(GenericRepository<Address> addressRepository) {
        addresses = addressRepository;

        log.info("AddressController > construct");

        if (addresses.count() > 0) {
            return;
        }

        log.info("AddressController > construct > adding default addresses");

        final Address defaultAddress = new Address();
        defaultAddress.setAddress1("123 Terry Ave");
        defaultAddress.setCity("Seattle");
        defaultAddress.setState("WA");
        defaultAddress.setZip("98109");

        try {
            addAddress(defaultAddress);
        } catch (Exception e) {
            log.error("AddressController > construct > adding default address > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Address getAddress(@Nonnull ObjectId uuid) {
        log.debug("AddressController > getAddress({})", uuid);
        return addresses.get(uuid);
    }

    @Nonnull
    public Collection<Address> getAddresses() {
        log.debug("AddressController > getAddresses()");
        return addresses.getAll();
    }

    @Nonnull
    public Address addAddress(@Nonnull Address address) throws Exception {
        log.debug("AddressController > addAddress(...)");
        if (!address.isValid()) {
            throw new Exception("Address is invalid");
        }
        ObjectId id = address.getId();

        if (id != null && addresses.get(id) != null) {
            throw new Exception("Address already exists");
        }

        return addresses.add(address);
    }

    public void updateAddress(@Nonnull Address address) throws Exception {
        log.debug("AddressController > updateAddress(...)");
        addresses.update(address);
    }

    public void deleteAddress(@Nonnull ObjectId id) throws Exception {
        log.debug("AddressController > deleteAddress(...)");
        addresses.delete(id);
    }
}
