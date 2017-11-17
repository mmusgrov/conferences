package io.narayana.devconf;

import io.vertx.core.AbstractVerticle;

public class TaxiVerticle extends AbstractVerticle {
    static String ADDRESS = "flight-service";

    @Override
    public void start() {
        String uid = config().getString("flight-uid");

        System.out.printf("Using uid %s%n", uid);

        TaxiService service = Helper.getTaxiService(uid);

        vertx.eventBus().consumer(ADDRESS, message -> {
            System.out.printf("Received: %s%n", message.body());
            service.makeBooking(message.body().toString());
        });
    }
}
