package io.github.lightman314.lightmanscurrency.api.events;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.eventbus.api.Event;

public abstract class ChainDataReloadedEvent extends Event {

    @Nonnull
    public abstract Map<String, ChainData> getChainMap();

    public boolean chainExists(@Nonnull String chain) {
        return this.getChainMap().containsKey(chain);
    }

    @Nullable
    public ChainData getChain(@Nonnull String chain) {
        return (ChainData) this.getChainMap().get(chain);
    }

    @Nonnull
    public final List<ChainData> getChains() {
        return ImmutableList.copyOf(this.getChainMap().values());
    }

    public static class Post extends ChainDataReloadedEvent {

        private final Map<String, ChainData> dataMap;

        @Nonnull
        @Override
        public Map<String, ChainData> getChainMap() {
            return this.dataMap;
        }

        public Post(@Nonnull Map<String, ChainData> dataMap) {
            this.dataMap = dataMap;
        }
    }

    public static class Pre extends ChainDataReloadedEvent {

        private final Map<String, ChainData> dataMap;

        @Nonnull
        @Override
        public Map<String, ChainData> getChainMap() {
            return ImmutableMap.copyOf(this.dataMap);
        }

        public Pre(Map<String, ChainData> dataMap) {
            this.dataMap = new HashMap(dataMap);
        }

        public void addEntry(@Nonnull ChainData chain) {
            this.addEntry(chain, false);
        }

        public void addEntry(@Nonnull ChainData chain, boolean allowOverride) {
            if (!this.dataMap.containsKey(chain.chain) || allowOverride) {
                this.dataMap.put(chain.chain, chain);
            }
        }

        public void removeEntry(@Nonnull String chain) {
            this.dataMap.remove(chain);
        }
    }
}