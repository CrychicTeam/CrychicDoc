package io.github.lightman314.lightmanscurrency.common.impl;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.capability.money.MoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.types.CurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.types.IPlayerMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class MoneyAPIImpl extends MoneyAPI {

    public static final MoneyAPI INSTANCE = new MoneyAPIImpl();

    private final Map<ResourceLocation, CurrencyType> registeredCurrencyTypes = new HashMap();

    private final Map<UUID, PlayerMoneyHolder> clientPlayerCache = new HashMap();

    private final Map<UUID, PlayerMoneyHolder> serverPlayerCache = new HashMap();

    private MoneyAPIImpl() {
    }

    @Nonnull
    @Override
    public List<CurrencyType> AllCurrencyTypes() {
        return ImmutableList.copyOf(this.registeredCurrencyTypes.values());
    }

    @Nullable
    @Override
    public CurrencyType GetRegisteredCurrencyType(@Nonnull ResourceLocation id) {
        return (CurrencyType) this.registeredCurrencyTypes.get(id);
    }

    @Override
    public void RegisterCurrencyType(@Nonnull CurrencyType type) {
        if (this.registeredCurrencyTypes.containsKey(type.getType())) {
            CurrencyType existingType = (CurrencyType) this.registeredCurrencyTypes.get(type.getType());
            if (existingType == type) {
                LightmansCurrency.LogWarning("Money Type " + type.getType() + " was registered twice!");
            } else {
                LightmansCurrency.LogError("Tried to register Money Type " + type.getType() + ", but another type has already been registered under that id!");
            }
        } else {
            this.registeredCurrencyTypes.put(type.getType(), type);
            LightmansCurrency.LogDebug("Registered Currency Type: " + type.getType());
        }
    }

    @Nonnull
    @Override
    public IMoneyHolder GetPlayersMoneyHandler(@Nonnull Player player) {
        Map<UUID, PlayerMoneyHolder> cache = player.isLocalPlayer() ? this.clientPlayerCache : this.serverPlayerCache;
        if (!cache.containsKey(player.m_20148_())) {
            List<IPlayerMoneyHandler> handlers = new ArrayList();
            for (CurrencyType type : this.registeredCurrencyTypes.values()) {
                IPlayerMoneyHandler h = type.createMoneyHandlerForPlayer(player);
                if (h != null) {
                    handlers.add(h);
                }
            }
            cache.put(player.m_20148_(), new PlayerMoneyHolder(handlers));
        }
        return ((PlayerMoneyHolder) cache.get(player.m_20148_())).updatePlayer(player);
    }

    @Override
    protected IMoneyHandler CreateContainersMoneyHandler(@Nonnull Container container, @Nonnull Consumer<ItemStack> overflowHandler) {
        List<IMoneyHandler> handlers = new ArrayList();
        for (CurrencyType type : this.registeredCurrencyTypes.values()) {
            IMoneyHandler h = type.createMoneyHandlerForContainer(container, overflowHandler);
            if (h != null) {
                handlers.add(h);
            }
        }
        return MoneyHandler.combine(handlers);
    }

    @Nonnull
    @Override
    public IMoneyHandler GetATMMoneyHandler(@Nonnull Player player, @Nonnull Container container) {
        List<IMoneyHandler> handlers = new ArrayList();
        for (CurrencyType type : this.registeredCurrencyTypes.values()) {
            IMoneyHandler h = type.createMoneyHandlerForATM(player, container);
            if (h != null) {
                handlers.add(h);
            }
        }
        return MoneyHandler.combine(handlers);
    }
}