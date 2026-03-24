package Backend.BUS;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    public interface Listener {
        void onEvent(String eventName);
    }

    // ✅ Dùng CopyOnWriteArrayList để thread-safe:
    //    tránh ConcurrentModificationException khi publish đồng thời subscribe
    private static final List<Listener> listeners = new CopyOnWriteArrayList<>();

    public static void subscribe(Listener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void unsubscribe(Listener listener) {
        listeners.remove(listener);
    }

    public static void publish(String eventName) {
        for (Listener listener : listeners) {
            listener.onEvent(eventName);
        }
    }
}