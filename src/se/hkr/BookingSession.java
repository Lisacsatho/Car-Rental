package se.hkr;

import se.hkr.Model.Booking;

public class BookingSession extends Session<Booking> {
    private static BookingSession ourInstance = new BookingSession();

    public static BookingSession getInstance() {
        return ourInstance;
    }

    private BookingSession() {

    }

    @Override
    public void resetSession() {
        sessionObject = new Booking();
    }
}


