package io.github.lightman314.lightmanscurrency.common.emergency_ejection;

import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.ownership.Owner;
import io.github.lightman314.lightmanscurrency.common.notifications.types.ejection.OwnableBlockEjectedNotification;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EjectionData implements Container, IClientTracker {

    private final OwnerData owner = new OwnerData(this, o -> {
    });

    MutableComponent traderName = Component.empty();

    List<ItemStack> items = new ArrayList();

    private boolean isClient = false;

    public MutableComponent getTraderName() {
        return this.traderName;
    }

    public void flagAsClient() {
        this.isClient = true;
    }

    @Override
    public boolean isClient() {
        return this.isClient;
    }

    private EjectionData() {
    }

    private EjectionData(OwnerData owner, MutableComponent traderName, List<ItemStack> items) {
        this.owner.copyFrom(owner);
        this.traderName = traderName;
        this.items = items;
    }

    public boolean canAccess(Player player) {
        if (LCAdminMode.isAdminPlayer(player)) {
            return true;
        } else {
            return this.owner == null ? false : this.owner.isMember(player);
        }
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.put("Owner", this.owner.save());
        compound.putString("Name", Component.Serializer.toJson(this.traderName));
        ListTag itemList = new ListTag();
        for (ItemStack item : this.items) {
            itemList.add(item.save(new CompoundTag()));
        }
        compound.put("Items", itemList);
        return compound;
    }

    public void load(CompoundTag compound) {
        if (compound.contains("Owner")) {
            this.owner.load(compound.getCompound("Owner"));
        }
        if (compound.contains("Name")) {
            this.traderName = Component.Serializer.fromJson(compound.getString("Name"));
        }
        if (compound.contains("Items")) {
            ListTag itemList = compound.getList("Items", 10);
            this.items = new ArrayList();
            for (int i = 0; i < itemList.size(); i++) {
                this.items.add(ItemStack.of(itemList.getCompound(i)));
            }
        }
    }

    public final void pushNotificationToOwner() {
        Owner owner = this.owner.getValidOwner();
        if (owner != null) {
            owner.pushNotification(OwnableBlockEjectedNotification.create(this.traderName), 1, true);
        }
    }

    public static EjectionData create(Level level, BlockPos pos, BlockState state, IDumpable trader) {
        return create(level, pos, state, trader, true);
    }

    public static EjectionData create(Level level, BlockPos pos, BlockState state, IDumpable trader, boolean dropBlock) {
        OwnerData owner = trader.getOwner();
        MutableComponent traderName = trader.getName();
        List<ItemStack> items = trader.getContents(level, pos, state, dropBlock);
        return new EjectionData(owner, traderName, items);
    }

    public static EjectionData loadData(CompoundTag compound) {
        EjectionData data = new EjectionData();
        data.load(compound);
        return data;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getItem(int slot) {
        return slot < this.items.size() && slot >= 0 ? (ItemStack) this.items.get(slot) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int slot, int count) {
        return slot < this.items.size() && slot >= 0 ? ((ItemStack) this.items.get(slot)).split(count) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot < this.items.size() && slot >= 0) {
            ItemStack stack = (ItemStack) this.items.get(slot);
            this.items.set(slot, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack item) {
        if (slot < this.items.size() && slot >= 0) {
            this.items.set(slot, item);
        }
    }

    private void clearEmptySlots() {
        this.items.removeIf(ItemStack::m_41619_);
    }

    @Override
    public void setChanged() {
        if (!this.isClient) {
            this.clearEmptySlots();
            if (this.isEmpty()) {
                EjectionSaveData.RemoveEjectionData(this);
            } else {
                EjectionSaveData.MarkEjectionDataDirty();
            }
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.canAccess(player);
    }
}