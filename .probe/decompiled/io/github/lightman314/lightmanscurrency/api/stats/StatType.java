package io.github.lightman314.lightmanscurrency.api.stats;

import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class StatType<A, B> {

    private static final Map<ResourceLocation, StatType<?, ?>> REGISTRY = new HashMap();

    @Nonnull
    public static String getTranslationKey(@Nonnull String statKey) {
        return "statistic.lightmanscurrency." + statKey;
    }

    public static void register(@Nonnull StatType<?, ?> type) {
        REGISTRY.put(type.getID(), type);
    }

    public static StatType<?, ?> getID(@Nonnull ResourceLocation type) {
        return (StatType<?, ?>) REGISTRY.get(type);
    }

    @Nonnull
    public abstract ResourceLocation getID();

    @Nonnull
    public abstract StatType.Instance<A, B> create();

    @Nonnull
    public final StatKey<A, B> createKey(@Nonnull String statKey) {
        return StatKey.create(statKey, this);
    }

    public abstract static class Instance<A, B> implements IClientTracker {

        private StatTracker parent = null;

        protected Instance() {
        }

        @Override
        public final boolean isClient() {
            return this.parent.isClient();
        }

        @Nonnull
        protected abstract StatType<A, B> getType();

        @Nonnull
        protected final ResourceLocation getID() {
            return this.getType().getID();
        }

        @Nonnull
        public final CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            this.saveAdditional(tag);
            tag.putString("Type", this.getType().getID().toString());
            return tag;
        }

        protected abstract void saveAdditional(@Nonnull CompoundTag var1);

        public abstract void load(@Nonnull CompoundTag var1);

        public abstract A get();

        public final void add(@Nonnull B addAmount) {
            this.addInternal(addAmount);
            if (this.parent != null) {
                this.parent.setChanged();
            }
        }

        protected abstract void addInternal(@Nonnull B var1);

        public abstract void clear();

        public void setParent(@Nonnull StatTracker parent) {
            this.parent = parent;
        }

        public abstract Object getDisplay();

        @Nonnull
        public MutableComponent getInfoText(@Nonnull String statKey) {
            return Component.translatable(StatType.getTranslationKey(statKey), this.getDisplay());
        }
    }
}