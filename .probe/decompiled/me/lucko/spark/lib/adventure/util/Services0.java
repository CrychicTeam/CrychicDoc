package me.lucko.spark.lib.adventure.util;

import java.util.ServiceLoader;

final class Services0 {

    private Services0() {
    }

    static <S> ServiceLoader<S> loader(final Class<S> type) {
        return ServiceLoader.load(type, type.getClassLoader());
    }
}