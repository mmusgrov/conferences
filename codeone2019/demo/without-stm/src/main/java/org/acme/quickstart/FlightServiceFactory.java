package org.acme.quickstart;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FlightServiceFactory {
    private FlightService flightService;

    public FlightServiceFactory() {
        flightService = new FlightServiceImpl();
    }

    FlightService getInstance() {
        return flightService;
    }
}
