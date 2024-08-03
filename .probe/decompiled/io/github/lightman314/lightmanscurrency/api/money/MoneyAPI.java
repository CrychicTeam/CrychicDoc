package io.github.lightman314.lightmanscurrency.api.money;

import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.types.CurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.PlayerMoneyHolder;
import io.github.lightman314.lightmanscurrency.common.impl.MoneyAPIImpl;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class MoneyAPI {

    public static final String MODID = "lightmanscurrency";

    public static final MoneyAPI API = MoneyAPIImpl.INSTANCE;

    @Nonnull
    public abstract List<CurrencyType> AllCurrencyTypes();

    @Deprecated(since = "2.2.0.4")
    @Nonnull
    public static List<CurrencyType> getAllCurrencyTypes() {
        return API.AllCurrencyTypes();
    }

    @Nullable
    public abstract CurrencyType GetRegisteredCurrencyType(@Nonnull ResourceLocation var1);

    @Deprecated(since = "2.2.0.4")
    @Nullable
    public static CurrencyType getCurrencyType(@Nonnull ResourceLocation id) {
        return API.GetRegisteredCurrencyType(id);
    }

    public abstract void RegisterCurrencyType(@Nonnull CurrencyType var1);

    @Deprecated(since = "2.2.0.4")
    public static void registerCurrencyType(@Nonnull CurrencyType type) {
        API.RegisterCurrencyType(type);
    }

    @Nonnull
    public abstract IMoneyHolder GetPlayersMoneyHandler(@Nonnull Player var1);

    @Deprecated(since = "2.2.0.4")
    @Nonnull
    public static PlayerMoneyHolder getPlayersMoneyHolder(@Nonnull Player player) {
        return new PlayerMoneyHolder(API.GetPlayersMoneyHandler(player));
    }

    @Deprecated(since = "2.2.0.4")
    @Nonnull
    public static MoneyView getPlayersAvailableFunds(@Nonnull Player player) {
        return API.GetPlayersMoneyHandler(player).getStoredMoney();
    }

    @Deprecated(since = "2.2.0.4")
    public static boolean canPlayerAfford(@Nonnull Player player, @Nonnull MoneyValue price) {
        return API.GetPlayersMoneyHandler(player).getStoredMoney().containsValue(price);
    }

    @Deprecated(since = "2.2.0.4")
    public static void giveMoneyToPlayer(@Nonnull Player player, @Nonnull MoneyValue value) {
        IMoneyHolder holder = API.GetPlayersMoneyHandler(player);
        holder.insertMoney(value, false);
    }

    @Deprecated(since = "2.2.0.4")
    public static boolean takeMoneyFromPlayer(@Nonnull Player player, @Nonnull MoneyValue value) {
        IMoneyHolder holder = API.GetPlayersMoneyHandler(player);
        if (holder.getStoredMoney().containsValue(value) && holder.extractMoney(value, true).isEmpty()) {
            holder.extractMoney(value, false);
            return true;
        } else {
            return false;
        }
    }

    @Nonnull
    public final IMoneyHandler GetContainersMoneyHandler(@Nonnull Container container, @Nonnull Player player) {
        return this.CreateContainersMoneyHandler(container, s -> ItemHandlerHelper.giveItemToPlayer(player, s));
    }

    @Nonnull
    public final IMoneyHandler GetContainersMoneyHandler(@Nonnull Container container, @Nonnull Consumer<ItemStack> overflowHandler) {
        return this.CreateContainersMoneyHandler(container, overflowHandler);
    }

    protected abstract IMoneyHandler CreateContainersMoneyHandler(@Nonnull Container var1, @Nonnull Consumer<ItemStack> var2);

    @Nonnull
    public abstract IMoneyHandler GetATMMoneyHandler(@Nonnull Player var1, @Nonnull Container var2);

    @Deprecated(since = "2.2.0.4")
    @Nonnull
    public static MoneyView valueOfContainer(@Nonnull List<ItemStack> container) {
        return API.GetContainersMoneyHandler(InventoryUtil.buildInventory(container), s -> {
        }).getStoredMoney();
    }

    @Deprecated(since = "2.2.0.4")
    @Nonnull
    public static MoneyView valueOfContainer(@Nonnull Container container) {
        return API.GetContainersMoneyHandler(container, s -> {
        }).getStoredMoney();
    }

    @Deprecated(since = "2.2.0.4")
    public static boolean canAddMoneyToContainer(@Nonnull Container container, @Nonnull MoneyValue moneyToAdd) {
        return API.GetContainersMoneyHandler(container, s -> {
        }).isMoneyTypeValid(moneyToAdd);
    }

    @Deprecated(since = "2.2.0.4")
    public static boolean addMoneyToContainer(@Nonnull Container container, @Nonnull Player player, @Nonnull MoneyValue moneyToAdd) {
        IMoneyHandler handler = API.GetContainersMoneyHandler(container, player);
        if (handler.insertMoney(moneyToAdd, true).isEmpty()) {
            handler.insertMoney(moneyToAdd, false);
            return true;
        } else {
            return false;
        }
    }

    @Deprecated(since = "2.2.0.4")
    public static boolean addMoneyToContainer(@Nonnull Container container, @Nonnull Consumer<ItemStack> overflowHandler, @Nonnull MoneyValue moneyToAdd) {
        IMoneyHandler handler = API.GetContainersMoneyHandler(container, overflowHandler);
        if (handler.insertMoney(moneyToAdd, true).isEmpty()) {
            handler.insertMoney(moneyToAdd, false);
            return true;
        } else {
            return false;
        }
    }

    @Deprecated(since = "2.2.0.4")
    public static boolean takeMoneyFromContainer(@Nonnull Container container, @Nonnull Player player, @Nonnull MoneyValue moneyToTake) {
        IMoneyHandler handler = API.GetContainersMoneyHandler(container, player);
        if (handler.extractMoney(moneyToTake, true).isEmpty()) {
            handler.extractMoney(moneyToTake, false);
            return true;
        } else {
            return false;
        }
    }

    @Deprecated(since = "2.2.0.4")
    public static boolean takeMoneyFromContainer(@Nonnull Container container, @Nonnull Consumer<ItemStack> overflowHandler, @Nonnull MoneyValue moneyToTake) {
        IMoneyHandler handler = API.GetContainersMoneyHandler(container, overflowHandler);
        if (handler.extractMoney(moneyToTake, true).isEmpty()) {
            handler.extractMoney(moneyToTake, false);
            return true;
        } else {
            return false;
        }
    }
}