package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.entity.EmberPacketEntity;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.particle.StarParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class EmberReceiverBlockEntity extends BlockEntity implements IEmberPacketReceiver {

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            EmberReceiverBlockEntity.this.m_6596_();
        }

        @Override
        public boolean acceptsVolatile() {
            return false;
        }
    };

    public static final int TRANSFER_RATE = 10;

    public long ticksExisted = 0L;

    public Random random = new Random();

    public EmberReceiverBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_RECEIVER_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(2000.0);
    }

    public EmberReceiverBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EmberReceiverBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
        BlockEntity attachedTile = level.getBlockEntity(pos.relative(facing, -1));
        if (blockEntity.ticksExisted % 2L == 0L && attachedTile != null) {
            IEmberCapability cap = (IEmberCapability) attachedTile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing).orElse(null);
            if (cap != null && cap.getEmber() < cap.getEmberCapacity() && blockEntity.capability.getEmber() > 0.0) {
                double added = cap.addAmount(Math.min(10.0, blockEntity.capability.getEmber()), true);
                blockEntity.capability.removeAmount(added, true);
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    @Override
    public boolean hasRoomFor(double ember) {
        return this.capability.getEmber() * 2.0 <= this.capability.getEmberCapacity();
    }

    @Override
    public boolean onReceive(EmberPacketEntity packet) {
        if (this.f_58857_ instanceof ServerLevel serverLevel) {
            if (this.capability.getEmber() + packet.value > this.capability.getEmberCapacity()) {
                serverLevel.sendParticles(new SparkParticleOptions(GlowParticleOptions.EMBER_COLOR, this.random.nextFloat() * 0.75F + 0.45F), (double) this.m_58899_().m_123341_() + 0.5, (double) this.m_58899_().m_123342_() + 0.5, (double) this.m_58899_().m_123343_() + 0.5, 5, (double) (0.125F * (this.random.nextFloat() - 0.5F)), (double) (0.125F * this.random.nextFloat()), (double) (0.125F * (this.random.nextFloat() - 0.5F)), 1.0);
                serverLevel.sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 2.0F + this.random.nextFloat() * 2.0F), (double) this.m_58899_().m_123341_() + 0.5, (double) this.m_58899_().m_123342_() + 0.5, (double) this.m_58899_().m_123343_() + 0.5, 15, (double) (0.0625F * (this.random.nextFloat() - 0.5F)), (double) (0.0625F + 0.0625F * (this.random.nextFloat() - 0.5F)), (double) (0.0625F * (this.random.nextFloat() - 0.5F)), 1.0);
            } else {
                serverLevel.sendParticles(new StarParticleOptions(GlowParticleOptions.EMBER_COLOR, 3.5F + 0.5F * this.random.nextFloat()), (double) this.m_58899_().m_123341_() + 0.5, (double) this.m_58899_().m_123342_() + 0.5, (double) this.m_58899_().m_123343_() + 0.5, 12, (double) (0.0125F * (this.random.nextFloat() - 0.5F)), (double) (0.0125F * (this.random.nextFloat() - 0.5F)), (double) (0.0125F * (this.random.nextFloat() - 0.5F)), 0.0);
            }
        }
        this.f_58857_.playLocalSound(packet.m_20185_(), packet.m_20186_(), packet.m_20189_(), packet.value >= 100.0 ? EmbersSounds.EMBER_RECEIVE_BIG.get() : EmbersSounds.EMBER_RECEIVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        return true;
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }
}