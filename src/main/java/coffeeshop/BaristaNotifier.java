package coffeeshop;

public final class BaristaNotifier implements OrderObserver {
    @Override
    public void onStateChanged(Order order) {
        System.out.printf("Barista: table %d status changed to %s.%n",
                order.getTableNumber(), order.getStateName());
    }
}
