package io.narayana.mucon;

import com.arjuna.ats.arjuna.AtomicAction;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.jboss.stm.Container;

public class MainVerticle extends AbstractVerticle {

    private static Container<FlightService> container;
    private static FlightService flightService;

    public static void main(String[] args) {
        container = new Container<>(Container.TYPE.PERSISTENT, Container.MODEL.SHARED);

        flightService = container.create(new FlightServiceImpl());

        AtomicAction a = new AtomicAction();
        flightService.createBooking("abc");
        a.commit();
        Vertx.vertx().deployVerticle(MainVerticle.class.getName(), new DeploymentOptions().setInstances(10));
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        FlightService clone = container.clone(new FlightServiceImpl(), container.getIdentifier(flightService));

        router.post("/api").handler(request -> {
            clone.createBooking("BA123");
            request.response().end(Thread.currentThread().getName() + ": Booking Count: " + clone.getNumberOfBookings());
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

}
