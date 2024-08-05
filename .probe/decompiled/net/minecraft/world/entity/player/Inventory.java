package net.minecraft.world.entity.player;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class Inventory implements Container, Nameable {

    public static final int POP_TIME_DURATION = 5;

    public static final int INVENTORY_SIZE = 36;

    private static final int SELECTION_SIZE = 9;

    public static final int SLOT_OFFHAND = 40;

    public static final int NOT_FOUND_INDEX = -1;

    public static final int[] ALL_ARMOR_SLOTS = new int[] { 0, 1, 2, 3 };

    public static final int[] HELMET_SLOT_ONLY = new int[] { 3 };

    public final NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);

    public final NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);

    public final NonNullList<ItemStack> offhand = NonNullList.withSize(1, ItemStack.EMPTY);

    private final List<NonNullList<ItemStack>> compartments = ImmutableList.of(this.items, this.armor, this.offhand);

    public int selected;

    public final Player player;

    private int timesChanged;

    public Inventory(Player player0) {
        this.player = player0;
    }

    public ItemStack getSelected() {
        return isHotbarSlot(this.selected) ? this.items.get(this.selected) : ItemStack.EMPTY;
    }

    public static int getSelectionSize() {
        return 9;
    }

    private boolean hasRemainingSpaceForItem(ItemStack itemStack0, ItemStack itemStack1) {
        return !itemStack0.isEmpty() && ItemStack.isSameItemSameTags(itemStack0, itemStack1) && itemStack0.isStackable() && itemStack0.getCount() < itemStack0.getMaxStackSize() && itemStack0.getCount() < this.m_6893_();
    }

    public int getFreeSlot() {
        for (int $$0 = 0; $$0 < this.items.size(); $$0++) {
            if (this.items.get($$0).isEmpty()) {
                return $$0;
            }
        }
        return -1;
    }

    public void setPickedItem(ItemStack itemStack0) {
        int $$1 = this.findSlotMatchingItem(itemStack0);
        if (isHotbarSlot($$1)) {
            this.selected = $$1;
        } else {
            if ($$1 == -1) {
                this.selected = this.getSuitableHotbarSlot();
                if (!this.items.get(this.selected).isEmpty()) {
                    int $$2 = this.getFreeSlot();
                    if ($$2 != -1) {
                        this.items.set($$2, this.items.get(this.selected));
                    }
                }
                this.items.set(this.selected, itemStack0);
            } else {
                this.pickSlot($$1);
            }
        }
    }

    public void pickSlot(int int0) {
        this.selected = this.getSuitableHotbarSlot();
        ItemStack $$1 = this.items.get(this.selected);
        this.items.set(this.selected, this.items.get(int0));
        this.items.set(int0, $$1);
    }

    public static boolean isHotbarSlot(int int0) {
        return int0 >= 0 && int0 < 9;
    }

    public int findSlotMatchingItem(ItemStack itemStack0) {
        for (int $$1 = 0; $$1 < this.items.size(); $$1++) {
            if (!this.items.get($$1).isEmpty() && ItemStack.isSameItemSameTags(itemStack0, this.items.get($$1))) {
                return $$1;
            }
        }
        return -1;
    }

    public int findSlotMatchingUnusedItem(ItemStack itemStack0) {
        for (int $$1 = 0; $$1 < this.items.size(); $$1++) {
            ItemStack $$2 = this.items.get($$1);
            if (!this.items.get($$1).isEmpty() && ItemStack.isSameItemSameTags(itemStack0, this.items.get($$1)) && !this.items.get($$1).isDamaged() && !$$2.isEnchanted() && !$$2.hasCustomHoverName()) {
                return $$1;
            }
        }
        return -1;
    }

    public int getSuitableHotbarSlot() {
        for (int $$0 = 0; $$0 < 9; $$0++) {
            int $$1 = (this.selected + $$0) % 9;
            if (this.items.get($$1).isEmpty()) {
                return $$1;
            }
        }
        for (int $$2 = 0; $$2 < 9; $$2++) {
            int $$3 = (this.selected + $$2) % 9;
            if (!this.items.get($$3).isEnchanted()) {
                return $$3;
            }
        }
        return this.selected;
    }

    public void swapPaint(double double0) {
        int $$1 = (int) Math.signum(double0);
        this.selected -= $$1;
        while (this.selected < 0) {
            this.selected += 9;
        }
        while (this.selected >= 9) {
            this.selected -= 9;
        }
    }

    public int clearOrCountMatchingItems(Predicate<ItemStack> predicateItemStack0, int int1, Container container2) {
        int $$3 = 0;
        boolean $$4 = int1 == 0;
        $$3 += ContainerHelper.clearOrCountMatchingItems(this, predicateItemStack0, int1 - $$3, $$4);
        $$3 += ContainerHelper.clearOrCountMatchingItems(container2, predicateItemStack0, int1 - $$3, $$4);
        ItemStack $$5 = this.player.containerMenu.getCarried();
        $$3 += ContainerHelper.clearOrCountMatchingItems($$5, predicateItemStack0, int1 - $$3, $$4);
        if ($$5.isEmpty()) {
            this.player.containerMenu.setCarried(ItemStack.EMPTY);
        }
        return $$3;
    }

    private int addResource(ItemStack itemStack0) {
        int $$1 = this.getSlotWithRemainingSpace(itemStack0);
        if ($$1 == -1) {
            $$1 = this.getFreeSlot();
        }
        return $$1 == -1 ? itemStack0.getCount() : this.addResource($$1, itemStack0);
    }

    private int addResource(int int0, ItemStack itemStack1) {
        Item $$2 = itemStack1.getItem();
        int $$3 = itemStack1.getCount();
        ItemStack $$4 = this.getItem(int0);
        if ($$4.isEmpty()) {
            $$4 = new ItemStack($$2, 0);
            if (itemStack1.hasTag()) {
                $$4.setTag(itemStack1.getTag().copy());
            }
            this.setItem(int0, $$4);
        }
        int $$5 = $$3;
        if ($$3 > $$4.getMaxStackSize() - $$4.getCount()) {
            $$5 = $$4.getMaxStackSize() - $$4.getCount();
        }
        if ($$5 > this.m_6893_() - $$4.getCount()) {
            $$5 = this.m_6893_() - $$4.getCount();
        }
        if ($$5 == 0) {
            return $$3;
        } else {
            $$3 -= $$5;
            $$4.grow($$5);
            $$4.setPopTime(5);
            return $$3;
        }
    }

    public int getSlotWithRemainingSpace(ItemStack itemStack0) {
        if (this.hasRemainingSpaceForItem(this.getItem(this.selected), itemStack0)) {
            return this.selected;
        } else if (this.hasRemainingSpaceForItem(this.getItem(40), itemStack0)) {
            return 40;
        } else {
            for (int $$1 = 0; $$1 < this.items.size(); $$1++) {
                if (this.hasRemainingSpaceForItem(this.items.get($$1), itemStack0)) {
                    return $$1;
                }
            }
            return -1;
        }
    }

    public void tick() {
        for (NonNullList<ItemStack> $$0 : this.compartments) {
            for (int $$1 = 0; $$1 < $$0.size(); $$1++) {
                if (!$$0.get($$1).isEmpty()) {
                    $$0.get($$1).inventoryTick(this.player.m_9236_(), this.player, $$1, this.selected == $$1);
                }
            }
        }
    }

    public boolean add(ItemStack itemStack0) {
        return this.add(-1, itemStack0);
    }

    public boolean add(int int0, ItemStack itemStack1) {
        if (itemStack1.isEmpty()) {
            return false;
        } else {
            try {
                if (itemStack1.isDamaged()) {
                    if (int0 == -1) {
                        int0 = this.getFreeSlot();
                    }
                    if (int0 >= 0) {
                        this.items.set(int0, itemStack1.copyAndClear());
                        this.items.get(int0).setPopTime(5);
                        return true;
                    } else if (this.player.getAbilities().instabuild) {
                        itemStack1.setCount(0);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    int $$2;
                    do {
                        $$2 = itemStack1.getCount();
                        if (int0 == -1) {
                            itemStack1.setCount(this.addResource(itemStack1));
                        } else {
                            itemStack1.setCount(this.addResource(int0, itemStack1));
                        }
                    } while (!itemStack1.isEmpty() && itemStack1.getCount() < $$2);
                    if (itemStack1.getCount() == $$2 && this.player.getAbilities().instabuild) {
                        itemStack1.setCount(0);
                        return true;
                    } else {
                        return itemStack1.getCount() < $$2;
                    }
                }
            } catch (Throwable var6) {
                CrashReport $$4 = CrashReport.forThrowable(var6, "Adding item to inventory");
                CrashReportCategory $$5 = $$4.addCategory("Item being added");
                $$5.setDetail("Item ID", Item.getId(itemStack1.getItem()));
                $$5.setDetail("Item data", itemStack1.getDamageValue());
                $$5.setDetail("Item name", (CrashReportDetail<String>) (() -> itemStack1.getHoverName().getString()));
                throw new ReportedException($$4);
            }
        }
    }

    public void placeItemBackInInventory(ItemStack itemStack0) {
        this.placeItemBackInInventory(itemStack0, true);
    }

    public void placeItemBackInInventory(ItemStack itemStack0, boolean boolean1) {
        while (!itemStack0.isEmpty()) {
            int $$2 = this.getSlotWithRemainingSpace(itemStack0);
            if ($$2 == -1) {
                $$2 = this.getFreeSlot();
            }
            if ($$2 == -1) {
                this.player.drop(itemStack0, false);
                break;
            }
            int $$3 = itemStack0.getMaxStackSize() - this.getItem($$2).getCount();
            if (this.add($$2, itemStack0.split($$3)) && boolean1 && this.player instanceof ServerPlayer) {
                ((ServerPlayer) this.player).connection.send(new ClientboundContainerSetSlotPacket(-2, 0, $$2, this.getItem($$2)));
            }
        }
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        List<ItemStack> $$2 = null;
        for (NonNullList<ItemStack> $$3 : this.compartments) {
            if (int0 < $$3.size()) {
                $$2 = $$3;
                break;
            }
            int0 -= $$3.size();
        }
        return $$2 != null && !((ItemStack) $$2.get(int0)).isEmpty() ? ContainerHelper.removeItem($$2, int0, int1) : ItemStack.EMPTY;
    }

    public void removeItem(ItemStack itemStack0) {
        for (NonNullList<ItemStack> $$1 : this.compartments) {
            for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
                if ($$1.get($$2) == itemStack0) {
                    $$1.set($$2, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        NonNullList<ItemStack> $$1 = null;
        for (NonNullList<ItemStack> $$2 : this.compartments) {
            if (int0 < $$2.size()) {
                $$1 = $$2;
                break;
            }
            int0 -= $$2.size();
        }
        if ($$1 != null && !$$1.get(int0).isEmpty()) {
            ItemStack $$3 = $$1.get(int0);
            $$1.set(int0, ItemStack.EMPTY);
            return $$3;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        NonNullList<ItemStack> $$2 = null;
        for (NonNullList<ItemStack> $$3 : this.compartments) {
            if (int0 < $$3.size()) {
                $$2 = $$3;
                break;
            }
            int0 -= $$3.size();
        }
        if ($$2 != null) {
            $$2.set(int0, itemStack1);
        }
    }

    public float getDestroySpeed(BlockState blockState0) {
        return this.items.get(this.selected).getDestroySpeed(blockState0);
    }

    public ListTag save(ListTag listTag0) {
        for (int $$1 = 0; $$1 < this.items.size(); $$1++) {
            if (!this.items.get($$1).isEmpty()) {
                CompoundTag $$2 = new CompoundTag();
                $$2.putByte("Slot", (byte) $$1);
                this.items.get($$1).save($$2);
                listTag0.add($$2);
            }
        }
        for (int $$3 = 0; $$3 < this.armor.size(); $$3++) {
            if (!this.armor.get($$3).isEmpty()) {
                CompoundTag $$4 = new CompoundTag();
                $$4.putByte("Slot", (byte) ($$3 + 100));
                this.armor.get($$3).save($$4);
                listTag0.add($$4);
            }
        }
        for (int $$5 = 0; $$5 < this.offhand.size(); $$5++) {
            if (!this.offhand.get($$5).isEmpty()) {
                CompoundTag $$6 = new CompoundTag();
                $$6.putByte("Slot", (byte) ($$5 + 150));
                this.offhand.get($$5).save($$6);
                listTag0.add($$6);
            }
        }
        return listTag0;
    }

    public void load(ListTag listTag0) {
        this.items.clear();
        this.armor.clear();
        this.offhand.clear();
        for (int $$1 = 0; $$1 < listTag0.size(); $$1++) {
            CompoundTag $$2 = listTag0.getCompound($$1);
            int $$3 = $$2.getByte("Slot") & 255;
            ItemStack $$4 = ItemStack.of($$2);
            if (!$$4.isEmpty()) {
                if ($$3 >= 0 && $$3 < this.items.size()) {
                    this.items.set($$3, $$4);
                } else if ($$3 >= 100 && $$3 < this.armor.size() + 100) {
                    this.armor.set($$3 - 100, $$4);
                } else if ($$3 >= 150 && $$3 < this.offhand.size() + 150) {
                    this.offhand.set($$3 - 150, $$4);
                }
            }
        }
    }

    @Override
    public int getContainerSize() {
        return this.items.size() + this.armor.size() + this.offhand.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack $$0 : this.items) {
            if (!$$0.isEmpty()) {
                return false;
            }
        }
        for (ItemStack $$1 : this.armor) {
            if (!$$1.isEmpty()) {
                return false;
            }
        }
        for (ItemStack $$2 : this.offhand) {
            if (!$$2.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int int0) {
        List<ItemStack> $$1 = null;
        for (NonNullList<ItemStack> $$2 : this.compartments) {
            if (int0 < $$2.size()) {
                $$1 = $$2;
                break;
            }
            int0 -= $$2.size();
        }
        return $$1 == null ? ItemStack.EMPTY : (ItemStack) $$1.get(int0);
    }

    @Override
    public Component getName() {
        return Component.translatable("container.inventory");
    }

    public ItemStack getArmor(int int0) {
        return this.armor.get(int0);
    }

    public void hurtArmor(DamageSource damageSource0, float float1, int[] int2) {
        if (!(float1 <= 0.0F)) {
            float1 /= 4.0F;
            if (float1 < 1.0F) {
                float1 = 1.0F;
            }
            for (int $$3 : int2) {
                ItemStack $$4 = this.armor.get($$3);
                if ((!damageSource0.is(DamageTypeTags.IS_FIRE) || !$$4.getItem().isFireResistant()) && $$4.getItem() instanceof ArmorItem) {
                    $$4.hurtAndBreak((int) float1, this.player, p_35997_ -> p_35997_.m_21166_(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, $$3)));
                }
            }
        }
    }

    public void dropAll() {
        for (List<ItemStack> $$0 : this.compartments) {
            for (int $$1 = 0; $$1 < $$0.size(); $$1++) {
                ItemStack $$2 = (ItemStack) $$0.get($$1);
                if (!$$2.isEmpty()) {
                    this.player.drop($$2, true, false);
                    $$0.set($$1, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public void setChanged() {
        this.timesChanged++;
    }

    public int getTimesChanged() {
        return this.timesChanged;
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.player.m_213877_() ? false : !(player0.m_20280_(this.player) > 64.0);
    }

    public boolean contains(ItemStack itemStack0) {
        for (List<ItemStack> $$1 : this.compartments) {
            for (ItemStack $$2 : $$1) {
                if (!$$2.isEmpty() && ItemStack.isSameItemSameTags($$2, itemStack0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contains(TagKey<Item> tagKeyItem0) {
        for (List<ItemStack> $$1 : this.compartments) {
            for (ItemStack $$2 : $$1) {
                if (!$$2.isEmpty() && $$2.is(tagKeyItem0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void replaceWith(Inventory inventory0) {
        for (int $$1 = 0; $$1 < this.getContainerSize(); $$1++) {
            this.setItem($$1, inventory0.getItem($$1));
        }
        this.selected = inventory0.selected;
    }

    @Override
    public void clearContent() {
        for (List<ItemStack> $$0 : this.compartments) {
            $$0.clear();
        }
    }

    public void fillStackedContents(StackedContents stackedContents0) {
        for (ItemStack $$1 : this.items) {
            stackedContents0.accountSimpleStack($$1);
        }
    }

    public ItemStack removeFromSelected(boolean boolean0) {
        ItemStack $$1 = this.getSelected();
        return $$1.isEmpty() ? ItemStack.EMPTY : this.removeItem(this.selected, boolean0 ? $$1.getCount() : 1);
    }
}