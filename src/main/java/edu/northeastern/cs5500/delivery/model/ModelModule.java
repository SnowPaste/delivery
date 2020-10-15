package edu.northeastern.cs5500.delivery.model;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {
    @Provides
    public Class<Delivery> provideDeliveryClass() {
        return Delivery.class;
    }

    @Provides
    public Class<Address> provideAddressClass() {
        return Address.class;
    }

    @Provides
    public Class<Cart> provideCartClass() {
        return Cart.class;
    }

    @Provides
    public Class<Order> provideOrderClass() {
        return Order.class;
    }
}
