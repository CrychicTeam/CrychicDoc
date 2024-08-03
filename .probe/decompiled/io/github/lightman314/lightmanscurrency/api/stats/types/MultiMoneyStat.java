package io.github.lightman314.lightmanscurrency.api.stats.types;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyStorage;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.stats.StatType;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class MultiMoneyStat extends StatType<MoneyView, MoneyValue> {

    public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "multi_money");

    public static final StatType<MoneyView, MoneyValue> INSTANCE = new MultiMoneyStat();

    private MultiMoneyStat() {
    }

    @Nonnull
    @Override
    public ResourceLocation getID() {
        return TYPE;
    }

    @Nonnull
    @Override
    public StatType.Instance<MoneyView, MoneyValue> create() {
        return new MultiMoneyStat.MMInstance();
    }

    protected static class MMInstance extends StatType.Instance<MoneyView, MoneyValue> {

        private final MoneyStorage data = new MoneyStorage(() -> {
        });

        @Nonnull
        @Override
        protected StatType<MoneyView, MoneyValue> getType() {
            return MultiMoneyStat.INSTANCE;
        }

        @Override
        protected void saveAdditional(@Nonnull CompoundTag tag) {
            tag.put("Value", this.data.save());
        }

        @Override
        public void load(@Nonnull CompoundTag tag) {
            this.data.load(tag.getList("Value", 10));
        }

        public MoneyView get() {
            return this.data.getStoredMoney();
        }

        protected void addInternal(@Nonnull MoneyValue addAmount) {
            this.data.addValue(addAmount);
        }

        @Override
        public void clear() {
            this.data.clear();
        }

        @Override
        public Object getDisplay() {
            return this.data.getRandomValueText();
        }
    }
}