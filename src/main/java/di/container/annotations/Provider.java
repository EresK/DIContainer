package di.container.annotations;

import di.container.context.AnnotationApplicationContext;

public class Provider<T> {
    private final Class<T> typeParameterClass;
    private final AnnotationApplicationContext context;
    public Provider(Class<T> typeParameterClass, AnnotationApplicationContext context) {
        this.typeParameterClass = typeParameterClass;
        this.context = context;
    }

    public T get() throws Exception {
        return context.getBean(typeParameterClass);
    }
}
