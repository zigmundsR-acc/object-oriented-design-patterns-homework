package coffeeshop;

public final class HappyHourDiscount implements PricingStrategy {
    @Override
    public String getName() {
        return "Happy hour (20% off)";
    }

    @Override
    public int calculateTotalInCents(int subtotalInCents) {
        return (int) ((subtotalInCents * 80L + 50) / 100);
    }
}
