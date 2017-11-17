package io.narayana.devconf;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {
    private TaxiService taxiService;

    @Override
    public void start() {

        taxiService = Helper.getTaxiService();

        if (Boolean.getBoolean("flights")) {
            JsonObject config = new JsonObject().put("flight-uid", Helper.readSharedUid());

            vertx.deployVerticle(TaxiVerticle.class.getName(), new DeploymentOptions().setConfig(config).setInstances(1));
        } else {
            Router router = Router.router(vertx);

            router.route().handler(BodyHandler.create());

            router.get("/api").handler(request -> {
                System.out.printf("Booking count: %d%n", taxiService.getNumberOfBookings());
                request.response().end(Integer.toString(taxiService.getNumberOfBookings()));
            });

            router.post("/api").handler(request -> {
                vertx.eventBus().publish(TaxiVerticle.ADDRESS, "BA123");
                request.response().end();
            });

            vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        }
    }
}
