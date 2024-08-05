package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IEmberInjectable;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.ArrayList;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class EmberInjectorBlockEntity extends BlockEntity implements ISoundController, IExtraDialInformation {

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            EmberInjectorBlockEntity.this.setChanged();
        }
    };

    protected int ticksExisted = 0;

    protected int progress = -1;

    static Random random = new Random();

    public static final double EMBER_COST = 1.0;

    public static final int SOUND_PROCESS = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public List<UpgradeContext> upgrades = new ArrayList();

    public boolean isWorking;

    public int distance;

    public EmberInjectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_INJECTOR_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(24000.0);
        this.capability.setEmber(0.0);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
        this.isWorking = nbt.getBoolean("isWorking");
        this.distance = nbt.getInt("distance");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
        nbt.putBoolean("isWorking", this.isWorking);
        nbt.putInt("distance", this.distance);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("isWorking", this.isWorking);
        nbt.putInt("distance", this.distance);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, EmberInjectorBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Direction.values());
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        blockEntity.handleSound();
        if (blockEntity.isWorking) {
            Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
            for (int i = 0; i < 6 * blockEntity.distance; i++) {
                level.addParticle(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, 4.0F), (double) ((float) pos.m_123341_() + 0.5F + random.nextFloat() * (float) blockEntity.distance * (float) facing.getNormal().getX()), (double) ((float) pos.m_123342_() + 0.5F + random.nextFloat() * (float) blockEntity.distance * (float) facing.getNormal().getY()), (double) ((float) pos.m_123343_() + 0.5F + random.nextFloat() * (float) blockEntity.distance * (float) facing.getNormal().getZ()), 0.0, 0.0, 0.0);
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EmberInjectorBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Direction.values());
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        boolean wasWorking = blockEntity.isWorking;
        int previousDist = blockEntity.distance;
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
            int maxDist = UpgradeUtil.getOtherParameter(blockEntity, "distance", ConfigManager.INJECTOR_MAX_DISTANCE.get().intValue(), blockEntity.upgrades);
            BlockPos hitPos = pos;
            BlockEntity tile = null;
            for (int i = 1; i <= maxDist + 1; i++) {
                hitPos = hitPos.relative(facing);
                BlockState hitState = level.getBlockState(hitPos);
                if (!hitState.m_60812_(level, hitPos).isEmpty()) {
                    tile = level.getBlockEntity(hitPos);
                    blockEntity.distance = i;
                    break;
                }
            }
            blockEntity.isWorking = false;
            double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, 1.0, blockEntity.upgrades);
            if (tile instanceof IEmberInjectable injectable && injectable.isValid() && blockEntity.capability.getEmber() >= emberCost) {
                boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                if (!cancel) {
                    double enberInjected = 1.0 * UpgradeUtil.getTotalSpeedModifier(blockEntity, blockEntity.upgrades);
                    injectable.inject(blockEntity, enberInjected);
                    UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
                    blockEntity.isWorking = true;
                    blockEntity.capability.removeAmount(emberCost, true);
                }
            }
        }
        if (wasWorking != blockEntity.isWorking || previousDist != blockEntity.distance) {
            blockEntity.setChanged();
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
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

    @Override
    public void playSound(int id) {
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.INJECTOR_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
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
        return id == 1 && this.isWorking;
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }
}