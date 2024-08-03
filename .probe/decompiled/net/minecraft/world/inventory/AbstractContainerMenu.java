package net.minecraft.world.inventory;

import com.google.common.base.Suppliers;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;

public abstract class AbstractContainerMenu {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int SLOT_CLICKED_OUTSIDE = -999;

    public static final int QUICKCRAFT_TYPE_CHARITABLE = 0;

    public static final int QUICKCRAFT_TYPE_GREEDY = 1;

    public static final int QUICKCRAFT_TYPE_CLONE = 2;

    public static final int QUICKCRAFT_HEADER_START = 0;

    public static final int QUICKCRAFT_HEADER_CONTINUE = 1;

    public static final int QUICKCRAFT_HEADER_END = 2;

    public static final int CARRIED_SLOT_SIZE = Integer.MAX_VALUE;

    private final NonNullList<ItemStack> lastSlots = NonNullList.create();

    public final NonNullList<Slot> slots = NonNullList.create();

    private final List<DataSlot> dataSlots = Lists.newArrayList();

    private ItemStack carried = ItemStack.EMPTY;

    private final NonNullList<ItemStack> remoteSlots = NonNullList.create();

    private final IntList remoteDataSlots = new IntArrayList();

    private ItemStack remoteCarried = ItemStack.EMPTY;

    private int stateId;

    @Nullable
    private final MenuType<?> menuType;

    public final int containerId;

    private int quickcraftType = -1;

    private int quickcraftStatus;

    private final Set<Slot> quickcraftSlots = Sets.newHashSet();

    private final List<ContainerListener> containerListeners = Lists.newArrayList();

    @Nullable
    private ContainerSynchronizer synchronizer;

    private boolean suppressRemoteUpdates;

    protected AbstractContainerMenu(@Nullable MenuType<?> menuType0, int int1) {
        this.menuType = menuType0;
        this.containerId = int1;
    }

    protected static boolean stillValid(ContainerLevelAccess containerLevelAccess0, Player player1, Block block2) {
        return containerLevelAccess0.evaluate((p_38916_, p_38917_) -> !p_38916_.getBlockState(p_38917_).m_60713_(block2) ? false : player1.m_20275_((double) p_38917_.m_123341_() + 0.5, (double) p_38917_.m_123342_() + 0.5, (double) p_38917_.m_123343_() + 0.5) <= 64.0, true);
    }

    public MenuType<?> getType() {
        if (this.menuType == null) {
            throw new UnsupportedOperationException("Unable to construct this menu by type");
        } else {
            return this.menuType;
        }
    }

    protected static void checkContainerSize(Container container0, int int1) {
        int $$2 = container0.getContainerSize();
        if ($$2 < int1) {
            throw new IllegalArgumentException("Container size " + $$2 + " is smaller than expected " + int1);
        }
    }

    protected static void checkContainerDataCount(ContainerData containerData0, int int1) {
        int $$2 = containerData0.getCount();
        if ($$2 < int1) {
            throw new IllegalArgumentException("Container data count " + $$2 + " is smaller than expected " + int1);
        }
    }

    public boolean isValidSlotIndex(int int0) {
        return int0 == -1 || int0 == -999 || int0 < this.slots.size();
    }

    protected Slot addSlot(Slot slot0) {
        slot0.index = this.slots.size();
        this.slots.add(slot0);
        this.lastSlots.add(ItemStack.EMPTY);
        this.remoteSlots.add(ItemStack.EMPTY);
        return slot0;
    }

    protected DataSlot addDataSlot(DataSlot dataSlot0) {
        this.dataSlots.add(dataSlot0);
        this.remoteDataSlots.add(0);
        return dataSlot0;
    }

    protected void addDataSlots(ContainerData containerData0) {
        for (int $$1 = 0; $$1 < containerData0.getCount(); $$1++) {
            this.addDataSlot(DataSlot.forContainer(containerData0, $$1));
        }
    }

    public void addSlotListener(ContainerListener containerListener0) {
        if (!this.containerListeners.contains(containerListener0)) {
            this.containerListeners.add(containerListener0);
            this.broadcastChanges();
        }
    }

    public void setSynchronizer(ContainerSynchronizer containerSynchronizer0) {
        this.synchronizer = containerSynchronizer0;
        this.sendAllDataToRemote();
    }

