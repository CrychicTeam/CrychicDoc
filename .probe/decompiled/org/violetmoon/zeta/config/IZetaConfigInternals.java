package org.violetmoon.zeta.config;

public interface IZetaConfigInternals {

    <T> T get(ValueDefinition<T> var1);

    <T> void set(ValueDefinition<T> var1, T var2);

    void flush();

    default long debounceTime() {
        return 0L;
    }
}