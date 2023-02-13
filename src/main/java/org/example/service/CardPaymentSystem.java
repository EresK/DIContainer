package org.example.service;

public class CardPaymentSystem implements PaymentSystem {
    @Override
    public void pay(String gift) {
        System.out.printf("Pay money for a %s by CARD%n", gift);
    }
}
