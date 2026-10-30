package coffeeshop;

public interface OrderObserver {
    void onStateChanged(Order order);
}
