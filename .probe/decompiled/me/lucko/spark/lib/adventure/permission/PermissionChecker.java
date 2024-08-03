package me.lucko.spark.lib.adventure.permission;

import java.util.function.Predicate;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.pointer.Pointer;
import me.lucko.spark.lib.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;

public interface PermissionChecker extends Predicate<String> {

    Pointer<PermissionChecker> POINTER = Pointer.pointer(PermissionChecker.class, Key.key("adventure", "permission"));

    @NotNull
    static PermissionChecker always(final TriState state) {
        if (state == TriState.TRUE) {
            return PermissionCheckers.TRUE;
        } else {
            return state == TriState.FALSE ? PermissionCheckers.FALSE : PermissionCheckers.NOT_SET;
        }
    }

    @NotNull
    TriState value(final String permission);

    default boolean test(final String permission) {
        return this.value(permission) == TriState.TRUE;
    }
}