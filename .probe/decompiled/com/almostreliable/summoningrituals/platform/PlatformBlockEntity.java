package com.almostreliable.summoningrituals.platform;

import com.almostreliable.summoningrituals.inventory.AltarInventory;
import com.almostreliable.summoningrituals.inventory.ItemHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public abstract class PlatformBlockEntity extends BlockEntity {

    protected final AltarInventory inventory = new AltarInventory(this);

    private final LazyOptional<ItemHandler> inventoryCap = LazyOptional.of(() -> this.inventory);

    protected int progress;

    protected PlatformBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryCap.invalidate();
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return !this.f_58859_ && cap.equals(ForgeCapabilities.ITEM_HANDLER) && this.progress == 0 ? this.inventoryCap.cast() : super.getCapability(cap, side);
    }

    public abstract ItemStack handleInteraction(@Nullable ServerPlayer var1, ItemStack var2);

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public AltarInventory getInventory() {
        return this.inventory;
    }
}