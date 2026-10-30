package coffeeshop;

public final class PreparingState implements OrderState {
    @Override
    public String getName() {
        return "Preparing";
    }

    @Override
    public void advance(Order order) {
        order.changeState(new ReadyState());
    }
}
