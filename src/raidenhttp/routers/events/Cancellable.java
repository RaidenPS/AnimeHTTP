package raidenhttp.routers.events;

/** Implementing this interface marks an event as cancellable. */
public interface Cancellable {
    void cancel();
}
