package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class EmberActivatorTopBlockEntity extends BlockEntity implements ISoundController, IExtraCapabilityInformation {

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            EmberActivatorTopBlockEntity.this.setChanged();
        }
    };

    static Random random = new Random();

    public static final int SOUND_HAS_EMBER = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public EmberActivatorTopBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_ACTIVATOR_TOP_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(16000.0);
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

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.capability.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, EmberActivatorTopBlockEntity blockEntity) {
        blockEntity.handleSound();
        if (blockEntity.capability.getEmber() > 0.0) {
            for (int i = 0; (double) i < Math.ceil(blockEntity.capability.getEmber() / 500.0); i++) {
                level.addParticle(GlowParticleOptions.EMBER, (double) ((float) pos.m_123341_() + 0.25F + random.nextFloat() * 0.5F), (double) ((float) pos.m_123342_() + 0.25F + random.nextFloat() * 0.5F), (double) ((float) pos.m_123343_() + 0.25F + random.nextFloat() * 0.5F), (Math.random() * 2.0 - 1.0) * 0.2, (Math.random() * 2.0 - 1.0) * 0.2, (Math.random() * 2.0 - 1.0) * 0.2);
            }
        }
    }

    @Override
    public void playSound(int id) {
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.GENERATOR_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
            default:
                this.soundsPlaying.add(id);
        }
    }

    @Override
    public void stopSound(int id) {
        this.soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return this.soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        return id == 1 && this.capability.getEmber() > 0.0;
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        return (float) ((this.capability.getEmber() + 5000.0) / (this.capability.getEmberCapacity() + 5000.0));
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return true;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == EmbersCapabilities.EMBER_CAPABILITY) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.ember", null));
        }
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }
}