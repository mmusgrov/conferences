package io.narayana.devconf;

import org.jboss.stm.annotations.Nested;
import org.jboss.stm.annotations.Transactional;

@Transactional
@Nested
public interface FlightService {
    int getNumberOfBookings();
    void makeBooking(String details);
}
