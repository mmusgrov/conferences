package io.narayana.mucon;

public class FlightServiceImpl implements FlightService {
    private int numberOfBookings;

    @Override
    public int getNumberOfBookings() {
        return numberOfBookings;
    }

    @Override
    public void createBooking(String details) {
        numberOfBookings += 1;
    }
}
