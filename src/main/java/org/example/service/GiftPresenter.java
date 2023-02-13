package org.example.service;

import org.example.annotation.Inject;
import org.example.annotation.PostConstruct;

public class GiftPresenter {
    @Inject
    private PaymentSystem paymentSystem;

    @Inject
    private DeliverySystem deliverySystem;

    @PostConstruct
    public void postConstruct() {
        System.out.println("Gift presenter has been initialized!");
    }

    public void present(String person) {
        String gift = "Smart watches";
        System.out.printf("Gift was chosen: %s%n", gift);
        paymentSystem.pay(gift);
        deliverySystem.deliver(gift, person);
    }
}
