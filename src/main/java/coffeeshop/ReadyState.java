package coffeeshop;

public final class ReadyState implements OrderState {
    @Override
    public String getName() {
        return "Ready";
    }

    @Override
    public void advance(Order order) {
        // Ready is the final state: no change and no duplicate notification.
    }
}
