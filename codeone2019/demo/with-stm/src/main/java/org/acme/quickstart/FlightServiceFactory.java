package org.acme.quickstart;

import org.jboss.stm.Container;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FlightServiceFactory {
    private Container<FlightService> container;
    private FlightService flightServiceProxy;

    public FlightServiceFactory() {
        container = new Container<>();
        flightServiceProxy = container.create(new FlightServiceImpl());
    }

    FlightService getInstance() {
        return flightServiceProxy;
    }
}
