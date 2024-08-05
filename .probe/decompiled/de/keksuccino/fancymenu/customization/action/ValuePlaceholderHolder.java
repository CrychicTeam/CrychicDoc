package de.keksuccino.fancymenu.customization.action;

import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public interface ValuePlaceholderHolder {

    String VALUE_PLACEHOLDER_PREFIX = "$$";

    void addValuePlaceholder(@NotNull String var1, @NotNull Supplier<String> var2);

    @NotNull
    Map<String, Supplier<String>> getValuePlaceholders();
}