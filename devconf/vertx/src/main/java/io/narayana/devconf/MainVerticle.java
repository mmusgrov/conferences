package io.narayana.devconf;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.deployVerticle(ConsumerVerticle.class.getName(), new DeploymentOptions().setInstances(1));
        vertx.deployVerticle(ProducerVerticle.class.getName(), new DeploymentOptions().setInstances(1));
    }

}
