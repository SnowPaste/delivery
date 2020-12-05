package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.*;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import edu.northeastern.cs5500.delivery.repository.RepositoryModule;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
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
    private final RepositoryModule repositoryModule = new RepositoryModule();
    private static final MongoDBService mongoDBService = new MongoDBService();
    private final Double DEFAULT_TIP_RATE = 0.2;
    private final Double INIT_PRICE = 0.0;
    private final Double INIT_TIP = 0.0;

    @Inject
    DishController dishController =
            new DishController(repositoryModule.provideDishRepository(mongoDBService));

    @Inject
    public CartController(GenericRepository<Cart> cartRepository) {
        carts = cartRepository;

        log.info("CartController > construct");

        if (carts.count() > 0) {
            return;
        }

        // log.info("CartController > construct > adding default carts");

        // final Cart defaultCart1 = new Cart();
        // final Customer defaultCustomer = new Customer();
        // final Restaurant defaultRestaurant = new Restaurant();
        // defaultCustomer.setFirstName("Cindy");
        // defaultCustomer.setEmail("cindy@mail.com");
        // defaultCustomer.setPassWord("12345");
        // defaultCart1.setRestaurant(defaultRestaurant);

        // try {
        //     addCart(defaultCart1);
        // } catch (Exception e) {
        //     log.error("CartController > construct > adding default carts > failure?");
        //     e.printStackTrace();
        // }
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
        carts.update(cart);
    }

    public void deleteCart(@Nonnull ObjectId id) throws Exception {
        log.debug("CartController > deleteCart(...)");
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

    private Double calculateTotalPrice(@Nonnull Cart cart) {
        log.debug("CartController > calculateTotalPrice(...)");
        Double total = calculateRawPrice(cart);
        if (cart.getTip() == null) {
            Double tip = calculateDefaultTip(cart);
            total += tip;
        } else {
            total += cart.getTip();
        }
        cart.setTotalPrice(total);
        return total;
    }

    public Cart setCartTip(@Nonnull Cart cart, Double tip) throws Exception {
        log.debug("CartController > setCartTip({})", tip);
        if (tip == null) {
            cart.setTip(calculateDefaultTip(cart));
            return cart;
        }
        cart.setTip(tip);
        cart.setTotalPrice(calculateTotalPrice(cart));
        updateCart(cart);
        return cart;
    }

    public Cart addDish(@Nonnull Dish dish, @Nonnull Cart cart) throws Exception {
        log.debug("CartController > addDish({})", dish.getId());

        cart.getItems().add(dish);
        cart.setTotalPrice(calculateTotalPrice(cart));
        updateCart(cart);
        System.out.println("Dish " + dish.getName() + " is added");
        return cart;
    }

    public Cart removeDish(@Nonnull Dish dish, @Nonnull Cart cart) throws Exception {
        log.debug("CartController > removeDish({})", dish.getId());
        if (dishController.getDish(dish.getId()) == null) {
            throw new Exception("Dish doesn't exists");
        }

        if (cart.getItems().contains(dish)) {
            cart.getItems().remove(dish);
            cart.setTotalPrice(calculateTotalPrice(cart));
        }
        System.out.println("Dish " + dish.getName() + " is removed");
        if (cart.getItems().isEmpty()) {
            System.out.println("Your cart is now empty!");
        }
        updateCart(cart);
        return cart;
    }

    public Cart emptyCart(@Nonnull Cart cart) throws Exception {
        cart = new Cart();
        updateCart(cart);
        System.out.println("Cart has been cleared");
        return cart;
    }
}
