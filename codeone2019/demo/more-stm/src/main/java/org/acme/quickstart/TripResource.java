package org.acme.quickstart;

import com.arjuna.ats.arjuna.AtomicAction;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/trip")
@RequestScoped
public class TripResource {
    @Inject
    private ServiceFactory factory;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<Integer> bookingCount() {
        return CompletableFuture.supplyAsync(
                () -> factory.getFlightService().getNumberOfBookings());
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> bookTrip(
            @QueryParam("fail") @DefaultValue("true") boolean fail) {
        return CompletableFuture.supplyAsync(() -> {

            FlightService flightService = factory.getFlightService();
            TaxiService taxiService = factory.getTaxiService();
            TaxiService altTaxiService = factory.getAltTaxiService();

            int flightBookings = -1;
            int taxiBookings = -1;
            int altTaxiBookings = -1;

            boolean aborted = false;

            AtomicAction A = new AtomicAction();
            AtomicAction B = new AtomicAction();

            A.begin();
            {
                try {
                    flightService.makeBooking("BA123");

                    B.begin();
                    {
                        try {
                            taxiService.makeBooking("preferred-taxi");

                            if (fail) {
                                taxiService.failToBook();
                            }

                            B.commit();
                        } catch (Exception e) {
                            B.abort();

                            altTaxiService.makeBooking("alt-taxi");
                        }
                    }

                    flightBookings = flightService.getNumberOfBookings();
                    taxiBookings = taxiService.getNumberOfBookings();
                    altTaxiBookings = altTaxiService.getNumberOfBookings();

                    A.commit();
                } catch (Exception e) {
                    A.abort();

                    aborted = true;
                }
            }

            return String.format(
                    "%s: Transaction %s - booking counts:%n\t%d flights %d taxis %d alt-taxis%n",
                    Thread.currentThread().getName(),
                    aborted ? "aborted" : "committed",
                    flightBookings, taxiBookings, altTaxiBookings);
        });
    }
}
