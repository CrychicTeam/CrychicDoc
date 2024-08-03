package org.violetmoon.zeta.config;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueDefinition<T> extends Definition {

    @NotNull
    public final T defaultValue;

    @Nullable
    public final Predicate<Object> validator;

    public ValueDefinition(ValueDefinition.Builder<T> builder) {
        super(builder);
        this.defaultValue = (T) Preconditions.checkNotNull(builder.defaultValue, "ValueDefinition needs a default value");
        this.validator = builder.validator;
    }

    public boolean isOfType(Class<?> clazz) {
        return clazz.isAssignableFrom(this.defaultValue.getClass());
    }

    @Nullable
    public <X> ValueDefinition<X> downcast(Class<X> newType) {
        return (ValueDefinition<X>) (this.isOfType(newType) ? this : null);
    }

    public boolean validate(Object underTest) {
        if (underTest == null) {
            return false;
        } else {
            boolean isList = List.class.isAssignableFrom(this.defaultValue.getClass());
            boolean isSubtype = this.defaultValue.getClass().isAssignableFrom(underTest.getClass());
            if (!isList && !isSubtype) {
                return false;
            } else {
                return this.validator == null ? true : this.validator.test(underTest);
            }
        }
    }

    public String toString() {
        return "ValueDefinition{" + this.name + "}";
    }

    public static class Builder<T> extends Definition.Builder<ValueDefinition.Builder<T>, ValueDefinition<T>> {

        @Nullable
        protected T defaultValue;

        @Nullable
        protected Predicate<Object> validator;

        public ValueDefinition<T> build() {
            return new ValueDefinition<>(this);
        }

        public ValueDefinition.Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public ValueDefinition.Builder<T> validator(Predicate<Object> validator) {
            this.validator = validator;
            return this;
        }
    }
}