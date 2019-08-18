package org.acme.quickstart;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/stm")
@RequestScoped
public class FlightResource {
    @Inject
    private FlightServiceFactory factory;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<Integer> bookingCount() {
        return CompletableFuture.supplyAsync(
                () -> factory.getInstance().getNumberOfBookings()
        );
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> book() {
        return CompletableFuture.supplyAsync(() -> {
            FlightService flightService = factory.getInstance();

            flightService.makeBooking("BA123");

            return Thread.currentThread().getName()
                    + ":  Booking Count=" + flightService.getNumberOfBookings();
        });
    }
}
