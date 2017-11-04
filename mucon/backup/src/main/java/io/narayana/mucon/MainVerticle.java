package io.narayana.mucon;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.post("/api").handler(request -> {
            request.response().end(Thread.currentThread().getName());
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

}
