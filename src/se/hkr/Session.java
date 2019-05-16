package se.hkr;

import se.hkr.Scenes.SessionListener;

import java.util.ArrayList;
import java.util.List;

public abstract class Session <T> {
    protected T sessionObject;
    private List<SessionListener> listeners = new ArrayList<>();

    public void notifyListeners() {
        listeners.forEach((listener) -> {
            listener.update();
        });
    }

    public void addListener(SessionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SessionListener listener) {
        listeners.remove(listener);
    }

    public T getSessionObject() {
        return sessionObject;
    }

    public void setSessionObject(T sessionObject) {
        this.sessionObject = sessionObject;
    }

    public abstract void resetSession();
}
