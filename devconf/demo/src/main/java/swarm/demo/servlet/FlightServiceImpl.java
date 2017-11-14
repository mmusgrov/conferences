package swarm.demo.servlet;

import org.jboss.stm.annotations.ReadLock;
import org.jboss.stm.annotations.WriteLock;

public class FlightServiceImpl implements FlightService {
    private int numberOfBookings;

    @Override
    @ReadLock
    public int getNumberOfBookings() {
        return numberOfBookings;
    }

    @Override
    @WriteLock
    public void makeBooking(String details) {
        numberOfBookings += 1;
    }
}
