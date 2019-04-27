package se.hkr.Database;

public interface Database extends AutoCloseable {
    void connect() throws Exception;
}
