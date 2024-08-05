package pie.ilikepiefoo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class ReflectionUtils {

    public static <T> ReflectionUtils.Pair<Class<?>, T> retrieveEventClass(Class<?> eventProvider, String fieldName, Class<T> eventType) throws IllegalArgumentException {
        if (eventProvider == null) {
            throw new IllegalArgumentException("Event Provider cannot be null!");
        } else if (fieldName == null) {
            throw new IllegalArgumentException("Field Name cannot be null!");
        } else {
            Field field = null;
            try {
                field = eventProvider.getField(fieldName);
            } catch (NoSuchFieldException var10) {
                String field_list = getFieldList(eventProvider.getFields());
                throw new IllegalArgumentException("Field Name must be a valid field! Valid fields are: " + field_list + "!");
            }
            if (!eventType.isAssignableFrom(field.getType())) {
                throw new IllegalArgumentException("Field must be of type Event!");
            } else if (!Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                String field_list = getFieldList(eventProvider.getFields());
                throw new IllegalArgumentException("Event Field must be public static! Valid fields are: " + field_list + "!");
            } else {
                T event = null;
                try {
                    event = (T) field.get(null);
                } catch (IllegalAccessException var9) {
                    String field_list = getFieldList(eventProvider.getFields());
                    throw new IllegalArgumentException("Event Field must be public static! Valid fields are: " + field_list + "!");
                }
                if (field.getGenericType() instanceof ParameterizedType parameterizedType) {
                    Type var16 = parameterizedType.getActualTypeArguments()[0];
                    Class eventClass = null;
                    if (var16 instanceof Class<?> clazz) {
                        eventClass = clazz;
                    }
                    if (eventClass == null && var16 instanceof ParameterizedType parameterizedType2) {
                        eventClass = (Class) parameterizedType2.getRawType();
                    }
                    if (eventClass == null) {
                        throw new IllegalArgumentException("Event Field must contain a either a parameterized type or a class!");
                    } else if (!eventClass.isInterface()) {
                        throw new IllegalArgumentException("Event Type must be an interface!");
                    } else {
                        return new ReflectionUtils.Pair<>(eventClass, event);
                    }
                } else {
                    throw new IllegalArgumentException("Event Field must contain a be parameterized type!");
                }
            }
        }
    }

    @NotNull
    private static String getFieldList(Field[] eventProvider) {
        return Arrays.toString(Stream.of(eventProvider).filter(f -> Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers())).map(Field::getName).toArray());
    }

    public static class Pair<A, B> {

        public A a;

        public B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getA() {
            return this.a;
        }

        public B getB() {
            return this.b;
        }
    }
}