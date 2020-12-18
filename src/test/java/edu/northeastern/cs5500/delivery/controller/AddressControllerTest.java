package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Address;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressControllerTest {

    private InMemoryRepository<Address> inMemoryRepository = new InMemoryRepository<>();
    private ObjectId addressId1 = ObjectId.get(), addressId2 = ObjectId.get();
    private String addressString1 = "987 34th Ave NE", addressString2 = "76 Terry St. N";
    private String city1 = "Bellevue", city2 = "Seattle";
    private String state1 = "WA", state2 = "WA";
    private String zip1 = "98406", zip2 = "98109";
    private Address address1 = new Address(), address2 = new Address();
    AddressController addressController = new AddressController(inMemoryRepository);

    @BeforeEach
    public void setUp() throws Exception {
        address1.setId(addressId1);
        address1.setAddress1(addressString1);
        address1.setCity(city1);
        address1.setState(state1);
        address1.setZip(zip1);
        address2.setId(addressId2);
        address2.setAddress1(addressString2);
        address2.setCity(city2);
        address2.setState(state2);
        address2.setZip(zip2);
        inMemoryRepository.add(address1);
        inMemoryRepository.add(address2);
    }

    @Test
    public void getAddress() {
        assertEquals(addressId1, addressController.getAddress(addressId1).getId());
    }

    @Test
    public void getAddresses() {
        assertEquals(3, addressController.getAddresses().size());
    }

    @Test
    public void addAddress() throws Exception {
        Address address = new Address();
        address.setId(ObjectId.get());
        address.setAddress1("4545 12th Ave SE");
        address.setCity("Seattle");
        address.setState("WA");
        address.setZip(("98105"));
        addressController.addAddress(address);
        assertTrue(addressController.getAddresses().contains(address));
    }

    @Test
    public void addAddressThrowsInvalidException() throws Exception {
        Address address = new Address();
        address.setId(ObjectId.get());
        address.setAddress1("4545 12th Ave SE");
        assertThrows(
                Exception.class,
                () -> {
                    addressController.addAddress(address);
                },
                "Address is invalid");
    }

    @Test
    public void addAddressThrowsExistsException() throws Exception {
        assertThrows(
                Exception.class,
                () -> {
                    addressController.addAddress(address1);
                },
                "Address already exists");
    }

    @Test
    public void updateAddress() throws Exception {
        address1.setZip("98000");
        addressController.updateAddress(address1);
        assertEquals("98000", addressController.getAddress(addressId1).getZip());
        address1.setCity("Federal Way");
        assertEquals("Federal Way", addressController.getAddress(addressId1).getCity());
        address1.setState("CA");
        assertEquals("CA", addressController.getAddress(addressId1).getState());
        address1.setAddress2("APT 604");
        assertEquals("APT 604", addressController.getAddress(addressId1).getAddress2());
    }

    @Test
    public void deleteAddress() throws Exception {
        addressController.deleteAddress(addressId2);
        assertFalse(addressController.getAddresses().contains(address2));
        assertTrue(addressController.getAddresses().contains(address1));
    }
}
