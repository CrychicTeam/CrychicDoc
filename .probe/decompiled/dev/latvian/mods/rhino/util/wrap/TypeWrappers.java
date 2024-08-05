package dev.latvian.mods.rhino.util.wrap;

import dev.latvian.mods.rhino.util.EnumTypeWrapper;
import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public class TypeWrappers {

    private final Map<Class<?>, TypeWrapper<?>> wrappers = new LinkedHashMap();

    public <T> void register(Class<T> target, Predicate<Object> validator, TypeWrapperFactory<T> factory) {
        if (target == null || target == Object.class) {
            throw new IllegalArgumentException("target can't be Object.class!");
        } else if (target.isArray()) {
            throw new IllegalArgumentException("target can't be an array!");
        } else if (this.wrappers.containsKey(target)) {
            throw new IllegalArgumentException("Wrapper for class " + target.getName() + " already exists!");
        } else {
            TypeWrapper<T> typeWrapper0 = new TypeWrapper<>(target, validator, factory);
            this.wrappers.put(target, typeWrapper0);
            Class<T[]> target1 = Array.newInstance(target, 0).getClass();
            TypeWrapper<T[]> typeWrapper1 = new TypeWrapper(target1, validator, new ArrayTypeWrapperFactory<>(typeWrapper0, target, target1));
            this.wrappers.put(target1, typeWrapper1);
            Class<T[][]> target2 = Array.newInstance(target1, 0).getClass();
            TypeWrapper<T[][]> typeWrapper2 = new TypeWrapper(target2, validator, new ArrayTypeWrapperFactory(typeWrapper1, target1, target2));
            this.wrappers.put(target2, typeWrapper2);
            Class<T[][][]> target3 = Array.newInstance(target2, 0).getClass();
            TypeWrapper<T[][][]> typeWrapper3 = new TypeWrapper(target3, validator, new ArrayTypeWrapperFactory(typeWrapper2, target2, target3));
            this.wrappers.put(target3, typeWrapper3);
        }
    }

    public <T> void register(Class<T> target, TypeWrapperFactory<T> factory) {
        this.register(target, TypeWrapper.ALWAYS_VALID, factory);
    }

    public <T> void registerSimple(Class<T> target, Predicate<Object> validator, TypeWrapperFactory.Simple<T> factory) {
        this.register(target, validator, factory);
    }

    public <T> void registerSimple(Class<T> target, TypeWrapperFactory.Simple<T> factory) {
        this.register(target, TypeWrapper.ALWAYS_VALID, factory);
    }

    @Nullable
    public TypeWrapperFactory<?> getWrapperFactory(Class<?> target, @Nullable Object from) {
        if (target == Object.class) {
            return null;
        } else {
            TypeWrapper<?> wrapper = (TypeWrapper<?>) this.wrappers.get(target);
            if (wrapper != null && wrapper.validator.test(from)) {
                return wrapper.factory;
            } else {
                return target.isEnum() ? EnumTypeWrapper.get(target) : null;
            }
        }
    }
}