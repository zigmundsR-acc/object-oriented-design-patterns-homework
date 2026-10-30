package coffeeshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Builder creates the order; this context delegates pricing and state behavior
// and notifies observers when its state changes.
public final class Order {
    private final int tableNumber;
    private final List<Drink> drinks;
    private final List<OrderObserver> observers = new ArrayList<>();
    private PricingStrategy pricingStrategy;
    private OrderState state = new PlacedState();

    private Order(Builder builder) {
        tableNumber = builder.tableNumber;
        drinks = Collections.unmodifiableList(new ArrayList<>(builder.drinks));
        pricingStrategy = builder.pricingStrategy;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public int getSubtotalInCents() {
        int subtotal = 0;
        for (Drink drink : drinks) {
            subtotal += drink.getPriceInCents();
        }
        return subtotal;
    }

    public int getTotalInCents() {
        return pricingStrategy.calculateTotalInCents(getSubtotalInCents());
    }

    public String getPricingName() {
        return pricingStrategy.getName();
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy,
                "A pricing strategy is required");
    }

    public String getStateName() {
        return state.getName();
    }

    public void advance() {
        state.advance(this);
    }

    void changeState(OrderState nextState) {
        state = nextState;
        for (OrderObserver observer : observers) {
            observer.onStateChanged(this);
        }
    }

    public void addObserver(OrderObserver observer) {
        observers.add(Objects.requireNonNull(observer, "An observer is required"));
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public static final class Builder {
        private int tableNumber;
        private final List<Drink> drinks = new ArrayList<>();
        private PricingStrategy pricingStrategy = new RegularPricing();

        private Builder() {
        }

        public Builder tableNumber(int tableNumber) {
            this.tableNumber = tableNumber;
            return this;
        }

        public Builder addDrink(Drink drink) {
            drinks.add(Objects.requireNonNull(drink, "A drink is required"));
            return this;
        }

        public Builder pricingStrategy(PricingStrategy pricingStrategy) {
            this.pricingStrategy = Objects.requireNonNull(pricingStrategy,
                    "A pricing strategy is required");
            return this;
        }

        public Order build() {
            if (tableNumber <= 0) {
                throw new IllegalArgumentException("Table number must be positive");
            }
            if (drinks.isEmpty()) {
                throw new IllegalStateException("An order needs at least one drink");
            }
            return new Order(this);
        }
    }
}
