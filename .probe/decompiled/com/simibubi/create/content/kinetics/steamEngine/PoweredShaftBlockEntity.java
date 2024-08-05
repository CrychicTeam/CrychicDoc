package com.simibubi.create.content.kinetics.steamEngine;

import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class PoweredShaftBlockEntity extends GeneratingKineticBlockEntity {

    public BlockPos enginePos;

    public float engineEfficiency;

    public int movementDirection = 1;

    public int initialTicks = 3;

    public Block capacityKey;

    public PoweredShaftBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.initialTicks > 0) {
            this.initialTicks--;
        }
    }

    public void update(BlockPos sourcePos, int direction, float efficiency) {
        BlockPos key = this.f_58858_.subtract(sourcePos);
        this.enginePos = key;
        float prev = this.engineEfficiency;
        this.engineEfficiency = efficiency;
        int prevDirection = this.movementDirection;
        if (!Mth.equal(efficiency, prev) || prevDirection != direction) {
            this.capacityKey = this.f_58857_.getBlockState(sourcePos).m_60734_();
            this.movementDirection = direction;
            this.updateGeneratedRotation();
        }
    }

    public void remove(BlockPos sourcePos) {
        if (this.isPoweredBy(sourcePos)) {
            this.enginePos = null;
            this.engineEfficiency = 0.0F;
            this.movementDirection = 0;
            this.capacityKey = null;
            this.updateGeneratedRotation();
        }
    }

    public boolean canBePoweredBy(BlockPos globalPos) {
        return this.initialTicks == 0 && (this.enginePos == null || this.isPoweredBy(globalPos));
    }

    public boolean isPoweredBy(BlockPos globalPos) {
        BlockPos key = this.f_58858_.subtract(globalPos);
        return key.equals(this.enginePos);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Direction", this.movementDirection);
        if (this.initialTicks > 0) {
            compound.putInt("Warmup", this.initialTicks);
        }
        if (this.enginePos != null && this.capacityKey != null) {
            compound.put("EnginePos", NbtUtils.writeBlockPos(this.enginePos));
            compound.putFloat("EnginePower", this.engineEfficiency);
            compound.putString("EngineType", RegisteredObjects.getKeyOrThrow(this.capacityKey).toString());
        }
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.movementDirection = compound.getInt("Direction");
        this.initialTicks = compound.getInt("Warmup");
        this.enginePos = null;
        this.engineEfficiency = 0.0F;
        if (compound.contains("EnginePos")) {
            this.enginePos = NbtUtils.readBlockPos(compound.getCompound("EnginePos"));
            this.engineEfficiency = compound.getFloat("EnginePower");
            this.capacityKey = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("EngineType")));
        }
    }

    @Override
    public float getGeneratedSpeed() {
        return this.getCombinedCapacity() > 0.0F ? (float) (this.movementDirection * 16 * this.getSpeedModifier()) : 0.0F;
    }

    private float getCombinedCapacity() {
        return this.capacityKey == null ? 0.0F : (float) ((double) this.engineEfficiency * BlockStressValues.getCapacity(this.capacityKey));
    }

    private int getSpeedModifier() {
        return (int) (1.0 + (this.engineEfficiency >= 1.0F ? 3.0 : Math.min(2.0, Math.floor((double) (this.engineEfficiency * 4.0F)))));
    }

    @Override
    public float calculateAddedStressCapacity() {
        float capacity = this.getCombinedCapacity() / (float) this.getSpeedModifier();
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    public int getRotationAngleOffset(Direction.Axis axis) {
        int combinedCoords = axis.choose(this.f_58858_.m_123341_(), this.f_58858_.m_123342_(), this.f_58858_.m_123343_());
        return super.getRotationAngleOffset(axis) + (combinedCoords % 2 == 0 ? 180 : 0);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

    public boolean addToEngineTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }
}