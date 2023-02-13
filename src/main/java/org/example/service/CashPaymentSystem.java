package org.example.service;

public class CashPaymentSystem implements PaymentSystem {
    @Override
    public void pay(String gift) {
        System.out.printf("Pay money for a %s by CASH%n", gift);
    }
}
