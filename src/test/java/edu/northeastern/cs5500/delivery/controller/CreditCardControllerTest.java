package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.Address;
import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreditCardControllerTest {
    private InMemoryRepository<CreditCard> inMemoryRepository = new InMemoryRepository<>();
    private CreditCardController creditCardController = new CreditCardController(inMemoryRepository);
    private CreditCard creditCard1 = new CreditCard(), creditCard2 = new CreditCard();
    @BeforeEach
    void setUp() {
        creditCard1.setId(ObjectId.get());
        creditCard1.setCardNumber("4047800097778999");
        creditCard1.setFirstName("firstName1");
        creditCard1.setLastName("lastName1");
        creditCard1.setSecurityCode("345");
        creditCard1.setExpDate("2024-12-23");
        Address billingAddress1 = new Address();
        billingAddress1.setId(ObjectId.get());
        billingAddress1.setAddress1("address1");
        billingAddress1.setCity("city1");
        billingAddress1.setState("state1");
        billingAddress1.setZip("zip1");
        creditCard1.setBillingAddress(billingAddress1);
        creditCard2.setId(ObjectId.get());
        creditCard2.setCardNumber("4047800091478999");
        creditCard2.setFirstName("firstName2");
        creditCard2.setLastName("lastName2");
        creditCard2.setSecurityCode("000");
        creditCard1.setExpDate("2022-02-23");
        Address billingAddress2 = new Address();
        billingAddress2.setId(ObjectId.get());
        billingAddress2.setAddress1("address2");
        billingAddress2.setCity("city2");
        billingAddress2.setState("state2");
        billingAddress2.setZip("zip2");
        inMemoryRepository.add(creditCard1);
        inMemoryRepository.add(creditCard2);
    }

    @Test
    void getCreditCard() {
        assertNotNull(creditCardController.getCreditCard(creditCard1.getId()));
    }

    @Test
    void getCreditCards() {
        assertEquals(2, creditCardController.getCreditCards().size());
    }

    @Test
    void addCreditCard() throws Exception {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(ObjectId.get());
        creditCard.setCardNumber("4047800097234599");
        creditCard.setFirstName("firstName");
        creditCard.setLastName("lastName");
        creditCard.setSecurityCode("345");
        creditCard.setExpDate("2024-10-23");
        Address billingAddress = new Address();
        billingAddress.setId(ObjectId.get());
        billingAddress.setAddress1("address");
        billingAddress.setCity("city");
        billingAddress.setState("state");
        billingAddress.setZip("zip");
        creditCard.setBillingAddress(billingAddress);
        creditCardController.addCreditCard(creditCard);
        assertTrue(creditCardController.getCreditCards().contains(creditCard));
    }

    @Test
    void addCreditCardThrowsCreditCardInvalidException() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(ObjectId.get());
        assertThrows(
            Exception.class,
            () -> {
                creditCardController.addCreditCard(creditCard);
            },
            "InvalidCreditCardException"
        );
    }

    @Test
    void addCreditCardThrowsCreditCardNumberException() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(ObjectId.get());
        creditCard.setCardNumber("123");
        creditCard.setFirstName("firstName");
        creditCard.setLastName("lastName");
        creditCard.setSecurityCode("345");
        creditCard.setExpDate("2024-10-23");
        Address billingAddress = new Address();
        billingAddress.setId(ObjectId.get());
        billingAddress.setAddress1("address");
        billingAddress.setCity("city");
        billingAddress.setState("state");
        billingAddress.setZip("zip");
        creditCard.setBillingAddress(billingAddress);
        assertThrows(
            Exception.class,
            () -> {
                creditCardController.addCreditCard(creditCard);
            },
            "InvalidCreditCardException"
        );
    }


    @Test
    void addCreditCardThrowsExpDateException() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(ObjectId.get());
        creditCard.setCardNumber("4047800097234599");
        creditCard.setFirstName("firstName");
        creditCard.setLastName("lastName");
        creditCard.setSecurityCode("345");
        creditCard.setExpDate("2017-10-23");
        Address billingAddress = new Address();
        billingAddress.setId(ObjectId.get());
        billingAddress.setAddress1("address");
        billingAddress.setCity("city");
        billingAddress.setState("state");
        billingAddress.setZip("zip");
        creditCard.setBillingAddress(billingAddress);
        assertThrows(
            Exception.class,
            () -> {
                creditCardController.addCreditCard(creditCard);
            },
            "InvalidCreditCardException"
        );
    }

    @Test
    void addCreditCardThrowsSecurityCodeException() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(ObjectId.get());
        creditCard.setCardNumber("4047800097234599");
        creditCard.setFirstName("firstName");
        creditCard.setLastName("lastName");
        creditCard.setSecurityCode("1");
        creditCard.setExpDate("2025-10-23");
        Address billingAddress = new Address();
        billingAddress.setId(ObjectId.get());
        billingAddress.setAddress1("address");
        billingAddress.setCity("city");
        billingAddress.setState("state");
        billingAddress.setZip("zip");
        creditCard.setBillingAddress(billingAddress);
        assertThrows(
            Exception.class,
            () -> {
                creditCardController.addCreditCard(creditCard);
            },
            "InvalidCreditCardException"
        );
    }

    @Test
    void updateCreditCard() throws Exception {
        creditCard1.setLastName("Smith");
        creditCardController.updateCreditCard(creditCard1);
        assertEquals("Smith", creditCardController.getCreditCard(creditCard1.getId()).getLastName());
    }

    @Test
    void deleteCreditCard() throws Exception {
        creditCardController.deleteCreditCard(creditCard2.getId());
        assertFalse(creditCardController.getCreditCards().contains(creditCard2));
        assertTrue(creditCardController.getCreditCards().contains(creditCard1));
    }
}
