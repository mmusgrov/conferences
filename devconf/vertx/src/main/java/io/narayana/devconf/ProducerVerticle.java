package io.narayana.devconf;

import io.vertx.core.AbstractVerticle;

import java.util.concurrent.atomic.AtomicLong;

public class ProducerVerticle extends AbstractVerticle {
    private static AtomicLong msgId = new AtomicLong(0);

    @Override
    public void start() {

        vertx.setPeriodic(10000, tid -> {
            long val = msgId.incrementAndGet();
            System.out.printf("sending %d%n", val);
            vertx.eventBus().publish("anAddress", String.format("Msg %d", val));
        });


    }
}
