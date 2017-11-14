package io.narayana.devconf;

import io.vertx.core.AbstractVerticle;

import java.util.UUID;

public class ConsumerVerticle extends AbstractVerticle {
    private static UUID id = UUID.randomUUID();

    @Override
    public void start() {
        vertx.eventBus().consumer("anAddress", message -> {
            System.out.printf("%s: %s%n", id.toString(), message.body());
        });
    }

}
