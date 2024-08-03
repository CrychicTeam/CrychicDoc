package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.power.IEmberPacketProducer;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.entity.EmberPacketEntity;
import com.rekindled.embers.power.DefaultEmberCapability;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class EmberEmitterBlockEntity extends BlockEntity implements IEmberPacketProducer {

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            EmberEmitterBlockEntity.this.m_6596_();
        }

        @Override
        public boolean acceptsVolatile() {
            return false;
        }
    };

    public static final double TRANSFER_RATE = 40.0;

    public static final double PULL_RATE = 10.0;

    public BlockPos target = null;

    public long ticksExisted = 0L;

    public Random random = new Random();

    public int offset = this.random.nextInt(40);

    public EmberEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_EMITTER_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(200.0);
        this.capability.setEmber(0.0);
    }

    public EmberEmitterBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("targetX")) {
            this.target = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
        }
        this.capability.deserializeNBT(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.target != null) {
            nbt.putInt("targetX", this.target.m_123341_());
            nbt.putInt("targetY", this.target.m_123342_());
            nbt.putInt("targetZ", this.target.m_123343_());
        }
        this.capability.writeToNBT(nbt);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EmberEmitterBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
        BlockEntity attachedTile = level.getBlockEntity(pos.relative(facing, -1));
        if (blockEntity.ticksExisted % 5L == 0L && attachedTile != null) {
            IEmberCapability cap = (IEmberCapability) attachedTile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing).orElse(null);
            if (cap != null && cap.getEmber() > 0.0 && blockEntity.capability.getEmber() < blockEntity.capability.getEmberCapacity()) {
                double removed = cap.removeAmount(10.0, true);
                blockEntity.capability.addAmount(removed, true);
            }
        }
        if ((blockEntity.ticksExisted + (long) blockEntity.offset) % 20L == 0L && level.m_276867_(pos) && blockEntity.target != null && level.isLoaded(blockEntity.target) && !level.isClientSide && blockEntity.capability.getEmber() > 10.0) {
            BlockEntity targetTile = level.getBlockEntity(blockEntity.target);
            if (targetTile instanceof IEmberPacketReceiver && ((IEmberPacketReceiver) targetTile).hasRoomFor(40.0)) {
                EmberPacketEntity packet = RegistryManager.EMBER_PACKET.get().create(blockEntity.f_58857_);
                Vec3 velocity = getBurstVelocity(facing);
                packet.initCustom(pos, blockEntity.target, velocity.x, velocity.y, velocity.z, Math.min(40.0, blockEntity.capability.getEmber()));
                blockEntity.capability.removeAmount(Math.min(40.0, blockEntity.capability.getEmber()), true);
                blockEntity.f_58857_.m_7967_(packet);
                level.playSound(null, pos, EmbersSounds.EMBER_EMIT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public static Vec3 getBurstVelocity(Direction facing) {
        switch(facing) {
            case DOWN:
                return new Vec3(0.0, -0.5, 0.0);
            case UP:
                return new Vec3(0.0, 0.5, 0.0);
            case NORTH:
                return new Vec3(0.0, -0.01, -0.5);
            case SOUTH:
                return new Vec3(0.0, -0.01, 0.5);
            case WEST:
                return new Vec3(-0.5, -0.01, 0.0);
            case EAST:
                return new Vec3(0.5, -0.01, 0.0);
            default:
                return Vec3.ZERO;
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY && this.f_58857_.getBlockState(this.m_58899_()).m_61143_(BlockStateProperties.FACING) != side ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }

    @Override
    public void setTargetPosition(BlockPos pos, Direction side) {
        this.target = pos;
        this.m_6596_();
    }

    @Override
    public Direction getEmittingDirection(Direction side) {
        return (Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING);
    }
}