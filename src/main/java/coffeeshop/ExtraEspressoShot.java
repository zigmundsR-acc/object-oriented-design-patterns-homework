package coffeeshop;

public final class ExtraEspressoShot extends DrinkDecorator {
    public ExtraEspressoShot(Drink drink) {
        super(drink);
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + " + extra espresso shot";
    }

    @Override
    public int getPriceInCents() {
        return drink.getPriceInCents() + 100;
    }
}
