package coffeeshop;

public interface OrderState {
    String getName();

    void advance(Order order);
}
