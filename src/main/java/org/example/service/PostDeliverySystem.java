package org.example.service;

public class PostDeliverySystem implements DeliverySystem {
    @Override
    public void deliver(String gift, String person) {
        System.out.printf("Delivered a %s to %s by POST%n", gift, person);
    }
}
