package edu.northeastern.cs5500.delivery.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreditCardControllerTest {
    private InMemoryRepository<CreditCard> inMemoryRepository = new InMemoryRepository<>();

    @BeforeEach
    void setUp() {}

    @Test
    void getCreditCard() {}

    @Test
    void getCreditCards() {}

    @Test
    void addCreditCard() {}

    @Test
    void updateCreditCard() {}

    @Test
    void deleteCreditCard() {}
}
