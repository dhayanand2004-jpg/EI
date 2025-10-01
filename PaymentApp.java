
interface PaymentStrategy {
    void pay(double amount, String currency);
    String getMethodName();
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(double amount, String currency) {
        System.out.println("Paid " + amount + " " + currency + " with Credit Card.");
    }
    public String getMethodName() { return "CreditCard"; }
}

class PayPalPayment implements PaymentStrategy {
    public void pay(double amount, String currency) {
        System.out.println("Paid " + amount + " " + currency + " via PayPal.");
    }
    public String getMethodName() { return "PayPal"; }
}

class PaymentProcessor {
    private PaymentStrategy strategy;
    public void setPaymentMethod(PaymentStrategy strategy) { this.strategy = strategy; }
    public void process(double amount, String currency) { strategy.pay(amount, currency); }
}

public class PaymentApp {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        processor.setPaymentMethod(new CreditCardPayment());
        processor.process(150.0, "USD");
        processor.setPaymentMethod(new PayPalPayment());
        processor.process(57.0, "EUR");
    }
}

