package dev.architectury.utils.value;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Value<T> extends Supplier<T>, Consumer<T> {
}