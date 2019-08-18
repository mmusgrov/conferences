package org.acme.quickstart;

import org.jboss.stm.annotations.ReadLock;
import org.jboss.stm.annotations.State;
import org.jboss.stm.annotations.WriteLock;

public class TaxiServiceImpl implements TaxiService {
    @State
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

    @Override
    @WriteLock
    public void failToBook() throws Exception {
        throw new Exception();
    }
}
