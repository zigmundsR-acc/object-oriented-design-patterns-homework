package coffeeshop;

public final class Caramel extends DrinkDecorator {
    public Caramel(Drink drink) {
        super(drink);
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + " + caramel";
    }

    @Override
    public int getPriceInCents() {
        return drink.getPriceInCents() + 50;
    }
}
