package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Delivery;
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
public class DeliveryController {
    private final GenericRepository<Delivery> deliverys;

    @Inject
    DeliveryController(GenericRepository<Delivery> deliveryRepository) {
        deliverys = deliveryRepository;

        log.info("DeliveryController > construct");

        if (deliverys.count() > 0) {
            return;
        }

        log.info("DeliveryController > construct > adding default deliverys");

        final Delivery defaultDelivery1 = new Delivery();
        defaultDelivery1.setTitle("Hot dog");
        final ObjectId id = new ObjectId("5fc860ab39d3aa1512ab0971");
        defaultDelivery1.setId(id);

        final Delivery defaultDelivery2 = new Delivery();
        defaultDelivery2.setTitle("A steak");
        defaultDelivery2.setDescription("Not a hot dog");

        try {
            addDelivery(defaultDelivery1);
            addDelivery(defaultDelivery2);
        } catch (Exception e) {
            log.error("DeliveryController > construct > adding default deliverys > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Delivery getDelivery(@Nonnull ObjectId uuid) {
        log.debug("DeliveryController > getDelivery({})", uuid);
        return deliverys.get(uuid);
    }

    @Nonnull
    public Collection<Delivery> getDeliverys() {
        log.debug("DeliveryController > getDeliverys()");
        return deliverys.getAll();
    }

    @Nonnull
    public Delivery addDelivery(@Nonnull Delivery delivery) throws Exception {
        log.debug("DeliveryController > addDelivery(...)");
        if (!delivery.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidDeliveryException");
        }

        ObjectId id = delivery.getId();

        if (id != null && deliverys.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return deliverys.add(delivery);
    }

    public void updateDelivery(@Nonnull Delivery delivery) throws Exception {
        log.debug("DeliveryController > updateDelivery(...)");
        deliverys.update(delivery);
    }

    public void deleteDelivery(@Nonnull ObjectId id) throws Exception {
        log.debug("DeliveryController > deleteDelivery(...)");
        deliverys.delete(id);
    }
}
