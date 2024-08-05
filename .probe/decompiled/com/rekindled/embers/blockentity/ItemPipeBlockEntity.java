package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.particle.VaporParticleOptions;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ItemPipeBlockEntity extends ItemPipeBlockEntityBase {

    IItemHandler[] sideHandlers;

    public ItemPipeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ITEM_PIPE_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void initInventory() {
        super.initInventory();
        this.sideHandlers = new IItemHandler[Direction.values().length];
        for (final Direction facing : Direction.values()) {
            this.sideHandlers[facing.get3DDataValue()] = new IItemHandler() {

                @Override
                public int getSlots() {
                    return ItemPipeBlockEntity.this.inventory.getSlots();
                }

                @Nonnull
                @Override
                public ItemStack getStackInSlot(int slot) {
                    return ItemPipeBlockEntity.this.inventory.getStackInSlot(slot);
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    if (!simulate) {
                        ItemPipeBlockEntity.this.setFrom(facing, true);
                    }
                    return ItemPipeBlockEntity.this.inventory.insertItem(slot, stack, simulate);
                }

                @Nonnull
                @Override
                public ItemStack extractItem(int slot, int amount, boolean simulate) {
                    return ItemPipeBlockEntity.this.inventory.extractItem(slot, amount, simulate);
                }

                @Override
                public int getSlotLimit(int slot) {
                    return ItemPipeBlockEntity.this.inventory.getSlotLimit(slot);
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    return true;
                }
            };
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ItemPipeBlockEntity blockEntity) {
        if (level instanceof ServerLevel && blockEntity.clogged && blockEntity.isAnySideUnclogged()) {
            Random posRand = new Random(pos.asLong());
            double angleA = posRand.nextDouble() * Math.PI * 2.0;
            double angleB = posRand.nextDouble() * Math.PI * 2.0;
            float xOffset = (float) (Math.cos(angleA) * Math.cos(angleB));
            float yOffset = (float) (Math.sin(angleA) * Math.cos(angleB));
            float zOffset = (float) Math.sin(angleB);
            float speed = 0.1F;
            float vx = xOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vy = yOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vz = zOffset * speed + posRand.nextFloat() * speed * 0.3F;
            ((ServerLevel) level).sendParticles(new VaporParticleOptions(new Vector3f(0.2509804F, 0.2509804F, 0.2509804F), new Vec3((double) vx, (double) vy, (double) vz), 1.0F), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 4, 0.0, 0.0, 0.0, 1.0);
        }
        ItemPipeBlockEntityBase.serverTick(level, pos, state, blockEntity);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
            }
            if (this.getConnection(side).transfer) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> this.sideHandlers[side.get3DDataValue()]));
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public int getCapacity() {
        return 4;
    }
}