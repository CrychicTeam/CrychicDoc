package io.github.lightman314.lightmanscurrency.api.events;

import com.google.common.collect.ImmutableMap;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.eventbus.api.Event;

public final class BuildDefaultMoneyDataEvent extends Event {

    private static final List<CoinEntry> existingEntries = new ArrayList();

    private final Map<String, ChainData.Builder> builders = new HashMap();

    public static List<CoinEntry> getExistingEntries() {
        return existingEntries;
    }

    public BuildDefaultMoneyDataEvent() {
        existingEntries.clear();
    }

    public Map<String, ChainData.Builder> getFinalResult() {
        return ImmutableMap.copyOf(this.builders);
    }

    @Nullable
    public ChainData.Builder getExistingBuilder(@Nonnull String chain) {
        return (ChainData.Builder) this.builders.get(chain);
    }

    public boolean exists(@Nonnull String chain) {
        return this.builders.containsKey(Objects.requireNonNull(chain));
    }

    public boolean available(@Nonnull String chain) {
        return !this.builders.containsKey(Objects.requireNonNull(chain));
    }

    public void addDefault(@Nonnull ChainData.Builder builder) {
        this.addDefault(builder, false);
    }

    public void addDefault(@Nonnull ChainData.Builder builder, boolean allowOverride) {
        if (this.builders.containsKey(((ChainData.Builder) Objects.requireNonNull(builder)).chain) && !allowOverride) {
            throw new IllegalArgumentException("Builder already exists for Money Data chain '" + builder.chain + "'!");
        } else {
            this.builders.put(builder.chain, builder);
        }
    }
}