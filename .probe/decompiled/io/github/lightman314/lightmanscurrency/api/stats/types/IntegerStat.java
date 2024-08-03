package io.github.lightman314.lightmanscurrency.api.stats.types;

import io.github.lightman314.lightmanscurrency.api.stats.StatType;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class IntegerStat extends StatType<Integer, Integer> {

    public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "basic_int");

    public static final StatType<Integer, Integer> INSTANCE = new IntegerStat();

    @Nonnull
    @Override
    public StatType.Instance<Integer, Integer> create() {
        return new IntegerStat.IntInstance();
    }

    @Nonnull
    @Override
    public ResourceLocation getID() {
        return TYPE;
    }

    private IntegerStat() {
    }

    protected static class IntInstance extends StatType.Instance<Integer, Integer> {

        private int value = 0;

        @Nonnull
        @Override
        protected StatType<Integer, Integer> getType() {
            return IntegerStat.INSTANCE;
        }

        @Override
        protected void saveAdditional(@Nonnull CompoundTag tag) {
            tag.putInt("Value", this.value);
        }

        @Override
        public void load(@Nonnull CompoundTag tag) {
            this.value = tag.getInt("Value");
        }

        public Integer get() {
            return this.value;
        }

        protected void addInternal(@Nonnull Integer addAmount) {
            this.value = this.value + addAmount;
        }

        @Override
        public void clear() {
            this.value = 0;
        }

        @Override
        public Object getDisplay() {
            return this.value;
        }
    }
}