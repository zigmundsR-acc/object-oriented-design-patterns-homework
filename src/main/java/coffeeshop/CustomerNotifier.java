package coffeeshop;

public final class CustomerNotifier implements OrderObserver {
    @Override
    public void onStateChanged(Order order) {
        System.out.printf("Customer: your order for table %d is now %s.%n",
                order.getTableNumber(), order.getStateName());
    }
}
