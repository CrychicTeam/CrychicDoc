package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.power.IEmberPacketProducer;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.entity.EmberPacketEntity;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.StarParticleOptions;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EmberRelayBlockEntity extends BlockEntity implements IEmberPacketProducer, IEmberPacketReceiver {

    public BlockPos target = null;

    public Random random = new Random();

    public boolean polled = false;

    public EmberRelayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_RELAY_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("targetX")) {
            this.target = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.target != null) {
            nbt.putInt("targetX", this.target.m_123341_());
            nbt.putInt("targetY", this.target.m_123342_());
            nbt.putInt("targetZ", this.target.m_123343_());
        }
    }

    @Override
    public boolean hasRoomFor(double ember) {
        if (this.polled) {
            return this.target != null;
        } else {
            this.polled = true;
            if (this.target != null && this.f_58857_.isLoaded(this.target) && this.f_58857_.getBlockEntity(this.target) instanceof IEmberPacketReceiver targetBE) {
                boolean hasRoom = targetBE.hasRoomFor(ember);
                this.polled = false;
                return hasRoom;
            } else {
                this.polled = false;
                return false;
            }
        }
    }

    @Override
    public boolean onReceive(EmberPacketEntity packet) {
        if (this.target != null && packet.pos != this.m_58899_()) {
            if (this.f_58857_ instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new StarParticleOptions(GlowParticleOptions.EMBER_COLOR, 3.5F + 0.5F * this.random.nextFloat()), (double) this.m_58899_().m_123341_() + 0.5, (double) this.m_58899_().m_123342_() + 0.5, (double) this.m_58899_().m_123343_() + 0.5, 12, (double) (0.0125F * (this.random.nextFloat() - 0.5F)), (double) (0.0125F * (this.random.nextFloat() - 0.5F)), (double) (0.0125F * (this.random.nextFloat() - 0.5F)), 0.0);
            }
            packet.lifetime = 78;
            packet.dest = this.target;
            packet.pos = this.m_58899_();
            packet.m_20256_(packet.m_20184_().scale(1.7));
            this.f_58857_.playLocalSound(packet.m_20185_(), packet.m_20186_(), packet.m_20189_(), EmbersSounds.EMBER_RELAY.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        return false;
    }

    @Override
    public void setTargetPosition(BlockPos pos, Direction side) {
        if (pos != this.f_58858_) {
            this.target = pos;
            this.m_6596_();
        }
    }

    @Override
    public Direction getEmittingDirection(Direction side) {
        return side;
    }
}