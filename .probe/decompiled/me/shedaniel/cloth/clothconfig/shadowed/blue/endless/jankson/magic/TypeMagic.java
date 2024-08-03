package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.magic;

import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.annotation.Nullable;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.DeserializationException;

public class TypeMagic {

    private static Map<Class<?>, Class<?>> concreteClasses = new HashMap();

    @Nullable
    public static Class<?> classForType(Type t) {
        if (t instanceof Class) {
            return (Class<?>) t;
        } else {
            if (t instanceof ParameterizedType) {
                Type subtype = ((ParameterizedType) t).getRawType();
                if (subtype instanceof Class) {
                    return (Class<?>) subtype;
                }
                String className = t.getTypeName();
                int typeParamStart = className.indexOf(60);
                if (typeParamStart >= 0) {
                    className = className.substring(0, typeParamStart);
                }
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException var6) {
                }
            }
            if (t instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) t).getUpperBounds();
                return upperBounds.length == 0 ? Object.class : classForType(upperBounds[0]);
            } else if (t instanceof TypeVariable) {
                return Object.class;
            } else if (t instanceof GenericArrayType) {
                GenericArrayType arrayType = (GenericArrayType) t;
                Class<?> componentClass = classForType(arrayType.getGenericComponentType());
                try {
                    return Class.forName("[L" + componentClass.getCanonicalName() + ";");
                } catch (ClassNotFoundException var5) {
                    return Object[].class;
                }
            } else {
                return null;
            }
        }
    }

    @Nullable
    public static <U> U createAndCast(Type t) {
        try {
            return createAndCast((Class<U>) classForType(t), false);
        } catch (Throwable var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static <U> U createAndCastCarefully(Type t) throws DeserializationException {
        return createAndCast(classForType(t));
    }

    @Nullable
    public static <U> U createAndCast(Class<U> t, boolean failFast) throws DeserializationException {
        if (t.isInterface()) {
            Class<?> substitute = (Class<?>) concreteClasses.get(t);
            if (substitute != null) {
                try {
                    return createAndCast(substitute);
                } catch (Throwable var5) {
                    return null;
                }
            }
        }
        Constructor<U> noArg = null;
        try {
            noArg = t.getConstructor();
        } catch (Throwable var8) {
            try {
                noArg = t.getDeclaredConstructor();
            } catch (Throwable var7) {
                if (failFast) {
                    throw new DeserializationException("Class " + t.getCanonicalName() + " doesn't have a no-arg constructor, so an instance can't be created.");
                }
                return null;
            }
        }
        try {
            boolean available = noArg.isAccessible();
            if (!available) {
                noArg.setAccessible(true);
            }
            U u = (U) noArg.newInstance();
            if (!available) {
                noArg.setAccessible(false);
            }
            return u;
        } catch (Throwable var6) {
            if (failFast) {
                throw new DeserializationException("An error occurred while creating an object.", var6);
            } else {
                return null;
            }
        }
    }

    public static <T> T shoehorn(Object o) {
        return (T) o;
    }

    static {
        concreteClasses.put(Map.class, HashMap.class);
        concreteClasses.put(Set.class, HashSet.class);
        concreteClasses.put(Collection.class, ArrayList.class);
        concreteClasses.put(List.class, ArrayList.class);
        concreteClasses.put(Queue.class, ArrayDeque.class);
        concreteClasses.put(Deque.class, ArrayDeque.class);
    }
}