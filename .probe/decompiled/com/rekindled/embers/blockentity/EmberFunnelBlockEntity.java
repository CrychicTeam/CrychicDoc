package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.entity.EmberPacketEntity;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.particle.StarParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EmberFunnelBlockEntity extends EmberReceiverBlockEntity {

    public static final int TRANSFER_RATE = 100;

    public EmberFunnelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_FUNNEL_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(2000.0);
        this.capability.setEmber(0.0);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EmberFunnelBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
        BlockEntity attachedTile = level.getBlockEntity(pos.relative(facing, -1));
        if (blockEntity.ticksExisted % 2L == 0L && attachedTile != null) {
            IEmberCapability cap = (IEmberCapability) attachedTile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing).orElse(null);
            if (cap != null && cap.getEmber() < cap.getEmberCapacity() && blockEntity.capability.getEmber() > 0.0) {
                double added = cap.addAmount(Math.min(100.0, blockEntity.capability.getEmber()), true);
                blockEntity.capability.removeAmount(added, true);
            }
        }
    }

    @Override
    public boolean onReceive(EmberPacketEntity packet) {
        if (this.f_58857_ instanceof ServerLevel serverLevel) {
            Direction facing = (Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING);
            double offX = 0.5 + (double) facing.getStepX() * 0.5;
            double offY = 0.5 + (double) facing.getStepY() * 0.5;
            double offZ = 0.5 + (double) facing.getStepZ() * 0.5;
            if (this.capability.getEmber() + packet.value > this.capability.getEmberCapacity()) {
                serverLevel.sendParticles(new SparkParticleOptions(GlowParticleOptions.EMBER_COLOR, this.random.nextFloat() * 0.75F + 0.45F), (double) this.m_58899_().m_123341_() + offX, (double) this.m_58899_().m_123342_() + offY, (double) this.m_58899_().m_123343_() + offZ, 5, (double) (0.125F * (this.random.nextFloat() - 0.5F)), (double) (0.125F * this.random.nextFloat()), (double) (0.125F * (this.random.nextFloat() - 0.5F)), 1.0);
                serverLevel.sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 2.0F + this.random.nextFloat() * 2.0F), (double) this.m_58899_().m_123341_() + offX, (double) this.m_58899_().m_123342_() + offY, (double) this.m_58899_().m_123343_() + offZ, 15, (double) (0.0625F * (this.random.nextFloat() - 0.5F)), (double) (0.0625F + 0.0625F * (this.random.nextFloat() - 0.5F)), (double) (0.0625F * (this.random.nextFloat() - 0.5F)), 1.0);
            } else {
                serverLevel.sendParticles(new StarParticleOptions(GlowParticleOptions.EMBER_COLOR, 3.5F + 0.5F * this.random.nextFloat()), (double) this.m_58899_().m_123341_() + offX, (double) this.m_58899_().m_123342_() + offY, (double) this.m_58899_().m_123343_() + offZ, 12, (double) (0.0125F * (this.random.nextFloat() - 0.5F)), (double) (0.0125F * (this.random.nextFloat() - 0.5F)), (double) (0.0125F * (this.random.nextFloat() - 0.5F)), 0.0);
            }
        }
        this.f_58857_.playLocalSound(packet.m_20185_(), packet.m_20186_(), packet.m_20189_(), packet.value >= 100.0 ? EmbersSounds.EMBER_RECEIVE_BIG.get() : EmbersSounds.EMBER_RECEIVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        return true;
    }
}