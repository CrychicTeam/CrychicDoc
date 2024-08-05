package com.almostreliable.morejs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.Ingredient;

public class Utils {

    public static <T> Optional<T> cast(Object o, Class<T> type) {
        return type.isInstance(o) ? Optional.of(type.cast(o)) : Optional.empty();
    }

    public static <T> T cast(Object o) {
        return (T) o;
    }

    @Nullable
    public static <T> T nullableCast(@Nullable Object o) {
        return (T) (o == null ? null : o);
    }

    public static String format(String string) {
        int index = string.indexOf(":");
        String sanitized = string.substring(index + 1).replaceAll("[#:._]", " ").trim();
        return (String) Arrays.stream(sanitized.split(" ")).map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining(" "));
    }

    public static List<Object> asList(Object o) {
        if (o instanceof List) {
            return cast(o);
        } else {
            return o instanceof Object[] ? new ArrayList(Arrays.asList((Object[]) o)) : new ArrayList(Collections.singletonList(o));
        }
    }

    public static boolean matchesIngredient(Ingredient filter, Ingredient ingredient) {
        return Arrays.stream(filter.getItems()).anyMatch(ingredient);
    }
}