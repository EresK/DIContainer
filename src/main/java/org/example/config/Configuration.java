package org.example.config;

import java.util.Map;

public interface Configuration {
    String getPackageToScan();

    Map<Class, Class> interfaceToImplementations();
}
