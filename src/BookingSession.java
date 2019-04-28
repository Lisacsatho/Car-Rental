import se.hkr.Model.Booking;

public class BookingSession {
    private static BookingSession ourInstance = new BookingSession();

    public static BookingSession getInstance() {
        return ourInstance;
    }

    private BookingSession() {

    }

    public static BookingSession getBooking() {
        return ourInstance;
    }

    public void resetSession() {
    }
}
