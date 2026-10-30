package coffeeshop;

public final class EmployeeDiscount implements PricingStrategy {
    @Override
    public String getName() {
        return "Employee (10% off)";
    }

    @Override
    public int calculateTotalInCents(int subtotalInCents) {
        // Round to the nearest cent; ties round up. Use long for multiplication.
        return (int) ((subtotalInCents * 90L + 50) / 100);
    }
}
