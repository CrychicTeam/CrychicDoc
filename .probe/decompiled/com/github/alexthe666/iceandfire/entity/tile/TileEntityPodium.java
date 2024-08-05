package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.inventory.ContainerPodium;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import com.github.alexthe666.iceandfire.message.MessageUpdatePodium;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

public class TileEntityPodium extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int[] slotsTop = new int[] { 0 };

    public int ticksExisted;

    public int prevTicksExisted;

    IItemHandler handlerUp = new SidedInvWrapper(this, Direction.UP);

    IItemHandler handlerDown = new SidedInvWrapper(this, Direction.DOWN);

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityPodium(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.PODIUM.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityPodium entityPodium) {
        entityPodium.prevTicksExisted = entityPodium.ticksExisted++;
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_, this.f_58858_.offset(1, 3, 1));
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @NotNull
    @Override
    public ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    @NotNull
    @Override
    public ItemStack removeItem(int index, int count) {
        if (!this.stacks.get(index).isEmpty()) {
            if (this.stacks.get(index).getCount() <= count) {
                ItemStack itemstack = this.stacks.get(index);
                this.stacks.set(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                ItemStack itemstack = this.stacks.get(index).split(count);
                if (this.stacks.get(index).isEmpty()) {
                    this.stacks.set(index, ItemStack.EMPTY);
                }
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack = this.stacks.get(index);
            this.stacks.set(index, itemstack);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        this.stacks.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.saveAdditional(this.getUpdateTag());
        if (!this.f_58857_.isClientSide) {
            IceAndFire.sendMSGToAll(new MessageUpdatePodium(this.m_58899_().asLong(), this.stacks.get(0)));
        }
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        ContainerHelper.saveAllItems(compound, this.stacks);
    }

    @Override
    public void startOpen(@NotNull Player player) {
    }

    @Override
    public void stopOpen(@NotNull Player player) {
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, Direction direction) {
        return index != 0 || stack.getItem() instanceof ItemDragonEgg || stack.getItem() instanceof ItemMyrmexEgg;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull Direction side) {
        return slotsTop;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        return false;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return false;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187480_();
    }

    @NotNull
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return this.getDefaultName();
    }

    @NotNull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.iceandfire.podium");
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (this.f_58859_ || facing == null || capability != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(capability, facing);
        } else {
            return facing == Direction.DOWN ? this.handlers[1].cast() : this.handlers[0].cast();
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ContainerPodium(id, this, playerInventory, new SimpleContainerData(0));
    }
}