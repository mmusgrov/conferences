package org.acme.quickstart;

import org.jboss.stm.annotations.Transactional;

@Transactional
public interface TaxiService {
    int getNumberOfBookings();
    void makeBooking(String details);
    void failToBook() throws Exception;
}