    public void sendAllDataToRemote() {
        int $$0 = 0;
        for (int $$1 = this.slots.size(); $$0 < $$1; $$0++) {
            this.remoteSlots.set($$0, this.slots.get($$0).getItem().copy());
        }
        this.remoteCarried = this.getCarried().copy();
        $$0 = 0;
        for (int $$3 = this.dataSlots.size(); $$0 < $$3; $$0++) {
            this.remoteDataSlots.set($$0, ((DataSlot) this.dataSlots.get($$0)).get());
        }
        if (this.synchronizer != null) {
            this.synchronizer.sendInitialData(this, this.remoteSlots, this.remoteCarried, this.remoteDataSlots.toIntArray());
        }
    }

    public void removeSlotListener(ContainerListener containerListener0) {
        this.containerListeners.remove(containerListener0);
    }

    public NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> $$0 = NonNullList.create();
        for (Slot $$1 : this.slots) {
            $$0.add($$1.getItem());
        }
        return $$0;
    }

    public void broadcastChanges() {
        for (int $$0 = 0; $$0 < this.slots.size(); $$0++) {
            ItemStack $$1 = this.slots.get($$0).getItem();
            Supplier<ItemStack> $$2 = Suppliers.memoize($$1::m_41777_);
            this.triggerSlotListeners($$0, $$1, $$2);
            this.synchronizeSlotToRemote($$0, $$1, $$2);
        }
        this.synchronizeCarriedToRemote();
        for (int $$3 = 0; $$3 < this.dataSlots.size(); $$3++) {
            DataSlot $$4 = (DataSlot) this.dataSlots.get($$3);
            int $$5 = $$4.get();
            if ($$4.checkAndClearUpdateFlag()) {
                this.updateDataSlotListeners($$3, $$5);
            }
            this.synchronizeDataSlotToRemote($$3, $$5);
        }
    }

    public void broadcastFullState() {
        for (int $$0 = 0; $$0 < this.slots.size(); $$0++) {
            ItemStack $$1 = this.slots.get($$0).getItem();
            this.triggerSlotListeners($$0, $$1, $$1::m_41777_);
        }
        for (int $$2 = 0; $$2 < this.dataSlots.size(); $$2++) {
            DataSlot $$3 = (DataSlot) this.dataSlots.get($$2);
            if ($$3.checkAndClearUpdateFlag()) {
                this.updateDataSlotListeners($$2, $$3.get());
            }
        }
        this.sendAllDataToRemote();
    }

    private void updateDataSlotListeners(int int0, int int1) {
        for (ContainerListener $$2 : this.containerListeners) {
            $$2.dataChanged(this, int0, int1);
        }
    }

    private void triggerSlotListeners(int int0, ItemStack itemStack1, Supplier<ItemStack> supplierItemStack2) {
        ItemStack $$3 = this.lastSlots.get(int0);
        if (!ItemStack.matches($$3, itemStack1)) {
            ItemStack $$4 = (ItemStack) supplierItemStack2.get();
            this.lastSlots.set(int0, $$4);
            for (ContainerListener $$5 : this.containerListeners) {
                $$5.slotChanged(this, int0, $$4);
            }
        }
    }

    private void synchronizeSlotToRemote(int int0, ItemStack itemStack1, Supplier<ItemStack> supplierItemStack2) {
        if (!this.suppressRemoteUpdates) {
            ItemStack $$3 = this.remoteSlots.get(int0);
            if (!ItemStack.matches($$3, itemStack1)) {
                ItemStack $$4 = (ItemStack) supplierItemStack2.get();
                this.remoteSlots.set(int0, $$4);
                if (this.synchronizer != null) {
                    this.synchronizer.sendSlotChange(this, int0, $$4);
                }
            }
        }
    }

    private void synchronizeDataSlotToRemote(int int0, int int1) {
        if (!this.suppressRemoteUpdates) {
            int $$2 = this.remoteDataSlots.getInt(int0);
            if ($$2 != int1) {
                this.remoteDataSlots.set(int0, int1);
                if (this.synchronizer != null) {
                    this.synchronizer.sendDataChange(this, int0, int1);
                }
            }
        }
    }

    private void synchronizeCarriedToRemote() {
        if (!this.suppressRemoteUpdates) {
            if (!ItemStack.matches(this.getCarried(), this.remoteCarried)) {
                this.remoteCarried = this.getCarried().copy();
                if (this.synchronizer != null) {
                    this.synchronizer.sendCarriedChange(this, this.remoteCarried);
                }
            }
        }
    }

    public void setRemoteSlot(int int0, ItemStack itemStack1) {
        this.remoteSlots.set(int0, itemStack1.copy());
    }

    public void setRemoteSlotNoCopy(int int0, ItemStack itemStack1) {
        if (int0 >= 0 && int0 < this.remoteSlots.size()) {
            this.remoteSlots.set(int0, itemStack1);
        } else {
            LOGGER.debug("Incorrect slot index: {} available slots: {}", int0, this.remoteSlots.size());
        }
    }

    public void setRemoteCarried(ItemStack itemStack0) {
        this.remoteCarried = itemStack0.copy();
    }

    public boolean clickMenuButton(Player player0, int int1) {
        return false;
    }

    public Slot getSlot(int int0) {
        return this.slots.get(int0);
    }

    public abstract ItemStack quickMoveStack(Player var1, int var2);

    public void clicked(int int0, int int1, ClickType clickType2, Player player3) {
        try {
            this.doClick(int0, int1, clickType2, player3);
        } catch (Exception var8) {
            CrashReport $$5 = CrashReport.forThrowable(var8, "Container click");
            CrashReportCategory $$6 = $$5.addCategory("Click info");
            $$6.setDetail("Menu Type", (CrashReportDetail<String>) (() -> this.menuType != null ? BuiltInRegistries.MENU.getKey(this.menuType).toString() : "<no type>"));
            $$6.setDetail("Menu Class", (CrashReportDetail<String>) (() -> this.getClass().getCanonicalName()));
            $$6.setDetail("Slot Count", this.slots.size());
            $$6.setDetail("Slot", int0);
            $$6.setDetail("Button", int1);
            $$6.setDetail("Type", clickType2);
            throw new ReportedException($$5);
        }
    }

    private void doClick(int int0, int int1, ClickType clickType2, Player player3) {
        Inventory $$4 = player3.getInventory();
        if (clickType2 == ClickType.QUICK_CRAFT) {
            int $$5 = this.quickcraftStatus;
            this.quickcraftStatus = getQuickcraftHeader(int1);
            if (($$5 != 1 || this.quickcraftStatus != 2) && $$5 != this.quickcraftStatus) {
                this.resetQuickCraft();
            } else if (this.getCarried().isEmpty()) {
                this.resetQuickCraft();
            } else if (this.quickcraftStatus == 0) {
                this.quickcraftType = getQuickcraftType(int1);
                if (isValidQuickcraftType(this.quickcraftType, player3)) {
                    this.quickcraftStatus = 1;
                    this.quickcraftSlots.clear();
                } else {
                    this.resetQuickCraft();
                }
            } else if (this.quickcraftStatus == 1) {
                Slot $$6 = this.slots.get(int0);
                ItemStack $$7 = this.getCarried();
                if (canItemQuickReplace($$6, $$7, true) && $$6.mayPlace($$7) && (this.quickcraftType == 2 || $$7.getCount() > this.quickcraftSlots.size()) && this.canDragTo($$6)) {
                    this.quickcraftSlots.add($$6);
                }
            } else if (this.quickcraftStatus == 2) {
                if (!this.quickcraftSlots.isEmpty()) {
                    if (this.quickcraftSlots.size() == 1) {
                        int $$8 = ((Slot) this.quickcraftSlots.iterator().next()).index;
                        this.resetQuickCraft();
                        this.doClick($$8, this.quickcraftType, ClickType.PICKUP, player3);
                        return;
                    }
                    ItemStack $$9 = this.getCarried().copy();
                    if ($$9.isEmpty()) {
                        this.resetQuickCraft();
                        return;
                    }
                    int $$10 = this.getCarried().getCount();
                    for (Slot $$11 : this.quickcraftSlots) {
                        ItemStack $$12 = this.getCarried();
                        if ($$11 != null && canItemQuickReplace($$11, $$12, true) && $$11.mayPlace($$12) && (this.quickcraftType == 2 || $$12.getCount() >= this.quickcraftSlots.size()) && this.canDragTo($$11)) {
                            int $$13 = $$11.hasItem() ? $$11.getItem().getCount() : 0;
                            int $$14 = Math.min($$9.getMaxStackSize(), $$11.getMaxStackSize($$9));
                            int $$15 = Math.min(getQuickCraftPlaceCount(this.quickcraftSlots, this.quickcraftType, $$9) + $$13, $$14);
                            $$10 -= $$15 - $$13;
                            $$11.setByPlayer($$9.copyWithCount($$15));
                        }
                    }
                    $$9.setCount($$10);
                    this.setCarried($$9);
                }
                this.resetQuickCraft();
            } else {
                this.resetQuickCraft();
            }
        } else if (this.quickcraftStatus != 0) {
            this.resetQuickCraft();
        } else if ((clickType2 == ClickType.PICKUP || clickType2 == ClickType.QUICK_MOVE) && (int1 == 0 || int1 == 1)) {
            ClickAction $$16 = int1 == 0 ? ClickAction.PRIMARY : ClickAction.SECONDARY;
            if (int0 == -999) {
                if (!this.getCarried().isEmpty()) {
                    if ($$16 == ClickAction.PRIMARY) {
                        player3.drop(this.getCarried(), true);
                        this.setCarried(ItemStack.EMPTY);
                    } else {
                        player3.drop(this.getCarried().split(1), true);
                    }
                }
            } else if (clickType2 == ClickType.QUICK_MOVE) {
                if (int0 < 0) {
                    return;
                }
                Slot $$17 = this.slots.get(int0);
                if (!$$17.mayPickup(player3)) {
                    return;
                }
                ItemStack $$18 = this.quickMoveStack(player3, int0);
                while (!$$18.isEmpty() && ItemStack.isSameItem($$17.getItem(), $$18)) {
                    $$18 = this.quickMoveStack(player3, int0);
                }
            } else {
                if (int0 < 0) {
                    return;
                }
                Slot $$19 = this.slots.get(int0);
                ItemStack $$20 = $$19.getItem();
                ItemStack $$21 = this.getCarried();
                player3.updateTutorialInventoryAction($$21, $$19.getItem(), $$16);
                if (!this.tryItemClickBehaviourOverride(player3, $$16, $$19, $$20, $$21)) {
                    if ($$20.isEmpty()) {
                        if (!$$21.isEmpty()) {
                            int $$22 = $$16 == ClickAction.PRIMARY ? $$21.getCount() : 1;
                            this.setCarried($$19.safeInsert($$21, $$22));
                        }
                    } else if ($$19.mayPickup(player3)) {
                        if ($$21.isEmpty()) {
                            int $$23 = $$16 == ClickAction.PRIMARY ? $$20.getCount() : ($$20.getCount() + 1) / 2;
                            Optional<ItemStack> $$24 = $$19.tryRemove($$23, Integer.MAX_VALUE, player3);
                            $$24.ifPresent(p_150421_ -> {
                                this.setCarried(p_150421_);
                                $$19.onTake(player3, p_150421_);
                            });
                        } else if ($$19.mayPlace($$21)) {
                            if (ItemStack.isSameItemSameTags($$20, $$21)) {
                                int $$25 = $$16 == ClickAction.PRIMARY ? $$21.getCount() : 1;
                                this.setCarried($$19.safeInsert($$21, $$25));
                            } else if ($$21.getCount() <= $$19.getMaxStackSize($$21)) {
                                this.setCarried($$20);
                                $$19.setByPlayer($$21);
                            }
                        } else if (ItemStack.isSameItemSameTags($$20, $$21)) {
                            Optional<ItemStack> $$26 = $$19.tryRemove($$20.getCount(), $$21.getMaxStackSize() - $$21.getCount(), player3);
                            $$26.ifPresent(p_150428_ -> {
                                $$21.grow(p_150428_.getCount());
                                $$19.onTake(player3, p_150428_);
                            });
                        }
                    }
                }
                $$19.setChanged();
            }
        } else if (clickType2 == ClickType.SWAP) {
            Slot $$27 = this.slots.get(int0);
            ItemStack $$28 = $$4.getItem(int1);
            ItemStack $$29 = $$27.getItem();
            if (!$$28.isEmpty() || !$$29.isEmpty()) {
                if ($$28.isEmpty()) {
                    if ($$27.mayPickup(player3)) {
                        $$4.setItem(int1, $$29);
                        $$27.onSwapCraft($$29.getCount());
                        $$27.setByPlayer(ItemStack.EMPTY);
                        $$27.onTake(player3, $$29);
                    }
                } else if ($$29.isEmpty()) {
                    if ($$27.mayPlace($$28)) {
                        int $$30 = $$27.getMaxStackSize($$28);
                        if ($$28.getCount() > $$30) {
                            $$27.setByPlayer($$28.split($$30));
                        } else {
                            $$4.setItem(int1, ItemStack.EMPTY);
                            $$27.setByPlayer($$28);
                        }
                    }
                } else if ($$27.mayPickup(player3) && $$27.mayPlace($$28)) {
                    int $$31 = $$27.getMaxStackSize($$28);
                    if ($$28.getCount() > $$31) {
                        $$27.setByPlayer($$28.split($$31));
                        $$27.onTake(player3, $$29);
                        if (!$$4.add($$29)) {
                            player3.drop($$29, true);
                        }
                    } else {
                        $$4.setItem(int1, $$29);
                        $$27.setByPlayer($$28);
                        $$27.onTake(player3, $$29);
                    }
                }
            }
        } else if (clickType2 == ClickType.CLONE && player3.getAbilities().instabuild && this.getCarried().isEmpty() && int0 >= 0) {
            Slot $$32 = this.slots.get(int0);
            if ($$32.hasItem()) {
                ItemStack $$33 = $$32.getItem();
                this.setCarried($$33.copyWithCount($$33.getMaxStackSize()));
            }
        } else if (clickType2 == ClickType.THROW && this.getCarried().isEmpty() && int0 >= 0) {
            Slot $$34 = this.slots.get(int0);
            int $$35 = int1 == 0 ? 1 : $$34.getItem().getCount();
            ItemStack $$36 = $$34.safeTake($$35, Integer.MAX_VALUE, player3);
            player3.drop($$36, true);
        } else if (clickType2 == ClickType.PICKUP_ALL && int0 >= 0) {
            Slot $$37 = this.slots.get(int0);
            ItemStack $$38 = this.getCarried();
            if (!$$38.isEmpty() && (!$$37.hasItem() || !$$37.mayPickup(player3))) {
                int $$39 = int1 == 0 ? 0 : this.slots.size() - 1;
                int $$40 = int1 == 0 ? 1 : -1;
                for (int $$41 = 0; $$41 < 2; $$41++) {
                    for (int $$42 = $$39; $$42 >= 0 && $$42 < this.slots.size() && $$38.getCount() < $$38.getMaxStackSize(); $$42 += $$40) {
                        Slot $$43 = this.slots.get($$42);
                        if ($$43.hasItem() && canItemQuickReplace($$43, $$38, true) && $$43.mayPickup(player3) && this.canTakeItemForPickAll($$38, $$43)) {
                            ItemStack $$44 = $$43.getItem();
                            if ($$41 != 0 || $$44.getCount() != $$44.getMaxStackSize()) {
                                ItemStack $$45 = $$43.safeTake($$44.getCount(), $$38.getMaxStackSize() - $$38.getCount(), player3);
                                $$38.grow($$45.getCount());
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean tryItemClickBehaviourOverride(Player player0, ClickAction clickAction1, Slot slot2, ItemStack itemStack3, ItemStack itemStack4) {
        FeatureFlagSet $$5 = player0.m_9236_().m_246046_();
        return itemStack4.isItemEnabled($$5) && itemStack4.overrideStackedOnOther(slot2, clickAction1, player0) ? true : itemStack3.isItemEnabled($$5) && itemStack3.overrideOtherStackedOnMe(itemStack4, slot2, clickAction1, player0, this.createCarriedSlotAccess());
    }

    private SlotAccess createCarriedSlotAccess() {
        return new SlotAccess() {

            @Override
            public ItemStack get() {
                return AbstractContainerMenu.this.getCarried();
            }

            @Override
            public boolean set(ItemStack p_150452_) {
                AbstractContainerMenu.this.setCarried(p_150452_);
                return true;
            }
        };
    }

    public boolean canTakeItemForPickAll(ItemStack itemStack0, Slot slot1) {
        return true;
    }

    public void removed(Player player0) {
        if (player0 instanceof ServerPlayer) {
            ItemStack $$1 = this.getCarried();
            if (!$$1.isEmpty()) {
                if (player0.m_6084_() && !((ServerPlayer) player0).hasDisconnected()) {
                    player0.getInventory().placeItemBackInInventory($$1);
                } else {
                    player0.drop($$1, false);
                }
                this.setCarried(ItemStack.EMPTY);
            }
        }
    }

    protected void clearContainer(Player player0, Container container1) {
        if (!player0.m_6084_() || player0 instanceof ServerPlayer && ((ServerPlayer) player0).hasDisconnected()) {
            for (int $$2 = 0; $$2 < container1.getContainerSize(); $$2++) {
                player0.drop(container1.removeItemNoUpdate($$2), false);
            }
        } else {
            for (int $$3 = 0; $$3 < container1.getContainerSize(); $$3++) {
                Inventory $$4 = player0.getInventory();
                if ($$4.player instanceof ServerPlayer) {
                    $$4.placeItemBackInInventory(container1.removeItemNoUpdate($$3));
                }
            }
        }
    }

    public void slotsChanged(Container container0) {
        this.broadcastChanges();
    }

    public void setItem(int int0, int int1, ItemStack itemStack2) {
        this.getSlot(int0).set(itemStack2);
        this.stateId = int1;
    }

    public void initializeContents(int int0, List<ItemStack> listItemStack1, ItemStack itemStack2) {
        for (int $$3 = 0; $$3 < listItemStack1.size(); $$3++) {
            this.getSlot($$3).set((ItemStack) listItemStack1.get($$3));
        }
        this.carried = itemStack2;
        this.stateId = int0;
    }

    public void setData(int int0, int int1) {
        ((DataSlot) this.dataSlots.get(int0)).set(int1);
    }

    public abstract boolean stillValid(Player var1);

    protected boolean moveItemStackTo(ItemStack itemStack0, int int1, int int2, boolean boolean3) {
        boolean $$4 = false;
        int $$5 = int1;
        if (boolean3) {
            $$5 = int2 - 1;
        }
        if (itemStack0.isStackable()) {
            while (!itemStack0.isEmpty() && (boolean3 ? $$5 >= int1 : $$5 < int2)) {
                Slot $$6 = this.slots.get($$5);
                ItemStack $$7 = $$6.getItem();
                if (!$$7.isEmpty() && ItemStack.isSameItemSameTags(itemStack0, $$7)) {
                    int $$8 = $$7.getCount() + itemStack0.getCount();
                    if ($$8 <= itemStack0.getMaxStackSize()) {
                        itemStack0.setCount(0);
                        $$7.setCount($$8);
                        $$6.setChanged();
                        $$4 = true;
                    } else if ($$7.getCount() < itemStack0.getMaxStackSize()) {
                        itemStack0.shrink(itemStack0.getMaxStackSize() - $$7.getCount());
                        $$7.setCount(itemStack0.getMaxStackSize());
                        $$6.setChanged();
                        $$4 = true;
                    }
                }
                if (boolean3) {
                    $$5--;
                } else {
                    $$5++;
                }
            }
        }
        if (!itemStack0.isEmpty()) {
            if (boolean3) {
                $$5 = int2 - 1;
            } else {
                $$5 = int1;
            }
            while (boolean3 ? $$5 >= int1 : $$5 < int2) {
                Slot $$9 = this.slots.get($$5);
                ItemStack $$10 = $$9.getItem();
                if ($$10.isEmpty() && $$9.mayPlace(itemStack0)) {
                    if (itemStack0.getCount() > $$9.getMaxStackSize()) {
                        $$9.setByPlayer(itemStack0.split($$9.getMaxStackSize()));
                    } else {
                        $$9.setByPlayer(itemStack0.split(itemStack0.getCount()));
                    }
                    $$9.setChanged();
                    $$4 = true;
                    break;
                }
                if (boolean3) {
                    $$5--;
                } else {
                    $$5++;
                }
            }
        }
        return $$4;
    }

    public static int getQuickcraftType(int int0) {
        return int0 >> 2 & 3;
    }

    public static int getQuickcraftHeader(int int0) {
        return int0 & 3;
    }

    public static int getQuickcraftMask(int int0, int int1) {
        return int0 & 3 | (int1 & 3) << 2;
    }

    public static boolean isValidQuickcraftType(int int0, Player player1) {
        if (int0 == 0) {
            return true;
        } else {
            return int0 == 1 ? true : int0 == 2 && player1.getAbilities().instabuild;
        }
    }

    protected void resetQuickCraft() {
        this.quickcraftStatus = 0;
        this.quickcraftSlots.clear();
    }

    public static boolean canItemQuickReplace(@Nullable Slot slot0, ItemStack itemStack1, boolean boolean2) {
        boolean $$3 = slot0 == null || !slot0.hasItem();
        return !$$3 && ItemStack.isSameItemSameTags(itemStack1, slot0.getItem()) ? slot0.getItem().getCount() + (boolean2 ? 0 : itemStack1.getCount()) <= itemStack1.getMaxStackSize() : $$3;
    }

    public static int getQuickCraftPlaceCount(Set<Slot> setSlot0, int int1, ItemStack itemStack2) {
        return switch(int1) {
            case 0 ->
                Mth.floor((float) itemStack2.getCount() / (float) setSlot0.size());
            case 1 ->
                1;
            case 2 ->
                itemStack2.getItem().getMaxStackSize();
            default ->
                itemStack2.getCount();
        };
    }

    public boolean canDragTo(Slot slot0) {
        return true;
    }

    public static int getRedstoneSignalFromBlockEntity(@Nullable BlockEntity blockEntity0) {
        return blockEntity0 instanceof Container ? getRedstoneSignalFromContainer((Container) blockEntity0) : 0;
    }

    public static int getRedstoneSignalFromContainer(@Nullable Container container0) {
        if (container0 == null) {
            return 0;
        } else {
            int $$1 = 0;
            float $$2 = 0.0F;
            for (int $$3 = 0; $$3 < container0.getContainerSize(); $$3++) {
                ItemStack $$4 = container0.getItem($$3);
                if (!$$4.isEmpty()) {
                    $$2 += (float) $$4.getCount() / (float) Math.min(container0.getMaxStackSize(), $$4.getMaxStackSize());
                    $$1++;
                }
            }
            $$2 /= (float) container0.getContainerSize();
            return Mth.floor($$2 * 14.0F) + ($$1 > 0 ? 1 : 0);
        }
    }

    public void setCarried(ItemStack itemStack0) {
        this.carried = itemStack0;
    }

    public ItemStack getCarried() {
        return this.carried;
    }

    public void suppressRemoteUpdates() {
        this.suppressRemoteUpdates = true;
    }

    public void resumeRemoteUpdates() {
        this.suppressRemoteUpdates = false;
    }

    public void transferState(AbstractContainerMenu abstractContainerMenu0) {
        Table<Container, Integer, Integer> $$1 = HashBasedTable.create();
        for (int $$2 = 0; $$2 < abstractContainerMenu0.slots.size(); $$2++) {
            Slot $$3 = abstractContainerMenu0.slots.get($$2);
            $$1.put($$3.container, $$3.getContainerSlot(), $$2);
        }
        for (int $$4 = 0; $$4 < this.slots.size(); $$4++) {
            Slot $$5 = this.slots.get($$4);
            Integer $$6 = (Integer) $$1.get($$5.container, $$5.getContainerSlot());
            if ($$6 != null) {
                this.lastSlots.set($$4, abstractContainerMenu0.lastSlots.get($$6));
                this.remoteSlots.set($$4, abstractContainerMenu0.remoteSlots.get($$6));
            }
        }
    }

    public OptionalInt findSlot(Container container0, int int1) {
        for (int $$2 = 0; $$2 < this.slots.size(); $$2++) {
            Slot $$3 = this.slots.get($$2);
            if ($$3.container == container0 && int1 == $$3.getContainerSlot()) {
                return OptionalInt.of($$2);
            }
        }
        return OptionalInt.empty();
    }

    public int getStateId() {
        return this.stateId;
    }

    public int incrementStateId() {
        this.stateId = this.stateId + 1 & 32767;
        return this.stateId;
    }
}