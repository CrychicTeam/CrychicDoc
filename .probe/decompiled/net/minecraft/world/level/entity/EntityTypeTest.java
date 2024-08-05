package net.minecraft.world.level.entity;

import javax.annotation.Nullable;

public interface EntityTypeTest<B, T extends B> {

    static <B, T extends B> EntityTypeTest<B, T> forClass(final Class<T> classT0) {
        return new EntityTypeTest<B, T>() {

            @Nullable
            @Override
            public T tryCast(B p_156924_) {
                return (T) (classT0.isInstance(p_156924_) ? p_156924_ : null);
            }

            @Override
            public Class<? extends B> getBaseClass() {
                return classT0;
            }
        };
    }

    @Nullable
    T tryCast(B var1);

    Class<? extends B> getBaseClass();
}