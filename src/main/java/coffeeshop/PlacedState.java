package coffeeshop;

public final class PlacedState implements OrderState {
    @Override
    public String getName() {
        return "Placed";
    }

    @Override
    public void advance(Order order) {
        order.changeState(new PreparingState());
    }
}
