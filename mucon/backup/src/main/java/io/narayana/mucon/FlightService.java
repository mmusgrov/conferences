package io.narayana.mucon;

import org.jboss.stm.annotations.Transactional;

@Transactional
public interface FlightService {
    int getNumberOfBookings();
    void createBooking(String details);
}
