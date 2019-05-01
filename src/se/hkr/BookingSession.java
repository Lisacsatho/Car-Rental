package se.hkr;

import se.hkr.Model.Booking;

public class BookingSession {
    private static BookingSession ourInstance = new BookingSession();

    private Booking booking;

    public static BookingSession getInstance() {
        return ourInstance;
    }

    private BookingSession() {

    }

    public Booking getBooking() {
        return booking;
    }

    public void resetSession() {
        booking = new Booking();
    }
}
