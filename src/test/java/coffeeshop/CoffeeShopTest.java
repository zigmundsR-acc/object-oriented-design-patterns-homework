package coffeeshop;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// A small test runner, requiring no test framework or external dependencies.
public final class CoffeeShopTest {
    private static int checksPassed;

    private CoffeeShopTest() {
    }

    public static void main(String[] args) throws Exception {
        pricesAndDiscounts();
        builderValidationAndIsolation();
        statesAndObservers();
        simulatorOutput();
        System.out.println("All " + checksPassed + " checks passed.");
    }

    private static void pricesAndDiscounts() {
        Drink drink = new ExtraEspressoShot(
                new Caramel(new Milk(new Coffee("Espresso", 200))));
        check(drink.getPriceInCents() == 400, "Toppings contribute to the price");
        check(drink.getDescription().equals(
                "Espresso + milk + caramel + extra espresso shot"),
                "Toppings contribute to the description");

        Order order = Order.builder().tableNumber(1).addDrink(drink).build();
        check(order.getTotalInCents() == 400, "Default pricing is regular");
        order.setPricingStrategy(new EmployeeDiscount());
        check(order.getTotalInCents() == 360, "Employee receives 10% off");
        order.setPricingStrategy(new HappyHourDiscount());
        check(order.getTotalInCents() == 320, "Strategy can change at runtime");
        check(new EmployeeDiscount().calculateTotalInCents(105) == 95,
                "Half a cent rounds up");
        check(new HappyHourDiscount().calculateTotalInCents(101) == 81,
                "Fractional cents round to the nearest cent");
    }

    private static void builderValidationAndIsolation() {
        expectThrows(IllegalArgumentException.class,
                () -> Order.builder().addDrink(new Coffee("Latte", 350)).build());
        expectThrows(IllegalStateException.class,
                () -> Order.builder().tableNumber(1).build());

        Order.Builder builder = Order.builder().tableNumber(1)
                .addDrink(new Coffee("Latte", 350));
        Order first = builder.build();
        Order second = builder.addDrink(new Coffee("Espresso", 200)).build();
        check(first.getDrinks().size() == 1, "Builder reuse preserves earlier orders");
        check(second.getSubtotalInCents() == 550, "An order sums multiple drinks");
        expectThrows(UnsupportedOperationException.class, () -> first.getDrinks().clear());
    }

    private static void statesAndObservers() {
        Order order = Order.builder().tableNumber(1)
                .addDrink(new Coffee("Latte", 350)).build();
        List<String> events = new ArrayList<>();
        OrderObserver customer = changed -> events.add("Customer:" + changed.getStateName());
        OrderObserver barista = changed -> events.add("Barista:" + changed.getStateName());
        order.addObserver(customer);
        order.addObserver(barista);

        check(order.getStateName().equals("Placed"), "Orders start as placed");
        order.advance();
        check(order.getStateName().equals("Preparing"), "Placed advances to preparing");
        check(events.equals(Arrays.asList("Customer:Preparing", "Barista:Preparing")),
                "Every observer receives the updated state");

        order.removeObserver(barista);
        order.advance();
        check(order.getStateName().equals("Ready"), "Preparing advances to ready");
        check(events.equals(Arrays.asList(
                "Customer:Preparing", "Barista:Preparing", "Customer:Ready")),
                "Unsubscribed observers receive no further updates");
        order.advance();
        check(order.getStateName().equals("Ready") && events.size() == 3,
                "Ready is terminal and does not generate duplicate events");
    }

    private static void simulatorOutput() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        try (PrintStream capturedOutput = new PrintStream(output, true, "UTF-8")) {
            System.setOut(capturedOutput);
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOutput);
        }
        String text = output.toString("UTF-8");
        check(text.contains("Total: 3.60 EUR"), "Employee example prints its total");
        check(text.contains("Total: 5.00 EUR"), "Regular example prints its total");
        check(text.contains("Total: 3.20 EUR"), "Happy-hour example prints its total");
        for (int table = 1; table <= 3; table++) {
            check(text.contains("Customer: your order for table " + table + " is now Ready."),
                    "Customer receives the ready notification for table " + table);
            check(text.contains("Barista: table " + table + " status changed to Ready."),
                    "Barista receives the ready notification for table " + table);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        checksPassed++;
    }

    private static void expectThrows(Class<? extends RuntimeException> expected,
            Runnable action) {
        try {
            action.run();
        } catch (RuntimeException exception) {
            check(expected.isInstance(exception), "Expected " + expected.getSimpleName());
            return;
        }
        throw new AssertionError("Expected " + expected.getSimpleName());
    }
}
