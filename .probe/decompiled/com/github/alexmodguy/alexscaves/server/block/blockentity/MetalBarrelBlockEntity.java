package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MetalBarrelBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level p_155062_, BlockPos p_155063_, BlockState p_155064_) {
            MetalBarrelBlockEntity.this.playSound(p_155064_, ACSoundRegistry.METAL_BARREL_LID.get());
            MetalBarrelBlockEntity.this.updateBlockState(p_155064_, true);
        }

        @Override
        protected void onClose(Level p_155072_, BlockPos p_155073_, BlockState p_155074_) {
            MetalBarrelBlockEntity.this.playSound(p_155074_, ACSoundRegistry.METAL_BARREL_LID.get());
            MetalBarrelBlockEntity.this.updateBlockState(p_155074_, false);
        }

        @Override
        protected void openerCountChanged(Level p_155066_, BlockPos p_155067_, BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        @Override
        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof ChestMenu) {
                Container container = ((ChestMenu) p_155060_.containerMenu).getContainer();
                return container == MetalBarrelBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public MetalBarrelBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(ACBlockEntityRegistry.METAL_BARREL.get(), blockPos0, blockState1);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.m_183515_(compoundTag0);
        if (!this.m_59634_(compoundTag0)) {
            ContainerHelper.saveAllItems(compoundTag0, this.items);
        }
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.m_142466_(compoundTag0);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compoundTag0)) {
            ContainerHelper.loadAllItems(compoundTag0, this.items);
        }
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullListItemStack0) {
        this.items = nonNullListItemStack0;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.alexscaves.metal_barrel");
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return ChestMenu.threeRows(int0, inventory1, this);
    }

    @Override
    public void startOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            this.openersCounter.incrementOpeners(player0, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    public void stopOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            this.openersCounter.decrementOpeners(player0, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    private void updateBlockState(BlockState blockState0, boolean boolean1) {
        this.f_58857_.setBlock(this.m_58899_(), (BlockState) blockState0.m_61124_(BarrelBlock.OPEN, boolean1), 3);
    }

    private void playSound(BlockState blockState0, SoundEvent soundEvent1) {
        Vec3i vec3i = ((Direction) blockState0.m_61143_(BarrelBlock.FACING)).getNormal();
        double d0 = (double) this.f_58858_.m_123341_() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) this.f_58858_.m_123342_() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.f_58857_.playSound((Player) null, d0, d1, d2, soundEvent1, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
    }
}