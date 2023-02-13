package org.example.config;

import org.example.service.CardPaymentSystem;
import org.example.service.CashPaymentSystem;
import org.example.service.PaymentSystem;

import java.util.Map;

public class JavaConfiguration implements Configuration {
    @Override
    public String getPackageToScan() {
        return "org.example";
    }

    @Override
    public Map<Class, Class> interfaceToImplementations() {
        return Map.of(PaymentSystem.class, CardPaymentSystem.class);
    }
}
