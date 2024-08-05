package com.rekindled.embers.blockentity;

import com.google.common.collect.Lists;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IBin;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.SmokeParticleOptions;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CinderPlinthBlockEntity extends BlockEntity implements ISoundController, IExtraDialInformation, IExtraCapabilityInformation {

    public static double EMBER_COST = 0.5;

    public static int PROCESS_TIME = 40;

    int progress = 0;

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            CinderPlinthBlockEntity.this.setChanged();
        }
    };

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            CinderPlinthBlockEntity.this.setChanged();
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    static Random random = new Random();

    protected List<UpgradeContext> upgrades = new ArrayList();

    public static final int SOUND_PROCESS = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public CinderPlinthBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.CINDER_PLINTH_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(4000.0);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(0, 0, 0), this.f_58858_.offset(1, 2, 1));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
        if (nbt.contains("progress")) {
            this.progress = nbt.getInt("progress");
        }
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
        nbt.putInt("progress", this.progress);
        nbt.put("inventory", this.inventory.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.capability.writeToNBT(nbt);
        nbt.put("inventory", this.inventory.serializeNBT());
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CinderPlinthBlockEntity blockEntity) {
        blockEntity.handleSound();
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Direction.values());
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CinderPlinthBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Direction.values());
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            if (blockEntity.shouldWork()) {
                boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                if (!cancel) {
                    blockEntity.progress++;
                    ((ServerLevel) level).sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 3.0F + random.nextFloat() * 0.4F), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.875F), (double) ((float) pos.m_123343_() + 0.5F), 1, 0.0125, 0.025, 0.0125, 1.0);
                    double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, EMBER_COST, blockEntity.upgrades);
                    UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
                    blockEntity.capability.removeAmount(emberCost, true);
                    if (blockEntity.progress > UpgradeUtil.getWorkTime(blockEntity, PROCESS_TIME, blockEntity.upgrades)) {
                        blockEntity.progress = 0;
                        BlockEntity tile = level.getBlockEntity(pos.below());
                        List<ItemStack> outputs = Lists.newArrayList(new ItemStack[] { new ItemStack(RegistryManager.ASH.get(), 1) });
                        UpgradeUtil.transformOutput(blockEntity, outputs, blockEntity.upgrades);
                        blockEntity.inventory.extractItem(0, 1, false);
                        for (ItemStack remainder : outputs) {
                            if (tile instanceof IBin) {
                                remainder = ((IBin) tile).getInventory().insertItem(0, remainder, false);
                            }
                            if (!remainder.isEmpty()) {
                                level.m_7967_(new ItemEntity(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 1.0, (double) pos.m_123343_() + 0.5, remainder));
                            }
                        }
                        ((ServerLevel) level).sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 3.0F), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.1F), (double) ((float) pos.m_123343_() + 0.5F), 9, 0.0125, 0.025, 0.0125, 1.0);
                    }
                }
            } else if (blockEntity.progress != 0) {
                blockEntity.progress = 0;
                blockEntity.setChanged();
            }
        }
    }

    private boolean shouldWork() {
        return !this.inventory.getStackInSlot(0).isEmpty() && this.capability.getEmber() > 0.0;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_) {
            if (cap == EmbersCapabilities.EMBER_CAPABILITY) {
                return this.capability.getCapability(cap, side);
            }
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return this.holder.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
        this.holder.invalidate();
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
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.PLINTH_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
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
        return id == 1 && this.shouldWork();
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", null));
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.ash")));
        }
    }
}