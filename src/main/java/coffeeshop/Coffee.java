package coffeeshop;

import java.util.Objects;

public final class Coffee implements Drink {
    private final String name;
    private final int priceInCents;

    public Coffee(String name, int priceInCents) {
        this.name = Objects.requireNonNull(name, "Coffee name is required");
        if (priceInCents < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.priceInCents = priceInCents;
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public int getPriceInCents() {
        return priceInCents;
    }
}
