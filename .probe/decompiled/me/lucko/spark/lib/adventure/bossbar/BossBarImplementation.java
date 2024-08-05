package me.lucko.spark.lib.adventure.bossbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface BossBarImplementation {

    @Internal
    @NotNull
    static <I extends BossBarImplementation> I get(@NotNull final BossBar bar, @NotNull final Class<I> type) {
        return BossBarImpl.ImplementationAccessor.get(bar, type);
    }

    @Internal
    public interface Provider {

        @Internal
        @NotNull
        BossBarImplementation create(@NotNull final BossBar bar);
    }
}