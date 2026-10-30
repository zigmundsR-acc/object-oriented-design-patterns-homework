# Coffee shop simulator

A small Java console application demonstrating five GoF design patterns. It prepares three predefined orders, prints their prices, and notifies customers and baristas as the orders progress. It reads no user input and has no external application dependencies.

## Patterns

| Pattern | Category | Implementation | Purpose |
| --- | --- | --- | --- |
| Builder | Creational | `Order.Builder` | Constructs an order with a table number, drinks, and an optional pricing strategy. |
| Decorator | Structural | `Drink`, `Coffee`, `DrinkDecorator`, `Milk`, `Caramel`, `ExtraEspressoShot` | Wraps a drink to add a topping's description and price. Decorators can be stacked. |
| Strategy | Behavioral | `PricingStrategy`, `RegularPricing`, `EmployeeDiscount`, `HappyHourDiscount` | Lets an order delegate its total calculation to an interchangeable pricing algorithm. |
| State | Behavioral | `OrderState`, `PlacedState`, `PreparingState`, `ReadyState` | Delegates progression to the current state: `Placed -> Preparing -> Ready`. |
| Observer | Behavioral | `OrderObserver`, `CustomerNotifier`, `BaristaNotifier` | Notifies subscribed observers after each state change. |

This meets the homework requirements: one creational pattern and four non-creational patterns, including both structural and behavioral patterns.

## Run

Install a **JDK 8 or newer** and ensure `java` and `javac` are on `PATH`. A Java runtime alone does not include the compiler. No Maven or Gradle is needed.

From the project directory in PowerShell:

```powershell
.\run.ps1
```

Or compile and run directly:

```powershell
New-Item -ItemType Directory -Path out -Force | Out-Null
javac -encoding UTF-8 -d out src/main/java/coffeeshop/*.java
java -cp out coffeeshop.Main
```

The main class is `src/main/java/coffeeshop/Main.java`. Edit its predefined orders to change the simulation; there are no prompts or interactive menus.

## Example

The first order is a caramel latte with an employee discount:

```text
--- Table 1 ---
  Latte + caramel: 4.00 EUR
Subtotal: 4.00 EUR
Pricing: Employee (10% off)
Total: 3.60 EUR
State: Placed
Customer: your order for table 1 is now Preparing.
Barista: table 1 status changed to Preparing.
Customer: your order for table 1 is now Ready.
Barista: table 1 status changed to Ready.
```

The other orders demonstrate regular pricing with multiple drinks (5.00 EUR) and happy-hour pricing with stacked toppings (3.20 EUR). Discounts are selected explicitly; the simulation does not depend on the clock.

## Implementation notes

- All prices use integer cents. Employee pricing takes 10% off; happy-hour pricing takes 20% off. Totals round to the nearest cent, with ties rounded up.
- The builder requires a positive table number and at least one drink. It copies its drink list so reusing the builder does not modify previous orders.
- An order starts in `Placed`. Each call to `advance()` delegates to its current state. Advancing a `Ready` order does nothing.
- State objects decide the next state; `Order` performs the change and notifies its observers. Both notifiers print to standard output.
- `setPricingStrategy()` allows changing the pricing algorithm at runtime; `removeObserver()` allows unsubscribing from notifications.

## Verify

The dependency-free test runner checks decorated prices, discount rounding, interchangeable strategies, builder validation and isolation, state transitions, observer subscription/removal, and all three console examples:

```powershell
.\run.ps1 -Test
```

Or, after compiling the main sources:

```powershell
javac -encoding UTF-8 -cp out -d out src/test/java/coffeeshop/CoffeeShopTest.java
java -cp out coffeeshop.CoffeeShopTest
```
