package io.github.lightman314.lightmanscurrency.api.money.coins.display;

import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class ValueDisplayData {

    private ChainData parent = null;

    @Nonnull
    public final String getChain() {
        return this.parent == null ? "" : this.parent.chain;
    }

    public final void setParent(@Nonnull ChainData parent) {
        if (this.parent == null) {
            this.parent = parent;
        }
    }

    protected final ChainData getParent() {
        return this.parent;
    }

    @Nonnull
    public final ResourceLocation getType() {
        return this.getSerializer().getType();
    }

    @Nonnull
    public abstract ValueDisplaySerializer getSerializer();

    @Nonnull
    public abstract MutableComponent formatValue(@Nonnull CoinValue var1, @Nonnull MutableComponent var2);

    public abstract void formatCoinTooltip(@Nonnull ItemStack var1, @Nonnull List<Component> var2);

    @Nonnull
    public MoneyValue parseDisplayInput(double displayInput) {
        return CoinValue.fromNumber(this.getChain(), (long) displayInput);
    }
}