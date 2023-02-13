package org.example.postprocessor;

public class LoggingBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void process(Object bean) {
        System.out.printf("Log: bean has been initialized: %s%n", bean.getClass());
    }
}
