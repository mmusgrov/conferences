package org.acme.quickstart;

import org.jboss.stm.Container;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceFactory {
    private FlightService flightService;
    private TaxiService taxiService;
    private TaxiService altTaxiService;

    public ServiceFactory() {
        Container<FlightService> flightContainer = new Container<>();
        Container<TaxiService> taxiContainer = new Container<>();

        flightService = flightContainer.create(new FlightServiceImpl());
        taxiService = taxiContainer.create(new TaxiServiceImpl());
        altTaxiService = taxiContainer.create(new TaxiServiceImpl());
    }

    FlightService getFlightService() {
        return flightService;
    }

    TaxiService getTaxiService() {
        return taxiService;
    }

    TaxiService getAltTaxiService() {
        return altTaxiService;
    }
}
