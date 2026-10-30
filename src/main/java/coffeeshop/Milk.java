package coffeeshop;

public final class Milk extends DrinkDecorator {
    public Milk(Drink drink) {
        super(drink);
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + " + milk";
    }

    @Override
    public int getPriceInCents() {
        return drink.getPriceInCents() + 50;
    }
}
