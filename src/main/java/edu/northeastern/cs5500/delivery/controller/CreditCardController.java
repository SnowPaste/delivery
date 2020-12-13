package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalDate;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CreditCardController {

    private final GenericRepository<CreditCard> creditCards;
    private static final int CREDIT_CARD_NUMBER_LENGTH = 16;
    private static final int SECURITY_CODE_LENGTH = 3;

    @Inject
    public CreditCardController(GenericRepository<CreditCard> creditCardRepository) {
        creditCards = creditCardRepository;
        log.info("CreditCardController > construct");
    }

    @Nullable
    public CreditCard getCreditCard(@Nonnull ObjectId uuid) {
        log.debug("CreditCardController > getCreditCard({})", uuid);
        return creditCards.get(uuid);
    }

    @Nonnull
    public Collection<CreditCard> getCreditCards() {
        log.debug("CreditCardController > getCreditCards()");
        return creditCards.getAll();
    }

    @Nonnull
    public CreditCard addCreditCard(@Nonnull CreditCard creditCard) throws Exception {
        log.debug("CreditCardController > addCreditCard(...)");
        if (!creditCard.isValid()
                && isCreditCardNumberValid(creditCard)
                && isExpDateValid(creditCard)
                && isSecurityCodeValid(creditCard)) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidCreditCardException");
        }

        ObjectId id = creditCard.getId();

        if (id != null && creditCards.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return creditCards.add(creditCard);
    }

    public void updateCreditCard(@Nonnull CreditCard creditCard) throws Exception {
        log.debug("CreditCardController > updateCreditCard(...)");
        creditCards.update(creditCard);
    }

    public void deleteCreditCard(@Nonnull ObjectId id) throws Exception {
        log.debug("CreditCardController > deleteDelivery(...)");
        creditCards.delete(id);
    }

    /**
     * Method to check if credit card number provided is valid or not
     *
     * @return true if card number contains only numbers and length of card number equals to 16.
     *     Otherwise return false
     */
    private boolean isCreditCardNumberValid(CreditCard creditCard) {
        return creditCard.getCardNumber().matches("[0-9]+")
                && creditCard.getCardNumber().length() == CREDIT_CARD_NUMBER_LENGTH;
    }

    /**
     * Method to check if security code provided is valid or not
     *
     * @return true if security code contains only numbers and length of security code is 3
     */
    private boolean isSecurityCodeValid(CreditCard creditCard) {
        return creditCard.getSecurityCode().matches("[0-9]+")
                && creditCard.getSecurityCode().length() == SECURITY_CODE_LENGTH;
    }

    /**
     * Method to check if credit card is valid at the current time
     *
     * @return true if the credit card does not expire
     */
    private boolean isExpDateValid(CreditCard creditCard) {
        LocalDate expDate = LocalDate.parse(creditCard.getExpDate());
        LocalDate now = LocalDate.now();
        return expDate.compareTo(now) > 0;
    }
}
