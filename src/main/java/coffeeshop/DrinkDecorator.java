package coffeeshop;

import java.util.Objects;

// A decorator is itself a Drink, so toppings can wrap other toppings.
public abstract class DrinkDecorator implements Drink {
    protected final Drink drink;

    protected DrinkDecorator(Drink drink) {
        this.drink = Objects.requireNonNull(drink, "A drink is required");
    }
}
