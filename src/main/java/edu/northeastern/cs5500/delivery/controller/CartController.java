package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CartController {
    private final GenericRepository<Cart> carts;
    private final GenericRepository<Dish> dishes;
    private final Double DEFAULT_TIP_RATE = 0.2;
    private final Double INIT_PRICE = 0.0;
    private final Double INIT_TIP = 0.0;

    @Inject
    public CartController(
            GenericRepository<Cart> cartRepository, GenericRepository<Dish> dishRepository) {
        carts = cartRepository;
        dishes = dishRepository;

        log.info("CartController > construct");

        if (carts.count() > 0) {
            return;
        }

        log.info("CartController > construct > adding default carts");

        final Cart defaultCart1 = new Cart();
        final Customer defaultCustomer = new Customer();
        defaultCustomer.setFirstName("Cindy");
        defaultCustomer.setEmail("cindy@mail.com");
        defaultCustomer.setPassWord("12345");
        defaultCart1.setCustomer(defaultCustomer);

        try {
            addCart(defaultCart1);
        } catch (Exception e) {
            log.error("CartController > construct > adding default carts > failure?");
            e.printStackTrace();
        }
    }

    @Nonnull
    public Cart getCart(@Nonnull ObjectId uuid) {
        log.debug("CartController > getCart({})", uuid);
        return carts.get(uuid);
    }

    @Nonnull
    public Collection<Cart> getCarts() {
        log.debug("CartController > getCarts()");
        return carts.getAll();
    }

    @Nonnull
    public Cart addCart(@Nonnull Cart cart) throws Exception {
        log.debug("CartController > addCart(...)");
        if (!cart.isValid()) {
            throw new Exception("Invalid Cart");
        }

        ObjectId id = cart.getId();

        if (id != null && carts.get(id) != null) {
            throw new Exception("Cart already exists");
        }

        return carts.add(cart);
    }

    public void updateCart(@Nonnull Cart cart) throws Exception {
        log.debug("CartController > updateCart(...)");
        if (carts.get(cart.getId()) == null) {
            throw new Exception("Cart doesn't exist, please addCart first");
        }
        carts.update(cart);
    }

    public void deleteCart(@Nonnull ObjectId id) throws Exception {
        log.debug("CartController > deleteCart(...)");
        if (carts.get(id) == null) {
            throw new Exception("Cart doesn't exists");
        }
        carts.delete(id);
    }

    public ObjectId getCartId(@Nonnull Cart cart) throws Exception {
        log.debug("CartController > getCartId(...)");
        ObjectId id = cart.getId();
        if (id == null || carts.get(id) == null) {
            throw new Exception("Cart not found");
        }
        return id;
    }

    public Customer getCartOwner(@Nonnull Cart cart) throws Exception {
        log.debug("CartController > getCartOwner(...)");
        return cart.getCustomer();
    }

    public ArrayList<Dish> getCartItems(@Nonnull Cart cart) throws Exception {
        log.debug("CartController > getCartItems(...)");
        return cart.getItems();
    }

    @Nonnull
    public Double getCartTip(@Nonnull Cart cart) throws Exception {
        log.debug("CartController > getCartTip(...)");
        if (cart.getTip() == null) {
            cart.setTip(calculateDefaultTip(cart));
        }
        return cart.getTip();
    }

    @Nonnull
    public Double getCartTotalPrice(@Nonnull Cart cart) throws Exception {
        log.debug("CartController > getCartTotalPrice(...)");
        calculateTotalPrice(cart);
        return cart.getTotalPrice();
    }

    private Double calculateRawPrice(@Nonnull Cart cart) {
        log.debug("CartController > calculateRawPrice(...)");
        Double total = INIT_PRICE;
        ArrayList<Dish> items = cart.getItems();
        if (!items.isEmpty()) {
            for (Dish dish : items) {
                total += dish.getPrice();
            }
        }
        return total;
    }

    private Double calculateDefaultTip(@Nonnull Cart cart) {
        Double rawPrice = calculateRawPrice(cart);
        if (rawPrice == INIT_PRICE) {
            return INIT_TIP;
        }
        return rawPrice * DEFAULT_TIP_RATE;
    }

    private void calculateTotalPrice(@Nonnull Cart cart) {
        log.debug("CartController > calculateTotalPrice(...)");
        Double total = calculateRawPrice(cart);
        if (cart.getTip() == null) {
            Double tip = calculateDefaultTip(cart);
            total += tip;
        } else {
            total += cart.getTip();
        }
        cart.setTotalPrice(total);
    }

    public void setCartTip(@Nonnull Cart cart, Double tip) {
        log.debug("CartController > setCartTip({})", tip);
        if (tip == null) {
            cart.setTip(calculateDefaultTip(cart));
            return;
        }
        cart.setTip(tip);
    }

    public void addDish(@Nonnull Dish dish, @Nonnull Cart cart) throws Exception {
        log.debug("CartController > addDish({})", dish.getId());
        if (dishes.get(dish.getId()) == null) {
            throw new Exception("Dish doesn't exists");
        }
        if (carts.get(cart.getId()) == null || cart.getId() == null) {
            throw new Exception("Cart doesn't exists");
        }

        cart.getItems().add(dish);
        updateCart(cart);
        System.out.println("Dish " + dish.getName() + "is added");
    }

    public void removeDish(@Nonnull Dish dish, @Nonnull Cart cart) throws Exception {
        log.debug("CartController > removeDish({})", dish.getId());
        if (dishes.get(dish.getId()) == null) {
            throw new Exception("Dish doesn't exists");
        }
        if (carts.get(cart.getId()) == null || cart.getId() == null) {
            throw new Exception("Cart doesn't exists");
        }

        if (!cart.getItems().contains(dish)) {
            throw new Exception("Dish is not in the Cart");
        }

        cart.getItems().remove(dish);
        updateCart(cart);
        System.out.println("Dish " + dish.getName() + "is removed");
    }

    public void emptyCart(@Nonnull Cart cart) throws Exception {
        cart.setItems(new ArrayList<Dish>());
        updateCart(cart);
        System.out.println("Cart has been cleared");
    }
}
