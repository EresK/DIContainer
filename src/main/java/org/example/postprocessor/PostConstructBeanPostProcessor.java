package org.example.postprocessor;

import org.example.annotation.PostConstruct;

import java.lang.reflect.Method;

public class PostConstructBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void process(Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {

                try {
                    method.invoke(bean);
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                    throw new RuntimeException("Can not invoke post construct method for bean");
                }
            }
        }
    }
}
