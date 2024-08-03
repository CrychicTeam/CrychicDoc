package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseContainerBlockEntity extends BlockEntity implements Container, MenuProvider, Nameable {

    private LockCode lockKey = LockCode.NO_LOCK;

    @Nullable
    private Component name;

    protected BaseContainerBlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        super(blockEntityType0, blockPos1, blockState2);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.lockKey = LockCode.fromTag(compoundTag0);
        if (compoundTag0.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(compoundTag0.getString("CustomName"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        this.lockKey.addToTag(compoundTag0);
        if (this.name != null) {
            compoundTag0.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    public void setCustomName(Component component0) {
        this.name = component0;
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    protected abstract Component getDefaultName();

    public boolean canOpen(Player player0) {
        return canUnlock(player0, this.lockKey, this.getDisplayName());
    }

    public static boolean canUnlock(Player player0, LockCode lockCode1, Component component2) {
        if (!player0.isSpectator() && !lockCode1.unlocksWith(player0.m_21205_())) {
            player0.displayClientMessage(Component.translatable("container.isLocked", component2), true);
            player0.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        return this.canOpen(player2) ? this.createMenu(int0, inventory1) : null;
    }

    protected abstract AbstractContainerMenu createMenu(int var1, Inventory var2);
}