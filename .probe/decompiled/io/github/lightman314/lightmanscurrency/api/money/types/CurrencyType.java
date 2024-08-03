package io.github.lightman314.lightmanscurrency.api.money.types;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValueParser;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class CurrencyType {

    private final ResourceLocation type;

    protected CurrencyType(@Nonnull ResourceLocation type) {
        this.type = type;
    }

    public final ResourceLocation getType() {
        return this.type;
    }

    public final MoneyValue sumValues(@Nonnull List<MoneyValue> values) {
        if (values.isEmpty()) {
            return MoneyValue.empty();
        } else {
            return values.size() == 1 ? (MoneyValue) values.get(0) : this.sumValuesInternal(values);
        }
    }

    @Nonnull
    protected abstract MoneyValue sumValuesInternal(@Nonnull List<MoneyValue> var1);

    @Nullable
    public abstract IPlayerMoneyHandler createMoneyHandlerForPlayer(@Nonnull Player var1);

    @Nullable
    public abstract IMoneyHandler createMoneyHandlerForContainer(@Nonnull Container var1, @Nonnull Consumer<ItemStack> var2);

    @Nullable
    public IMoneyHandler createMoneyHandlerForATM(@Nonnull Player player, @Nonnull Container container) {
        return this.createMoneyHandlerForContainer(container, s -> ItemHandlerHelper.giveItemToPlayer(player, s));
    }

    public abstract MoneyValue loadMoneyValue(@Nonnull CompoundTag var1);

    public abstract MoneyValue loadMoneyValueJson(@Nonnull JsonObject var1);

    @Nonnull
    public abstract MoneyValueParser getValueParser();

    @OnlyIn(Dist.CLIENT)
    public abstract List<Object> getInputHandlers(@Nullable Player var1);
}