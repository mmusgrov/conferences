package io.narayana.devconf;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.jboss.stm.Container;

public class MainVerticle extends AbstractVerticle {
    private static FlightService flightService;
    private static Container<FlightService> container;

    public static void main(String[] args) {
        container = new Container<>();

        flightService = container.create(new FlightServiceImpl());

        Vertx.vertx().deployVerticle(
                MainVerticle.class.getName(),
                new DeploymentOptions().setInstances(8));
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        FlightService clone = container.clone(new FlightServiceImpl(), flightService);

        router.post("/api").handler(request -> {
            clone.makeBooking("BA123");
            request.response().end(Thread.currentThread().getName()
                    + ":  Booking Count=" + clone.getNumberOfBookings());
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

}
