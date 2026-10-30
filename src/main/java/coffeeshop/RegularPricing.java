package coffeeshop;

public final class RegularPricing implements PricingStrategy {
    @Override
    public String getName() {
        return "Regular";
    }

    @Override
    public int calculateTotalInCents(int subtotalInCents) {
        return subtotalInCents;
    }
}
