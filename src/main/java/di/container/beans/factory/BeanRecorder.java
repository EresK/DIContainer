package di.container.beans.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanRecorder {
    private final Map<Class<?>, Object> singletonMap = new ConcurrentHashMap<>();
    private final Map<Thread, Map<Class<?>, Object>> threadMap = new ConcurrentHashMap<>();

    public void registerSingletonBean(Class<?> clazz, Object instance) {
        singletonMap.putIfAbsent(clazz, instance);
    }

    public void registerThreadBean(Thread thread, Class<?> clazz, Object instance) {
        Map<Class<?>, Object> map = threadMap.get(thread);

        if (map != null) {
            if (map.get(clazz) != null)
                throw new IllegalStateException(String.format(
                        "Can not create extra instance for bean: %s, with thread scope", clazz));

            map.put(clazz, instance);
        }
        else {
            map = new HashMap<>();
            map.put(clazz, instance);
            threadMap.put(thread, map);
        }

        clean();
    }

    public Object getSingletonBean(Class<?> clazz) {
        return singletonMap.get(clazz);
    }

    public Object getThreadBean(Thread thread, Class<?> clazz) {
        return threadMap.get(thread) != null ?
                threadMap.get(thread).get(clazz) : null;
    }

    private void clean() {
        List<Thread> threads = threadMap.keySet().stream().filter(thread -> !thread.isAlive()).toList();

        for (Thread t : threads)
            threadMap.remove(t);
    }
}
