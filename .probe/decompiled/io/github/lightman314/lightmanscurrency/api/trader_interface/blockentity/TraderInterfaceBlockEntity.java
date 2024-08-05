package io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.IServerTicker;
import io.github.lightman314.lightmanscurrency.api.misc.blockentity.EasyBlockEntity;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.ownership.builtin.PlayerOwner;
import io.github.lightman314.lightmanscurrency.api.trader_interface.blocks.TraderInterfaceBlock;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TradeResult;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.upgrades.IUpgradeable;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.IDumpable;
import io.github.lightman314.lightmanscurrency.common.items.UpgradeItem;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import io.github.lightman314.lightmanscurrency.common.menus.providers.EasyMenuProvider;
import io.github.lightman314.lightmanscurrency.common.traderinterface.NetworkTradeReference;
import io.github.lightman314.lightmanscurrency.common.traderinterface.handlers.SidedHandler;
import io.github.lightman314.lightmanscurrency.common.upgrades.Upgrades;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.SpeedUpgrade;
import io.github.lightman314.lightmanscurrency.network.message.interfacebe.CPacketInterfaceHandlerMessage;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import io.github.lightman314.lightmanscurrency.util.EnumUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public abstract class TraderInterfaceBlockEntity extends EasyBlockEntity implements IUpgradeable, IDumpable, IServerTicker {

    public static final int INTERACTION_DELAY = 20;

    private boolean allowRemoval = false;

    public final OwnerData owner = new OwnerData(this, o -> this.OnOwnerChanged());

    List<SidedHandler<?>> handlers = new ArrayList();

    private TraderInterfaceBlockEntity.ActiveMode mode = TraderInterfaceBlockEntity.ActiveMode.DISABLED;

    private boolean onlineMode = false;

    private TraderInterfaceBlockEntity.InteractionType interaction = TraderInterfaceBlockEntity.InteractionType.TRADE;

    NetworkTradeReference reference = new NetworkTradeReference(this::isClient, this::deserializeTrade);

    private SimpleContainer upgradeSlots = new SimpleContainer(5);

    private TradeResult lastResult = TradeResult.SUCCESS;

    private int waitTimer = 20;

    private TradeContext cachedContext = null;

    public boolean allowRemoval() {
        return this.allowRemoval;
    }

    public void flagAsRemovable() {
        this.allowRemoval = true;
    }

    public void initOwner(Entity owner) {
        if (!this.owner.hasOwner() && owner instanceof Player player) {
            this.owner.SetOwner(PlayerOwner.of(player));
        }
    }

    private void OnOwnerChanged() {
        this.mode = TraderInterfaceBlockEntity.ActiveMode.DISABLED;
        this.cachedContext = null;
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveOwner(this.saveMode(new CompoundTag())));
        }
    }

    public PlayerReference getReferencedPlayer() {
        return this.owner.getPlayerForContext();
    }

    public MutableComponent getOwnerName() {
        return this.owner.getName();
    }

    @Nullable
    public IBankAccount getBankAccount() {
        BankReference reference = this.getAccountReference();
        return reference != null ? reference.get() : null;
    }

    @Nullable
    public BankReference getAccountReference() {
        return this.owner.getValidOwner().asBankReference();
    }

    public TraderInterfaceBlockEntity.ActiveMode getMode() {
        return this.mode;
    }

    public void setMode(TraderInterfaceBlockEntity.ActiveMode mode) {
        this.mode = mode;
        this.setModeDirty();
    }

    public boolean isOnlineMode() {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
        this.setOnlineModeDirty();
    }

    public TraderInterfaceBlockEntity.InteractionType getInteractionType() {
        return this.interaction;
    }

    public void setInteractionType(TraderInterfaceBlockEntity.InteractionType type) {
        if (this.getBlacklistedInteractions().contains(type)) {
            LightmansCurrency.LogInfo("Attempted to set interaction type to " + type.name() + ", but that type is blacklisted for this interface type (" + this.getClass().getName() + ").");
        } else {
            this.interaction = type;
            this.setInteractionDirty();
        }
    }

    public List<TraderInterfaceBlockEntity.InteractionType> getBlacklistedInteractions() {
        return new ArrayList();
    }

    public boolean hasTrader() {
        return this.getTrader() != null;
    }

    public TraderData getTrader() {
        TraderData trader = this.reference.getTrader();
        return this.interaction.requiresPermissions && !this.hasTraderPermissions(trader) ? null : trader;
    }

    public int getTradeIndex() {
        return this.reference.getTradeIndex();
    }

    public TradeData getReferencedTrade() {
        return this.reference.getLocalTrade();
    }

    public TradeData getTrueTrade() {
        return this.reference.getTrueTrade();
    }

    public Container getUpgradeInventory() {
        return this.upgradeSlots;
    }

    public void setUpgradeSlotsDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveUpgradeSlots(new CompoundTag()));
        }
    }

    public void setTrader(long traderID) {
        if (this.reference.getTraderID() != traderID) {
            this.reference.setTrader(traderID);
            this.reference.setTrade(-1);
            this.cachedContext = null;
            this.setTradeReferenceDirty();
        }
    }

    public void setTradeIndex(int tradeIndex) {
        this.reference.setTrade(tradeIndex);
        this.setTradeReferenceDirty();
    }

    public void acceptTradeChanges() {
        this.reference.refreshTrade();
        this.setTradeReferenceDirty();
    }

    public TradeResult mostRecentTradeResult() {
        return this.lastResult;
    }

    protected abstract TradeData deserializeTrade(CompoundTag var1);

    public boolean canAccess(Player player) {
        return this.owner.isMember(player);
    }

    public boolean isOwner(Player player) {
        return this.owner.isAdmin(player);
    }

    protected TraderInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setModeDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveMode(new CompoundTag()));
        }
    }

    public void setOnlineModeDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveOnlineMode(new CompoundTag()));
        }
    }

    public void setLastResultDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveLastResult(new CompoundTag()));
        }
    }

    protected abstract TradeContext.Builder buildTradeContext(TradeContext.Builder var1);

    public TradeContext getTradeContext() {
        if (this.cachedContext == null) {
            if (this.interaction.trades) {
                this.cachedContext = this.buildTradeContext(TradeContext.create(this.getTrader(), this.getReferencedPlayer()).withBankAccount(this.getAccountReference())).build();
            } else {
                this.cachedContext = TradeContext.createStorageMode(this.getTrader());
            }
        }
        return this.cachedContext;
    }

    protected final <H extends SidedHandler<?>> H addHandler(@Nonnull H handler) {
        handler.setParent(this);
        this.handlers.add(handler);
        return handler;
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        this.saveOwner(compound);
        this.saveMode(compound);
        this.saveOnlineMode(compound);
        this.saveInteraction(compound);
        this.saveLastResult(compound);
        this.saveReference(compound);
        this.saveUpgradeSlots(compound);
        for (SidedHandler<?> handler : this.handlers) {
            this.saveHandler(compound, handler);
        }
    }

    protected final CompoundTag saveOwner(CompoundTag compound) {
        if (this.owner != null) {
            compound.put("Owner", this.owner.save());
        }
        return compound;
    }

    protected final CompoundTag saveMode(CompoundTag compound) {
        compound.putString("Mode", this.mode.name());
        return compound;
    }

    protected final CompoundTag saveOnlineMode(CompoundTag compound) {
        compound.putBoolean("OnlineMode", this.onlineMode);
        return compound;
    }

    protected final CompoundTag saveInteraction(CompoundTag compound) {
        compound.putString("InteractionType", this.interaction.name());
        return compound;
    }

    protected final CompoundTag saveLastResult(CompoundTag compound) {
        compound.putString("LastResult", this.lastResult.name());
        return compound;
    }

    protected final CompoundTag saveReference(CompoundTag compound) {
        compound.put("Trade", this.reference.save());
        return compound;
    }

    protected final CompoundTag saveUpgradeSlots(CompoundTag compound) {
        InventoryUtil.saveAllItems("Upgrades", compound, this.upgradeSlots);
        return compound;
    }

    protected final CompoundTag saveHandler(CompoundTag compound, SidedHandler<?> handler) {
        compound.put(handler.getTag(), handler.save());
        return compound;
    }

    public void setHandlerDirty(SidedHandler<?> handler) {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveHandler(new CompoundTag(), handler));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("Owner", 10)) {
            this.owner.load(compound.getCompound("Owner"));
            this.cachedContext = null;
        }
        if (compound.contains("Mode")) {
            this.mode = EnumUtil.enumFromString(compound.getString("Mode"), TraderInterfaceBlockEntity.ActiveMode.values(), TraderInterfaceBlockEntity.ActiveMode.DISABLED);
        }
        if (compound.contains("OnlineMode")) {
            this.onlineMode = compound.getBoolean("OnlineMode");
        }
        if (compound.contains("InteractionType", 8)) {
            this.interaction = EnumUtil.enumFromString(compound.getString("InteractionType"), TraderInterfaceBlockEntity.InteractionType.values(), TraderInterfaceBlockEntity.InteractionType.TRADE);
            this.cachedContext = null;
        }
        if (compound.contains("Trade", 10)) {
            this.reference.load(compound.getCompound("Trade"));
        }
        if (compound.contains("Upgrades")) {
            this.upgradeSlots = InventoryUtil.loadAllItems("Upgrades", compound, 5);
        }
        for (SidedHandler<?> handler : this.handlers) {
            if (compound.contains(handler.getTag(), 10)) {
                handler.load(compound.getCompound(handler.getTag()));
            }
        }
    }

    public void setInteractionDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveInteraction(new CompoundTag()));
        }
    }

    @NotNull
    public <C> LazyOptional<C> getCapability(@Nonnull Capability<C> cap, @Nullable Direction side) {
        Direction relativeSide = this.getRelativeSide(side);
        for (SidedHandler<?> sidedHandler : this.handlers) {
            Object handler = sidedHandler.getHandler(relativeSide);
            if (cap == ForgeCapabilities.ITEM_HANDLER && handler instanceof IItemHandler) {
                return LazyOptional.<IItemHandler>of(() -> (IItemHandler) handler).cast();
            }
            if (cap == ForgeCapabilities.FLUID_HANDLER && handler instanceof IFluidHandler) {
                return LazyOptional.<IFluidHandler>of(() -> (IFluidHandler) handler).cast();
            }
            if (cap == ForgeCapabilities.ENERGY && handler instanceof IEnergyStorage) {
                return LazyOptional.<IEnergyStorage>of(() -> (IEnergyStorage) handler).cast();
            }
        }
        return super.getCapability(cap, side);
    }

    protected final Direction getRelativeSide(Direction side) {
        Direction relativeSide = side;
        if (side != null & this.m_58900_().m_60734_() instanceof IRotatableBlock) {
            relativeSide = IRotatableBlock.getRelativeSide(((IRotatableBlock) this.m_58900_().m_60734_()).getFacing(this.m_58900_()), side);
        }
        return relativeSide;
    }

    public void sendHandlerMessage(ResourceLocation type, CompoundTag message) {
        if (this.isClient()) {
            new CPacketInterfaceHandlerMessage(this.f_58858_, type, message).send();
        }
    }

    public void receiveHandlerMessage(ResourceLocation type, Player player, CompoundTag message) {
        if (this.canAccess(player)) {
            for (SidedHandler<?> handler : this.handlers) {
                if (handler.getType().equals(type)) {
                    handler.receiveMessage(message);
                }
            }
        }
    }

    public void setTradeReferenceDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveReference(new CompoundTag()));
        }
    }

    public TradeResult interactWithTrader() {
        TradeContext tradeContext = this.getTradeContext();
        TraderData trader = this.getTrader();
        if (trader != null) {
            this.lastResult = trader.TryExecuteTrade(tradeContext, this.reference.getTradeIndex());
        } else {
            this.lastResult = TradeResult.FAIL_NULL;
        }
        this.setLastResultDirty();
        return this.lastResult;
    }

    public boolean isActive() {
        return this.mode.isActive(this) && this.onlineCheck();
    }

    public boolean onlineCheck() {
        if (this.isClient()) {
            return false;
        } else {
            return !this.onlineMode ? true : this.owner.getValidOwner().isOnline();
        }
    }

    public final boolean hasTraderPermissions(TraderData trader) {
        if (trader == null) {
            return false;
        } else {
            return this.owner.getValidOwner() instanceof PlayerOwner po ? trader.hasPermission(po.player, "interactionLink") : trader.getOwner().getValidOwner().matches(this.owner.getValidOwner());
        }
    }

    @Override
    public void serverTick() {
        if (this.isActive() && --this.waitTimer <= 0) {
            this.waitTimer = this.getInteractionDelay();
            if (this.interaction.requiresPermissions) {
                if (!this.validTrader() || !this.hasTraderPermissions(this.getTrader())) {
                    return;
                }
                if (this.interaction.drains) {
                    this.drainTick();
                }
                if (this.interaction.restocks) {
                    this.restockTick();
                }
            } else if (this.interaction.trades) {
                if (!this.validTrade()) {
                    return;
                }
                this.tradeTick();
            }
            if (this.hasHopperUpgrade()) {
                this.hopperTick();
            }
        }
    }

    public boolean validTrader() {
        TraderData trader = this.getTrader();
        return trader != null && this.validTraderType(trader);
    }

    public boolean validTrade() {
        TradeData expectedTrade = this.getReferencedTrade();
        TradeData trueTrade = this.getTrueTrade();
        return expectedTrade != null && trueTrade != null ? trueTrade.AcceptableDifferences(trueTrade.compare(expectedTrade)) : false;
    }

    public abstract boolean validTraderType(TraderData var1);

    protected abstract void drainTick();

    protected abstract void restockTick();

    protected abstract void tradeTick();

    protected abstract void hopperTick();

    public void openMenu(Player player) {
        if (this.canAccess(player)) {
            MenuProvider provider = this.getMenuProvider();
            if (provider == null) {
                return;
            }
            NetworkHooks.openScreen((ServerPlayer) player, provider, this.f_58858_);
        }
    }

    protected MenuProvider getMenuProvider() {
        return new TraderInterfaceBlockEntity.InterfaceMenuProvider(this);
    }

    protected int getInteractionDelay() {
        int delay = 20;
        for (int i = 0; i < this.upgradeSlots.getContainerSize() && delay > 1; i++) {
            ItemStack stack = this.upgradeSlots.getItem(i);
            Item var5 = stack.getItem();
            if (var5 instanceof UpgradeItem) {
                UpgradeItem upgrade = (UpgradeItem) var5;
                if (upgrade.getUpgradeType() instanceof SpeedUpgrade) {
                    delay -= UpgradeItem.getUpgradeData(stack).getIntValue(SpeedUpgrade.DELAY_AMOUNT);
                }
            }
        }
        return delay;
    }

    public abstract void initMenuTabs(TraderInterfaceMenu var1);

    @Override
    public boolean allowUpgrade(@Nonnull UpgradeType type) {
        return type == Upgrades.SPEED || type == Upgrades.HOPPER && this.allowHopperUpgrade() && !this.hasHopperUpgrade() || this.allowAdditionalUpgrade(type);
    }

    protected boolean allowHopperUpgrade() {
        return true;
    }

    protected boolean allowAdditionalUpgrade(UpgradeType type) {
        return false;
    }

    protected final boolean hasHopperUpgrade() {
        return UpgradeType.hasUpgrade(Upgrades.HOPPER, this.upgradeSlots);
    }

    @Override
    public final List<ItemStack> getContents(Level level, BlockPos pos, BlockState state, boolean dropBlock) {
        List<ItemStack> contents = new ArrayList();
        if (dropBlock) {
            if (state.m_60734_() instanceof TraderInterfaceBlock) {
                contents.add(((TraderInterfaceBlock) state.m_60734_()).getDropBlockItem(state, this));
            } else {
                contents.add(new ItemStack(state.m_60734_()));
            }
        }
        for (int i = 0; i < this.upgradeSlots.getContainerSize(); i++) {
            if (!this.upgradeSlots.getItem(i).isEmpty()) {
                contents.add(this.upgradeSlots.getItem(i));
            }
        }
        this.getAdditionalContents(contents);
        return contents;
    }

    protected abstract void getAdditionalContents(List<ItemStack> var1);

    @Override
    public OwnerData getOwner() {
        return this.owner;
    }

    public static enum ActiveMode {

        DISABLED(0, be -> false), REDSTONE_OFF(1, be -> be.f_58857_ != null ? !be.f_58857_.m_276867_(be.m_58899_()) : false), REDSTONE_ONLY(2, be -> be.f_58857_ != null ? be.f_58857_.m_276867_(be.m_58899_()) : false), ALWAYS_ON(3, be -> true);

        public final int index;

        private final Function<TraderInterfaceBlockEntity, Boolean> active;

        public final Component getDisplayText() {
            return LCText.GUI_INTERFACE_ACTIVE_MODE.get(this).get();
        }

        public final TraderInterfaceBlockEntity.ActiveMode getNext() {
            return fromIndex(this.index + 1);
        }

        public boolean isActive(TraderInterfaceBlockEntity blockEntity) {
            return (Boolean) this.active.apply(blockEntity);
        }

        private ActiveMode(int index, Function<TraderInterfaceBlockEntity, Boolean> active) {
            this.index = index;
            this.active = active;
        }

        public static TraderInterfaceBlockEntity.ActiveMode fromIndex(int index) {
            for (TraderInterfaceBlockEntity.ActiveMode mode : values()) {
                if (mode.index == index) {
                    return mode;
                }
            }
            return DISABLED;
        }
    }

    public static enum InteractionType {

        RESTOCK_AND_DRAIN(true, true, true, false, 3), RESTOCK(true, true, false, false, 1), DRAIN(true, false, true, false, 2), TRADE(false, false, false, true, 0);

        public final boolean requiresPermissions;

        public final boolean restocks;

        public final boolean drains;

        public final boolean trades;

        public final int index;

        public final Component getDisplayText() {
            return LCText.GUI_INTERFACE_INTERACTION_TYPE.get(this).get();
        }

        private InteractionType(boolean requiresPermissions, boolean restocks, boolean drains, boolean trades, int index) {
            this.requiresPermissions = requiresPermissions;
            this.restocks = restocks;
            this.drains = drains;
            this.trades = trades;
            this.index = index;
        }

        public static TraderInterfaceBlockEntity.InteractionType fromIndex(int index) {
            for (TraderInterfaceBlockEntity.InteractionType type : values()) {
                if (type.index == index) {
                    return type;
                }
            }
            return TRADE;
        }

        public static int size() {
            return 4;
        }
    }

    public static class InterfaceMenuProvider implements EasyMenuProvider {

        private final TraderInterfaceBlockEntity blockEntity;

        public InterfaceMenuProvider(TraderInterfaceBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        @Override
        public AbstractContainerMenu createMenu(int windowID, @NotNull Inventory inventory, @NotNull Player player) {
            return new TraderInterfaceMenu(windowID, inventory, this.blockEntity);
        }
    }
}