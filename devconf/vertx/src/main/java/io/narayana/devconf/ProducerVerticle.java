package io.narayana.devconf;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

import java.util.UUID;

public class ProducerVerticle extends AbstractVerticle {
    private static UUID id = UUID.randomUUID();

    @Override
    public void start() {
        vertx.setPeriodic(10000, tid -> {

            vertx.eventBus().publish("anAddress", String.format("From %s:%s", id.toString(), Thread.currentThread().getName()));
//            vertx.eventBus().send   ("anAddress", "send from " + Thread.currentThread().getName());
        });


    }

}
