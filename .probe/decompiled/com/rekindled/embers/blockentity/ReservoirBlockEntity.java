package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersBlockTags;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.util.Misc;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ReservoirBlockEntity extends OpenTankBlockEntity {

    int ticksExisted = 0;

    public float renderOffset;

    int previousFluid;

    public int height = 0;

    public ReservoirBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.RESERVOIR_ENTITY.get(), pPos, pBlockState);
        this.tank = new FluidTank(Integer.MAX_VALUE) {

            @Override
            public void onContentsChanged() {
                ReservoirBlockEntity.this.m_6596_();
            }

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                if (Misc.isGaseousFluid(resource)) {
                    ReservoirBlockEntity.this.setEscapedFluid(resource);
                    return resource.getAmount();
                } else {
                    return super.fill(resource, action);
                }
            }
        };
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, 1, -1), this.f_58858_.offset(2, 1 + this.height, 2));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.m_183515_(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluid();
    }

    public FluidTank getTank() {
        return this.tank;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return facing != Direction.DOWN && facing != null ? LazyOptional.empty() : super.getCapability(capability, facing);
    }

    public void updateCapacity() {
        int capacity = 0;
        this.height = 0;
        for (int i = 1; this.f_58857_.getBlockState(this.f_58858_.above(i)).m_204336_(EmbersBlockTags.RESERVOIR_EXPANSION); i++) {
            capacity += ConfigManager.RESERVOIR_CAPACITY.get();
            this.height++;
        }
        if (this.tank.getCapacity() != capacity) {
            this.tank.setCapacity(capacity);
            int amount = this.tank.getFluidAmount();
            if (amount > capacity) {
                this.tank.drain(amount - capacity, IFluidHandler.FluidAction.EXECUTE);
            }
            this.m_6596_();
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ReservoirBlockEntity blockEntity) {
        commonTick(level, pos, state, blockEntity);
        if (blockEntity.ticksExisted == 1) {
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        if (blockEntity.tank.getFluidAmount() != blockEntity.previousFluid) {
            blockEntity.renderOffset = blockEntity.renderOffset + (float) blockEntity.tank.getFluidAmount() - (float) blockEntity.previousFluid;
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        if (blockEntity.shouldEmitParticles()) {
            blockEntity.updateEscapeParticles();
        }
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, ReservoirBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        if (blockEntity.ticksExisted % 20 == 0) {
            blockEntity.updateCapacity();
        }
    }

    @Override
    protected void updateEscapeParticles() {
        Vector3f color = IClientFluidTypeExtensions.of(this.lastEscaped.getFluid().getFluidType()).modifyFogColor(Minecraft.getInstance().gameRenderer.getMainCamera(), 0.0F, (ClientLevel) this.f_58857_, 6, 0.0F, new Vector3f(1.0F, 1.0F, 1.0F));
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            float xOffset = 0.5F + (random.nextFloat() - 0.5F) * 2.0F * 0.2F;
            float yOffset = (float) this.height + 0.9F;
            float zOffset = 0.5F + (random.nextFloat() - 0.5F) * 2.0F * 0.2F;
            this.f_58857_.addParticle(new VaporParticleOptions(color, 2.0F), (double) ((float) this.f_58858_.m_123341_() + xOffset), (double) ((float) this.f_58858_.m_123342_() + yOffset), (double) ((float) this.f_58858_.m_123343_() + zOffset), 0.0, 0.2F, 0.0);
        }
    }
}