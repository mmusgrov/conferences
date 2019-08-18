package org.acme.quickstart;

public class FlightServiceImpl implements FlightService {
    private int numberOfBookings;

    public int getNumberOfBookings() {
        return numberOfBookings;
    }

    public void makeBooking(String details) {
        numberOfBookings += 1;
    }
}
