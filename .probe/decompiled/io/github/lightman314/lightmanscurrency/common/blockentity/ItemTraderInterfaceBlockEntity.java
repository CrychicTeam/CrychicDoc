package io.github.lightman314.lightmanscurrency.common.blockentity;

import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.common.blockentity.handler.ItemInterfaceHandler;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.items.UpgradeItem;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import io.github.lightman314.lightmanscurrency.common.menus.traderinterface.item.ItemStorageTab;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import io.github.lightman314.lightmanscurrency.common.upgrades.Upgrades;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.capacity.CapacityUpgrade;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class ItemTraderInterfaceBlockEntity extends TraderInterfaceBlockEntity implements TraderItemStorage.ITraderItemFilter {

    private final TraderItemStorage itemBuffer = new TraderItemStorage(this);

    ItemInterfaceHandler itemHandler = this.addHandler(new ItemInterfaceHandler(this, this::getItemBuffer));

    public TraderItemStorage getItemBuffer() {
        return this.itemBuffer;
    }

    public ItemInterfaceHandler getItemHandler() {
        return this.itemHandler;
    }

    public ItemTraderInterfaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRADER_INTERFACE_ITEM.get(), pos, state);
    }

    @Override
    public TradeContext.Builder buildTradeContext(TradeContext.Builder baseContext) {
        return baseContext.withItemHandler(this.itemBuffer);
    }

    public boolean allowInput(ItemStack item) {
        if (this.getInteractionType().trades) {
            if (this.getReferencedTrade() instanceof ItemTradeData trade) {
                if (trade.isBarter()) {
                    for (int i = 0; i < 2; i++) {
                        if (InventoryUtil.ItemMatches(item, trade.getBarterItem(i))) {
                            return true;
                        }
                    }
                } else if (trade.isPurchase()) {
                    for (int ix = 0; ix < 2; ix++) {
                        if (InventoryUtil.ItemMatches(item, trade.getSellItem(ix))) {
                            return true;
                        }
                    }
                }
            }
        } else {
            TraderData trader = this.getTrader();
            if (trader instanceof ItemTraderData) {
                for (ItemTradeData tradex : ((ItemTraderData) trader).getTradeData()) {
                    if (tradex.isSale() || tradex.isBarter()) {
                        for (int ixx = 0; ixx < 2; ixx++) {
                            if (InventoryUtil.ItemMatches(item, tradex.getSellItem(ixx))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean allowOutput(ItemStack item) {
        return !this.allowInput(item);
    }

    @Override
    public boolean isItemRelevant(ItemStack item) {
        if (this.getInteractionType().trades) {
            if (this.getReferencedTrade() instanceof ItemTradeData trade) {
                return trade.allowItemInStorage(item);
            }
        } else if (this.getTrader() instanceof ItemTraderData it) {
            for (ItemTradeData trade : it.getTradeData()) {
                if (trade.allowItemInStorage(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getStorageStackLimit() {
        int limit = 576;
        for (int i = 0; i < this.getUpgradeInventory().getContainerSize(); i++) {
            ItemStack stack = this.getUpgradeInventory().getItem(i);
            Item var5 = stack.getItem();
            if (var5 instanceof UpgradeItem) {
                UpgradeItem upgradeItem = (UpgradeItem) var5;
                if (this.allowUpgrade(upgradeItem) && upgradeItem.getUpgradeType() instanceof CapacityUpgrade) {
                    limit += UpgradeItem.getUpgradeData(stack).getIntValue(CapacityUpgrade.CAPACITY);
                }
            }
        }
        return limit;
    }

    protected ItemTradeData deserializeTrade(CompoundTag compound) {
        return ItemTradeData.loadData(compound, false);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        this.saveItemBuffer(compound);
    }

    protected final CompoundTag saveItemBuffer(CompoundTag compound) {
        this.itemBuffer.save(compound, "Storage");
        return compound;
    }

    public void setItemBufferDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveItemBuffer(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Storage")) {
            this.itemBuffer.load(compound, "Storage");
        }
    }

    @Override
    public boolean validTraderType(TraderData trader) {
        return trader instanceof ItemTraderData;
    }

    protected final ItemTraderData getItemTrader() {
        TraderData trader = this.getTrader();
        return trader instanceof ItemTraderData ? (ItemTraderData) trader : null;
    }

    @Override
    protected void drainTick() {
        ItemTraderData trader = this.getItemTrader();
        if (trader != null && trader.hasPermission(this.owner.getPlayerForContext(), "interactionLink")) {
            for (int i = 0; i < trader.getTradeCount(); i++) {
                ItemTradeData trade = trader.getTrade(i);
                if (trade.isValid()) {
                    List<ItemStack> drainItems = new ArrayList();
                    if (trade.isPurchase()) {
                        drainItems.add(trade.getSellItem(0));
                        drainItems.add(trade.getSellItem(1));
                    }
                    if (trade.isBarter()) {
                        drainItems.add(trade.getBarterItem(0));
                        drainItems.add(trade.getBarterItem(1));
                    }
                    for (ItemStack drainItem : drainItems) {
                        if (!drainItem.isEmpty()) {
                            int drainableAmount = trader.getStorage().getItemCount(drainItem);
                            if (drainableAmount > 0) {
                                ItemStack movingStack = drainItem.copy();
                                movingStack.setCount(Math.min(movingStack.getMaxStackSize(), drainableAmount));
                                ItemStack removed = trader.getStorage().removeItem(movingStack);
                                ItemStack leftovers = ItemHandlerHelper.insertItemStacked(this.itemBuffer, removed, false);
                                if (!leftovers.isEmpty()) {
                                    trader.getStorage().forceAddItem(leftovers);
                                }
                                this.setItemBufferDirty();
                                trader.markStorageDirty();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void restockTick() {
        ItemTraderData trader = this.getItemTrader();
        if (trader != null && trader.hasPermission(this.owner.getPlayerForContext(), "interactionLink")) {
            for (int i = 0; i < trader.getTradeCount(); i++) {
                ItemTradeData trade = trader.getTrade(i);
                if (trade.isValid() && (trade.isBarter() || trade.isSale())) {
                    for (int s = 0; s < 2; s++) {
                        ItemStack stockItem = trade.getSellItem(s);
                        if (!stockItem.isEmpty()) {
                            int stockableAmount = this.itemBuffer.getItemCount(stockItem);
                            if (stockableAmount > 0) {
                                ItemStack movingStack = stockItem.copy();
                                movingStack.setCount(Math.min(movingStack.getMaxStackSize(), stockableAmount));
                                ItemStack removedItem = this.itemBuffer.removeItem(movingStack);
                                if (removedItem.getCount() == movingStack.getCount()) {
                                    trader.getStorage().tryAddItem(movingStack);
                                    if (!movingStack.isEmpty()) {
                                        this.itemBuffer.forceAddItem(movingStack);
                                    }
                                } else {
                                    this.itemBuffer.forceAddItem(removedItem);
                                }
                                this.setItemBufferDirty();
                                trader.markStorageDirty();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void tradeTick() {
        if (this.getTrueTrade() instanceof ItemTradeData trade && trade != null && trade.isValid()) {
            if (trade.isSale()) {
                if (this.itemBuffer.canFitItems(trade.getSellItem(0), trade.getSellItem(1))) {
                    this.interactWithTrader();
                    this.setItemBufferDirty();
                }
            } else if (trade.isPurchase()) {
                if (this.itemBuffer.hasItems(trade.getSellItem(0), trade.getSellItem(1))) {
                    this.interactWithTrader();
                    this.setItemBufferDirty();
                }
            } else if (trade.isBarter() && this.itemBuffer.hasItems(trade.getBarterItem(0), trade.getBarterItem(1)) && this.itemBuffer.canFitItems(trade.getSellItem(0), trade.getSellItem(1))) {
                this.interactWithTrader();
                this.setItemBufferDirty();
            }
        }
    }

    @Override
    protected void hopperTick() {
        AtomicBoolean markBufferDirty = new AtomicBoolean(false);
        for (Direction relativeSide : Direction.values()) {
            if (this.itemHandler.getInputSides().get(relativeSide) || this.itemHandler.getOutputSides().get(relativeSide)) {
                Direction actualSide = relativeSide;
                if (this.m_58900_().m_60734_() instanceof IRotatableBlock b) {
                    actualSide = IRotatableBlock.getActualSide(b.getFacing(this.m_58900_()), relativeSide);
                }
                BlockPos queryPos = this.f_58858_.relative(actualSide);
                BlockEntity be = this.f_58857_.getBlockEntity(queryPos);
                if (be != null) {
                    be.getCapability(ForgeCapabilities.ITEM_HANDLER, actualSide.getOpposite()).ifPresent(itemHandler -> {
                        if (this.itemHandler.getInputSides().get(relativeSide)) {
                            boolean query = true;
                            for (int i = 0; query && i < itemHandler.getSlots(); i++) {
                                ItemStack stack = itemHandler.getStackInSlot(i);
                                int fittableAmount = this.itemBuffer.getFittableAmount(stack);
                                if (fittableAmount > 0) {
                                    query = false;
                                    ItemStack result = itemHandler.extractItem(i, fittableAmount, false);
                                    this.itemBuffer.forceAddItem(result);
                                    markBufferDirty.set(true);
                                }
                            }
                        }
                        if (this.itemHandler.getOutputSides().get(relativeSide)) {
                            List<ItemStack> buffer = this.itemBuffer.getContents();
                            boolean query = true;
                            for (int ix = 0; query && ix < buffer.size(); ix++) {
                                ItemStack stack = ((ItemStack) buffer.get(ix)).copy();
                                if (this.allowOutput(stack)) {
                                    for (int slot = 0; query && slot < itemHandler.getSlots(); slot++) {
                                        ItemStack result = itemHandler.insertItem(slot, stack.copy(), false);
                                        int placed = stack.getCount() - result.getCount();
                                        if (placed > 0) {
                                            query = false;
                                            stack.setCount(placed);
                                            this.itemBuffer.removeItem(stack);
                                            markBufferDirty.set(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
        if (markBufferDirty.get()) {
            this.setItemBufferDirty();
        }
    }

    @Override
    public void initMenuTabs(TraderInterfaceMenu menu) {
        menu.setTab(1, new ItemStorageTab(menu));
    }

    @Override
    public boolean allowAdditionalUpgrade(UpgradeType type) {
        return type == Upgrades.ITEM_CAPACITY;
    }

    @Override
    public void getAdditionalContents(List<ItemStack> contents) {
        contents.addAll(this.itemBuffer.getSplitContents());
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable("block.lightmanscurrency.item_trader_interface");
    }
}