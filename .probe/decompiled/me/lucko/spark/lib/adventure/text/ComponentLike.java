package me.lucko.spark.lib.adventure.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ComponentLike {

    @NotNull
    static List<Component> asComponents(@NotNull final List<? extends ComponentLike> likes) {
        return asComponents(likes, null);
    }

    @NotNull
    static List<Component> asComponents(@NotNull final List<? extends ComponentLike> likes, @Nullable final Predicate<? super Component> filter) {
        Objects.requireNonNull(likes, "likes");
        int size = likes.size();
        if (size == 0) {
            return Collections.emptyList();
        } else {
            ArrayList<Component> components = null;
            for (int i = 0; i < size; i++) {
                ComponentLike like = (ComponentLike) likes.get(i);
                if (like == null) {
                    throw new NullPointerException("likes[" + i + "]");
                }
                Component component = like.asComponent();
                if (filter == null || filter.test(component)) {
                    if (components == null) {
                        components = new ArrayList(size);
                    }
                    components.add(component);
                }
            }
            if (components == null) {
                return Collections.emptyList();
            } else {
                components.trimToSize();
                return Collections.unmodifiableList(components);
            }
        }
    }

    @Nullable
    static Component unbox(@Nullable final ComponentLike like) {
        return like != null ? like.asComponent() : null;
    }

    @Contract(pure = true)
    @NotNull
    Component asComponent();
}