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
    public Class<Driver> provideDriverClass() {return Driver.class;}

    @Provides
    public Class<CreditCard> provideCreditCardClass() {return CreditCard.class;}

    @Provides
    public Class<Customer> provideCustomerClass() {return Customer.class;}
}
