package edu.northeastern.cs5500.delivery.controller;

import static org.junit.Assert.*;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.junit.Before;
import org.junit.Test;

public class AddressControllerTest {
    private InMemoryRepository<Customer> inMemoryRepository = new InMemoryRepository<>();

    @Before
    public void setUp() throws Exception {}

    @Test
    public void getAddress() {}

    @Test
    public void getAddresses() {}

    @Test
    public void addAddress() {}

    @Test
    public void updateAddress() {}

    @Test
    public void deleteAddress() {}
}
