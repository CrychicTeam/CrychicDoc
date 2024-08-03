package io.github.lightman314.lightmanscurrency.common.traders.paygate;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.stats.StatKeys;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TradeResult;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderType;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.permissions.PermissionOption;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderStorageScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.PaygateBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.menus.TraderMenu;
import io.github.lightman314.lightmanscurrency.common.menus.TraderStorageMenu;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.paygate.PaygateTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.notifications.types.trader.PaygateNotification;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.tradedata.PaygateTradeData;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import io.github.lightman314.lightmanscurrency.network.message.paygate.CPacketCollectTicketStubs;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PaygateTraderData extends TraderData {

    public static final TraderType<PaygateTraderData> TYPE = new TraderType<>(new ResourceLocation("lightmanscurrency", "paygate"), PaygateTraderData::new);

    public static final int DURATION_MIN = 1;

    private final List<ItemStack> storedTicketStubs = new ArrayList();

    protected List<PaygateTradeData> trades = PaygateTradeData.listOfSize(1);

    public static int getMaxDuration() {
        int val = LCConfig.SERVER.paygateMaxDuration.get();
        return val <= 0 ? Integer.MAX_VALUE : val;
    }

    public int getStoredTicketStubs() {
        int count = 0;
        for (ItemStack stack : this.storedTicketStubs) {
            count += stack.getCount();
        }
        return count;
    }

    public void addTicketStub(ItemStack stub) {
        if (!this.isCreative()) {
            for (ItemStack s : this.storedTicketStubs) {
                if (stub.getItem() == s.getItem()) {
                    s.grow(stub.getCount());
                    stub.setCount(0);
                    break;
                }
            }
            if (!stub.isEmpty()) {
                this.storedTicketStubs.add(stub.copyAndClear());
            }
            this.markTicketStubsDirty();
        }
    }

    public void collectTicketStubs(Player player) {
        for (ItemStack stub : this.storedTicketStubs) {
            ItemHandlerHelper.giveItemToPlayer(player, stub);
        }
        this.storedTicketStubs.clear();
        this.markTicketStubsDirty();
    }

    @Override
    public boolean canShowOnTerminal() {
        return false;
    }

    private PaygateTraderData() {
        super(TYPE);
    }

    public PaygateTraderData(@Nonnull Level level, @Nonnull BlockPos pos) {
        super(TYPE, level, pos);
    }

    @Override
    public int getTradeCount() {
        return this.trades.size();
    }

    @Override
    public IconData getIcon() {
        return IconData.of(Items.REDSTONE_BLOCK);
    }

    @Override
    protected boolean allowAdditionalUpgradeType(UpgradeType type) {
        return false;
    }

    @Override
    public boolean canEditTradeCount() {
        return true;
    }

    @Override
    public int getMaxTradeCount() {
        return 8;
    }

    @Override
    public void addTrade(Player requestor) {
        if (!this.isClient()) {
            if (this.getTradeCount() < 100) {
                if (this.getTradeCount() >= this.getMaxTradeCount() && !LCAdminMode.isAdminPlayer(requestor)) {
                    Permissions.PermissionWarning(requestor, "add creative trade slot", "LC_ADMIN_MODE");
                } else if (!this.hasPermission(requestor, "editTrades")) {
                    Permissions.PermissionWarning(requestor, "add trade slot", "editTrades");
                } else {
                    this.overrideTradeCount(this.getTradeCount() + 1);
                }
            }
        }
    }

    @Override
    public void removeTrade(Player requestor) {
        if (!this.isClient()) {
            if (this.getTradeCount() > 1) {
                if (!this.hasPermission(requestor, "editTrades")) {
                    Permissions.PermissionWarning(requestor, "remove trade slot", "editTrades");
                } else {
                    this.overrideTradeCount(this.getTradeCount() - 1);
                }
            }
        }
    }

    public void overrideTradeCount(int newTradeCount) {
        if (this.getTradeCount() != newTradeCount) {
            int tradeCount = MathUtil.clamp(newTradeCount, 1, 100);
            List<PaygateTradeData> oldTrades = this.trades;
            this.trades = PaygateTradeData.listOfSize(tradeCount);
            for (int i = 0; i < oldTrades.size() && i < this.trades.size(); i++) {
                this.trades.set(i, (PaygateTradeData) oldTrades.get(i));
            }
            this.markTradesDirty();
        }
    }

    public PaygateTradeData getTrade(int tradeSlot) {
        if (tradeSlot >= 0 && tradeSlot < this.trades.size()) {
            return (PaygateTradeData) this.trades.get(tradeSlot);
        } else {
            LightmansCurrency.LogError("Cannot get trade in index " + tradeSlot + " from a trader with only " + this.trades.size() + " trades.");
            return new PaygateTradeData();
        }
    }

    @Nonnull
    @Override
    public List<PaygateTradeData> getTradeData() {
        return this.trades;
    }

    @Override
    public int getTradeStock(int tradeIndex) {
        return 1;
    }

    private PaygateBlockEntity getBlockEntity() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerLevel l = server.getLevel(this.getLevel());
            if (l != null && l.m_46749_(this.getPos())) {
                BlockEntity be = l.m_7702_(this.getPos());
                if (be instanceof PaygateBlockEntity) {
                    return (PaygateBlockEntity) be;
                }
            }
        }
        return null;
    }

    public boolean isActive() {
        PaygateBlockEntity be = this.getBlockEntity();
        return be != null ? be.isActive() : false;
    }

    private void activate(int duration) {
        PaygateBlockEntity be = this.getBlockEntity();
        if (be != null) {
            be.activate(duration);
        }
    }

    @Override
    public TradeResult ExecuteTrade(TradeContext context, int tradeIndex) {
        PaygateTradeData trade = this.getTrade(tradeIndex);
        if (trade == null) {
            LightmansCurrency.LogError("Trade at index " + tradeIndex + " is null. Cannot execute trade!");
            return TradeResult.FAIL_INVALID_TRADE;
        } else if (!trade.isValid()) {
            LightmansCurrency.LogWarning("Trade at index " + tradeIndex + " is not a valid trade. Cannot execute trade.");
            return TradeResult.FAIL_INVALID_TRADE;
        } else if (this.isActive()) {
            LightmansCurrency.LogWarning("Paygate is already activated. It cannot be activated until the previous timer is completed.");
            return TradeResult.FAIL_OUT_OF_STOCK;
        } else if (!context.hasPlayerReference()) {
            return TradeResult.FAIL_NULL;
        } else if (this.runPreTradeEvent(trade, context).isCanceled()) {
            return TradeResult.FAIL_TRADE_RULE_DENIAL;
        } else {
            MoneyValue price = MoneyValue.empty();
            MoneyValue taxesPaid = MoneyValue.empty();
            if (trade.isTicketTrade()) {
                if (!trade.canAfford(context)) {
                    LightmansCurrency.LogDebug("Ticket ID " + trade.getTicketID() + " could not be found in the players inventory to pay for trade " + tradeIndex + ". Cannot execute trade.");
                    return TradeResult.FAIL_CANNOT_AFFORD;
                }
                boolean hasPass = context.hasPass(trade.getTicketID());
                if (!hasPass) {
                    ItemStack ticketStub = trade.getTicketStub();
                    if (!trade.shouldStoreTicketStubs() && !context.canFitItem(ticketStub)) {
                        LightmansCurrency.LogInfo("Not enough room for the ticket stub. Aborting trade!");
                        return TradeResult.FAIL_NO_OUTPUT_SPACE;
                    }
                    if (!context.collectTicket(trade.getTicketID())) {
                        LightmansCurrency.LogError("Unable to collect the ticket. Aborting Trade!");
                        return TradeResult.FAIL_CANNOT_AFFORD;
                    }
                    if (trade.shouldStoreTicketStubs()) {
                        this.addTicketStub(ticketStub);
                    } else {
                        context.putItem(ticketStub);
                    }
                }
                this.activate(trade.getDuration());
                this.pushNotification(PaygateNotification.createTicket(trade, hasPass, context.getPlayerReference(), this.getNotificationCategory()));
            } else {
                price = trade.getCost(context);
                if (!context.getPayment(price)) {
                    LightmansCurrency.LogDebug("Not enough money is present for the trade at index " + tradeIndex + ". Cannot execute trade.");
                    return TradeResult.FAIL_CANNOT_AFFORD;
                }
                this.activate(trade.getDuration());
                if (!this.isCreative()) {
                    taxesPaid = this.addStoredMoney(price, true);
                }
                this.incrementStat(StatKeys.Traders.MONEY_EARNED, price);
                if (!taxesPaid.isEmpty()) {
                    this.incrementStat(StatKeys.Taxables.TAXES_PAID, taxesPaid);
                }
                this.pushNotification(PaygateNotification.createMoney(trade, price, context.getPlayerReference(), this.getNotificationCategory(), taxesPaid));
            }
            this.runPostTradeEvent(trade, context, price, taxesPaid);
            return TradeResult.SUCCESS;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        this.saveTrades(compound);
        this.saveTicketStubs(compound);
    }

    protected final void saveTicketStubs(CompoundTag compound) {
        ListTag list = new ListTag();
        for (ItemStack stub : this.storedTicketStubs) {
            CompoundTag tag = stub.save(new CompoundTag());
            list.add(tag);
        }
        compound.put("Stubs", list);
    }

    @Override
    protected final void saveTrades(CompoundTag compound) {
        PaygateTradeData.saveAllData(compound, this.trades);
    }

    public void markTicketStubsDirty() {
        this.markDirty(new Consumer[] { this::saveTicketStubs });
    }

    @Override
    protected void saveAdditionalToJson(JsonObject json) {
    }

    @Override
    protected void loadAdditional(CompoundTag compound) {
        if (compound.contains("Trades")) {
            this.trades = PaygateTradeData.loadAllData(compound);
        }
        if (compound.contains("TicketStubs")) {
            int count = compound.getInt("TicketStubs");
            this.storedTicketStubs.clear();
            if (count > 0) {
                this.storedTicketStubs.add(new ItemStack(ModItems.TICKET_STUB.get(), count));
            }
        } else if (compound.contains("Stubs")) {
            ListTag list = compound.getList("Stubs", 10);
            this.storedTicketStubs.clear();
            for (int i = 0; i < list.size(); i++) {
                ItemStack stack = ItemStack.of(list.getCompound(i));
                if (!stack.isEmpty()) {
                    this.storedTicketStubs.add(stack);
                }
            }
        }
    }

    @Override
    protected void loadAdditionalFromJson(JsonObject json) {
    }

    @Override
    protected void saveAdditionalPersistentData(CompoundTag compound) {
    }

    @Override
    protected void loadAdditionalPersistentData(CompoundTag compound) {
    }

    @Override
    protected void getAdditionalContents(List<ItemStack> results) {
    }

    @Override
    public boolean canMakePersistent() {
        return false;
    }

    @Override
    public void initStorageTabs(@Nonnull ITraderStorageMenu menu) {
        menu.setTab(2, new PaygateTradeEditTab(menu));
    }

    @Override
    protected void addPermissionOptions(List<PermissionOption> options) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onScreenInit(TraderScreen screen, Consumer<Object> addWidget) {
        super.onScreenInit(screen, addWidget);
        IconButton button = this.createTicketStubCollectionButton(() -> ((TraderMenu) screen.m_6262_()).player);
        addWidget.accept(button);
        screen.leftEdgePositioner.addWidget(button);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onStorageScreenInit(TraderStorageScreen screen, Consumer<Object> addWidget) {
        super.onStorageScreenInit(screen, addWidget);
        IconButton button = this.createTicketStubCollectionButton(() -> ((TraderStorageMenu) screen.m_6262_()).player);
        addWidget.accept(button);
        screen.leftEdgePositioner.addWidget(button);
    }

    @OnlyIn(Dist.CLIENT)
    private IconButton createTicketStubCollectionButton(Supplier<Player> playerSource) {
        return new IconButton(0, 0, b -> new CPacketCollectTicketStubs(this.getID()).send(), IconData.of(ModItems.TICKET_STUB)).withAddons(EasyAddonHelper.toggleTooltip(() -> this.getStoredTicketStubs() > 0, () -> LCText.TOOLTIP_TRADER_PAYGATE_COLLECT_TICKET_STUBS.get(this.getStoredTicketStubs()), EasyText::empty), EasyAddonHelper.visibleCheck((NonNullSupplier<Boolean>) (() -> this.areTicketStubsRelevant() && this.hasPermission((Player) playerSource.get(), "openStorage"))), EasyAddonHelper.activeCheck((NonNullSupplier<Boolean>) (() -> this.getStoredTicketStubs() > 0)));
    }

    private boolean areTicketStubsRelevant() {
        return this.getStoredTicketStubs() > 0 || this.trades.stream().anyMatch(t -> t.isTicketTrade() && t.shouldStoreTicketStubs());
    }
}