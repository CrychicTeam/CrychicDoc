package com.github.alexthe666.iceandfire.entity.tile;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityMyrmexCocoon extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> chestContents = NonNullList.withSize(18, ItemStack.EMPTY);

    public TileEntityMyrmexCocoon(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.MYRMEX_COCOON.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.chestContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.m_142466_(compound);
        this.chestContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compound)) {
            ContainerHelper.loadAllItems(compound, this.chestContents);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        if (!this.m_59634_(compound)) {
            ContainerHelper.saveAllItems(compound, this.chestContents);
        }
    }

    @NotNull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.myrmex_cocoon");
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return new ChestMenu(MenuType.GENERIC_9x2, id, player, this, 2);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ChestMenu(MenuType.GENERIC_9x2, id, playerInventory, this, 2);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @NotNull
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemsIn) {
    }

    @Override
    public void startOpen(Player player) {
        this.m_59640_(null);
        player.m_9236_().playLocalSound((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), SoundEvents.SLIME_JUMP, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    @Override
    public void stopOpen(Player player) {
        this.m_59640_(null);
        player.m_9236_().playLocalSound((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, 1.0F, 1.0F, false);
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

    public boolean isFull(ItemStack heldStack) {
        for (ItemStack itemstack : this.chestContents) {
            if (itemstack.isEmpty() || heldStack != null && !heldStack.isEmpty() && ItemStack.isSameItem(itemstack, heldStack) && itemstack.getCount() + heldStack.getCount() < itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }
}