package coffeeshop;

public interface PricingStrategy {
    String getName();

    int calculateTotalInCents(int subtotalInCents);
}
