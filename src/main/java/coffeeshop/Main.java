package coffeeshop;

import java.util.Locale;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Order employeeOrder = Order.builder()
                .tableNumber(1)
                .addDrink(new Caramel(new Coffee("Latte", 350)))
                .pricingStrategy(new EmployeeDiscount())
                .build();

        Order regularOrder = Order.builder()
                .tableNumber(2)
                .addDrink(new Milk(new Coffee("Espresso", 200)))
                .addDrink(new Coffee("Americano", 250))
                .build();

        Order happyHourOrder = Order.builder()
                .tableNumber(3)
                .addDrink(new ExtraEspressoShot(
                        new Caramel(new Milk(new Coffee("Espresso", 200)))))
                .pricingStrategy(new HappyHourDiscount())
                .build();

        System.out.println("COFFEE SHOP SIMULATOR");
        for (Order order : new Order[] {employeeOrder, regularOrder, happyHourOrder}) {
            prepare(order);
        }
    }

    private static void prepare(Order order) {
        order.addObserver(new CustomerNotifier());
        order.addObserver(new BaristaNotifier());

        System.out.printf("%n--- Table %d ---%n", order.getTableNumber());
        for (Drink drink : order.getDrinks()) {
            System.out.printf("  %s: %s%n", drink.getDescription(),
                    formatMoney(drink.getPriceInCents()));
        }
        System.out.println("Subtotal: " + formatMoney(order.getSubtotalInCents()));
        System.out.println("Pricing: " + order.getPricingName());
        System.out.println("Total: " + formatMoney(order.getTotalInCents()));
        System.out.println("State: " + order.getStateName());

        order.advance(); // Placed -> Preparing; both observers are notified.
        order.advance(); // Preparing -> Ready; both observers are notified.
    }

    private static String formatMoney(int cents) {
        return String.format(Locale.ROOT, "%d.%02d EUR", cents / 100, cents % 100);
    }
}
