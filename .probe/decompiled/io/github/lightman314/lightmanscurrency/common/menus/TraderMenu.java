package io.github.lightman314.lightmanscurrency.common.menus;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.traders.ITraderSource;
import io.github.lightman314.lightmanscurrency.api.traders.InteractionSlotData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TradeResult;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.IMoneyCollectionMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.customer.ITraderMenu;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.slots.CoinSlot;
import io.github.lightman314.lightmanscurrency.common.menus.slots.InteractionSlot;
import io.github.lightman314.lightmanscurrency.common.menus.validation.EasyMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.IValidatedMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class TraderMenu extends EasyMenu implements IValidatedMenu, ITraderMenu, IMoneyCollectionMenu {

    private final Supplier<ITraderSource> traderSource;

    private final Map<Long, TradeContext> contextCache = new HashMap();

    public static final int SLOT_OFFSET = 15;

    InteractionSlot interactionSlot;

    private final Container coins;

    List<Slot> coinSlots = new ArrayList();

    private final MenuValidator validator;

    @Nullable
    @Override
    public ITraderSource getTraderSource() {
        return (ITraderSource) this.traderSource.get();
    }

    @Nonnull
    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Nonnull
    @Override
    public List<Slot> getSlots() {
        return ImmutableList.copyOf(this.f_38839_);
    }

    @Nonnull
    @Override
    public ItemStack getHeldItem() {
        return this.m_142621_();
    }

    @Override
    public void setHeldItem(@Nonnull ItemStack stack) {
        this.m_142503_(stack);
    }

    public InteractionSlot getInteractionSlot() {
        return this.interactionSlot;
    }

    public List<Slot> getCoinSlots() {
        return this.coinSlots;
    }

    @Nonnull
    @Override
    public MenuValidator getValidator() {
        return this.validator;
    }

    public TraderMenu(int windowID, Inventory inventory, long traderID, MenuValidator validator) {
        this(ModMenus.TRADER.get(), windowID, inventory, () -> TraderSaveData.GetTrader(inventory.player.m_9236_().isClientSide, traderID), validator);
    }

    protected TraderMenu(MenuType<?> type, int windowID, Inventory inventory, Supplier<ITraderSource> traderSource, MenuValidator validator) {
        super(type, windowID, inventory);
        this.validator = validator;
        this.traderSource = traderSource;
        this.coins = new SimpleContainer(5);
        this.addValidator(this::traderSourceValid);
        this.addValidator(this.validator);
        this.init(inventory);
        for (TraderData trader : ((ITraderSource) this.traderSource.get()).getTraders()) {
            if (trader != null) {
                trader.userOpen(this.player);
            }
        }
    }

    @Nonnull
    @Override
    public TradeContext getContext(@Nullable TraderData trader) {
        long traderID = trader == null ? -1L : trader.getID();
        if (!this.contextCache.containsKey(traderID) || ((TradeContext) this.contextCache.get(traderID)).getTrader() != trader) {
            this.contextCache.put(traderID, TradeContext.create(trader, this.player).withCoinSlots(this.coins).withInteractionSlot(this.interactionSlot).build());
        }
        return (TradeContext) this.contextCache.get(traderID);
    }

    protected void init(Inventory inventory) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.m_38897_(new Slot(inventory, x + y * 9 + 9, 23 + x * 18, 154 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.m_38897_(new Slot(inventory, x, 23 + x * 18, 212));
        }
        for (int x = 0; x < this.coins.getContainerSize(); x++) {
            this.coinSlots.add(this.m_38897_(new CoinSlot(this.coins, x, 23 + (x + 4) * 18, 122)));
        }
        List<InteractionSlotData> slotData = new ArrayList();
        for (TraderData trader : ((ITraderSource) this.traderSource.get()).getTraders()) {
            trader.addInteractionSlots(slotData);
        }
        this.interactionSlot = new InteractionSlot(slotData, 23, 122);
        this.m_38897_(this.interactionSlot);
    }

    private boolean traderSourceValid() {
        return this.traderSource != null && this.traderSource.get() != null && ((ITraderSource) this.traderSource.get()).getTraders() != null && !((ITraderSource) this.traderSource.get()).getTraders().isEmpty();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.m_6877_(player);
        this.m_150411_(player, this.coins);
        this.m_150411_(player, this.interactionSlot.f_40218_);
        if (this.traderSource.get() != null) {
            for (TraderData trader : ((ITraderSource) this.traderSource.get()).getTraders()) {
                if (trader != null) {
                    trader.userClose(this.player);
                }
            }
        }
        this.contextCache.values().forEach(TradeContext::clearCache);
    }

    public void ExecuteTrade(int traderIndex, int tradeIndex) {
        ITraderSource traderSource = (ITraderSource) this.traderSource.get();
        if (traderSource == null) {
            this.player.closeContainer();
        } else {
            List<TraderData> traderList = traderSource.getTraders();
            if (traderIndex >= 0 && traderIndex < traderList.size()) {
                TraderData trader = (TraderData) traderSource.getTraders().get(traderIndex);
                if (trader == null) {
                    LightmansCurrency.LogWarning("Trader at index " + traderIndex + " is null.");
                    return;
                }
                TradeResult result = trader.TryExecuteTrade(this.getContext(trader), tradeIndex);
                if (result.hasMessage()) {
                    LightmansCurrency.LogDebug(result.getMessage().getString());
                }
            } else {
                LightmansCurrency.LogWarning("Trader " + traderIndex + " is not a valid trader index.");
            }
        }
    }

    public boolean isSingleTrader() {
        ITraderSource tradeSource = (ITraderSource) this.traderSource.get();
        if (tradeSource == null) {
            this.player.closeContainer();
            return false;
        } else {
            return tradeSource.isSingleTrader() && tradeSource.getTraders().size() == 1;
        }
    }

    public TraderData getSingleTrader() {
        return this.isSingleTrader() ? ((ITraderSource) this.traderSource.get()).getSingleTrader() : null;
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerEntity, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < 36) {
                if (!this.m_38903_(slotStack, 36, this.f_38839_.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < this.f_38839_.size() && !this.m_38903_(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clickedStack;
    }

    @Override
    public void CollectStoredMoney() {
        if (this.isSingleTrader()) {
            LightmansCurrency.LogInfo("Attempting to collect coins from trader.");
            TraderData trader = this.getSingleTrader();
            trader.CollectStoredMoney(this.player);
        }
    }

    public static class TraderMenuAllNetwork extends TraderMenu {

        public TraderMenuAllNetwork(int windowID, Inventory inventory, MenuValidator validator) {
            super(ModMenus.TRADER_NETWORK_ALL.get(), windowID, inventory, ITraderSource.UniversalTraderSource(inventory.player.m_9236_().isClientSide), validator);
        }
    }

    public static class TraderMenuBlockSource extends TraderMenu {

        public TraderMenuBlockSource(int windowID, Inventory inventory, BlockPos pos, MenuValidator validator) {
            super(ModMenus.TRADER_BLOCK.get(), windowID, inventory, () -> {
                BlockEntity patt8582$temp = inventory.player.m_9236_().getBlockEntity(pos);
                return patt8582$temp instanceof ITraderSource ? (ITraderSource) patt8582$temp : null;
            }, validator);
        }
    }
}