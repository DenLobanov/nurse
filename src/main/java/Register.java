import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Register {

    Map<String, Object> register = new HashMap<>();
    Map<Field, Object> fieldsToInject = new HashMap<>();

    public Optional<Object> get(String name) {
        Object something = register.get(name);
        return Optional.ofNullable(something);
    }

    void inject() {
        for (Field field : fieldsToInject.keySet()) {
            Object something = fieldsToInject.get(field);
            Object injection = this.get(field.getType());
            field.setAccessible(true);
            try {
                field.set(something, injection);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> T get(Class<T> type) {
        return (T) get(type.getClass().getName());
    }

    void add(String name, Object o) {
        if (register.containsKey(name)) {
            throw new RuntimeException();
        }
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                fieldsToInject.put(field, o);
            }

        }
        this.register.put(name, o);
    }

    void add(Object some) {
        add(some.getClass().getName(), some);
    }
}
