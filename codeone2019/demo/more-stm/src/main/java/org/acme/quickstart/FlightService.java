package org.acme.quickstart;

import org.jboss.stm.annotations.Transactional;

@Transactional
public interface FlightService {
    int getNumberOfBookings();
    void makeBooking(String details);
}
