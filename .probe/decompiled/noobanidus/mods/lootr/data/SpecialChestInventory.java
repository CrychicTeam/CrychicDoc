package noobanidus.mods.lootr.data;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.MenuBuilder;
import noobanidus.mods.lootr.api.inventory.ILootrInventory;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;

public class SpecialChestInventory implements ILootrInventory {

    private final Component name;

    private final ChestData newChestData;

    private NonNullList<ItemStack> contents;

    private MenuBuilder menuBuilder = null;

    public SpecialChestInventory(ChestData newChestData, NonNullList<ItemStack> contents, Component name) {
        this.newChestData = newChestData;
        if (!contents.isEmpty()) {
            this.contents = contents;
        } else {
            this.contents = NonNullList.withSize(newChestData.getSize(), ItemStack.EMPTY);
        }
        this.name = name;
    }

    public SpecialChestInventory(ChestData newChestData, CompoundTag items, String componentAsJSON) {
        this.newChestData = newChestData;
        this.name = Component.Serializer.fromJson(componentAsJSON);
        this.contents = NonNullList.withSize(newChestData.getSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(items, this.contents);
    }

    public void setMenuBuilder(MenuBuilder builder) {
        this.menuBuilder = builder;
    }

    @Nullable
    @Override
    public BaseContainerBlockEntity getBlockEntity(Level level) {
        if (level != null && !level.isClientSide() && this.newChestData.getPos() != null) {
            BlockEntity te = level.getBlockEntity(this.newChestData.getPos());
            return te instanceof BaseContainerBlockEntity ? (BaseContainerBlockEntity) te : null;
        } else {
            return null;
        }
    }

    @Nullable
    public LootrChestMinecartEntity getEntity(Level world) {
        if (world == null || world.isClientSide() || this.newChestData.getEntityId() == null) {
            return null;
        } else if (!(world instanceof ServerLevel serverWorld)) {
            return null;
        } else {
            Entity entity = serverWorld.getEntity(this.newChestData.getEntityId());
            return entity instanceof LootrChestMinecartEntity ? (LootrChestMinecartEntity) entity : null;
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockPos getPos() {
        return this.newChestData.getPos();
    }

    @Override
    public int getContainerSize() {
        return this.contents.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.contents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.contents.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(this.contents, index, count);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack result = ContainerHelper.takeItem(this.contents, index);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        return result;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.contents.set(index, stack);
        if (stack.getCount() > this.m_6893_()) {
            stack.setCount(this.m_6893_());
        }
        this.setChanged();
    }

    @Override
    public void setChanged() {
        this.newChestData.m_77762_();
    }

    @Override
    public boolean stillValid(Player player) {
        if (!player.m_9236_().dimension().equals(this.newChestData.getDimension())) {
            return false;
        } else if (this.newChestData.isEntity()) {
            if (this.newChestData.getEntityId() == null) {
                return false;
            } else if (player.m_9236_() instanceof ServerLevel serverLevel) {
                return serverLevel.getEntity(this.newChestData.getEntityId()) instanceof ContainerEntity container ? container.isChestVehicleStillValid(player) : false;
            } else {
                return true;
            }
        } else {
            BlockEntity be = player.m_9236_().getBlockEntity(this.newChestData.getPos());
            return be == null ? false : Container.stillValidBlockEntity(be, player);
        }
    }

    @Override
    public void clearContent() {
        this.contents.clear();
        this.setChanged();
    }

    @Override
    public Component getDisplayName() {
        return this.name;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if (this.menuBuilder != null) {
            return this.menuBuilder.build(id, inventory, this, this.getContainerSize() / 9);
        } else {
            return switch(this.getContainerSize()) {
                case 9 ->
                    new ChestMenu(MenuType.GENERIC_9x1, id, inventory, this, 1);
                case 18 ->
                    new ChestMenu(MenuType.GENERIC_9x2, id, inventory, this, 2);
                case 36 ->
                    new ChestMenu(MenuType.GENERIC_9x4, id, inventory, this, 4);
                case 45 ->
                    new ChestMenu(MenuType.GENERIC_9x5, id, inventory, this, 5);
                case 54 ->
                    ChestMenu.sixRows(id, inventory, this);
                default ->
                    ChestMenu.threeRows(id, inventory, this);
            };
        }
    }

    @Override
    public void startOpen(Player player) {
        Level world = player.m_9236_();
        BaseContainerBlockEntity tile = this.getBlockEntity(world);
        if (tile != null) {
            tile.m_5856_(player);
        }
        if (this.newChestData.getEntityId() != null) {
            LootrChestMinecartEntity entity = this.getEntity(world);
            if (entity != null) {
                entity.startOpen(player);
            }
        }
    }

    @Override
    public void stopOpen(Player player) {
        this.setChanged();
        Level world = player.m_9236_();
        if (this.newChestData.getPos() != null) {
            BaseContainerBlockEntity tile = this.getBlockEntity(world);
            if (tile != null) {
                tile.m_5785_(player);
            }
        }
        if (this.newChestData.getEntityId() != null) {
            LootrChestMinecartEntity entity = this.getEntity(world);
            if (entity != null) {
                entity.stopOpen(player);
            }
        }
    }

    @Nullable
    public UUID getTileId() {
        return this.newChestData == null ? null : this.newChestData.getTileId();
    }

    public CompoundTag writeItems() {
        CompoundTag result = new CompoundTag();
        return ContainerHelper.saveAllItems(result, this.contents);
    }

    public String writeName() {
        return Component.Serializer.toJson(this.name);
    }

    @Override
    public NonNullList<ItemStack> getInventoryContents() {
        return this.contents;
    }

    public void resizeInventory(int newSize) {
        if (newSize > this.contents.size()) {
            NonNullList<ItemStack> oldContents = this.contents;
            this.contents = NonNullList.withSize(newSize, ItemStack.EMPTY);
            for (int i = 0; i < oldContents.size(); i++) {
                this.contents.set(i, oldContents.get(i));
            }
            LootrAPI.LOG.info("Resized inventory with key '" + this.newChestData.getKey() + "' in dimension '" + this.newChestData.getDimension() + "' at location '" + this.newChestData.getPos() + "' from " + oldContents.size() + " slots to " + newSize + " slots.");
        } else if (newSize < this.contents.size()) {
            throw new IllegalArgumentException("Cannot resize inventory associated with '" + this.newChestData.getKey() + "' in dimension '" + this.newChestData.getDimension() + "' at location '" + this.newChestData.getPos() + "' to a smaller size.");
        }
    }
}