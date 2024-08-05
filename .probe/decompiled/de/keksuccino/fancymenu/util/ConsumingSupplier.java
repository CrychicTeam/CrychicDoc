package de.keksuccino.fancymenu.util;

@FunctionalInterface
public interface ConsumingSupplier<C, R> {

    R get(C var1);
}