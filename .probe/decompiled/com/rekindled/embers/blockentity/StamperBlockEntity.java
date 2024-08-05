package com.rekindled.embers.blockentity;

import com.google.common.collect.Lists;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IBin;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.tile.IMechanicallyPowered;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.recipe.IStampingRecipe;
import com.rekindled.embers.recipe.StampingContext;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StamperBlockEntity extends BlockEntity implements IMechanicallyPowered, IExtraDialInformation, IExtraCapabilityInformation {

    public static final double EMBER_COST = 80.0;

    public static final int STAMP_TIME = 70;

    public static final int RETRACT_TIME = 10;

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            StamperBlockEntity.this.setChanged();
        }
    };

    public boolean prevPowered = false;

    public boolean powered = false;

    public long ticksExisted = 0L;

    Random random = new Random();

    public ItemStackHandler stamp = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            StamperBlockEntity.this.setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.stamp);

    protected List<UpgradeContext> upgrades = new ArrayList();

    public IStampingRecipe cachedRecipe = null;

    public StamperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.STAMPER_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(8000.0);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(0, -1, 0), this.f_58858_.offset(1, 1, 1));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
        this.powered = nbt.getBoolean("powered");
        this.stamp.deserializeNBT(nbt.getCompound("stamp"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
        nbt.putBoolean("powered", this.powered);
        nbt.put("stamp", this.stamp.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("powered", this.powered);
        nbt.put("stamp", this.stamp.serializeNBT());
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, StamperBlockEntity blockEntity) {
        blockEntity.prevPowered = blockEntity.powered;
        if (level.getBlockState(pos.below(2)).m_60734_() == RegistryManager.STAMP_BASE.get()) {
            blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
            UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StamperBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        blockEntity.prevPowered = blockEntity.powered;
        if (level.getBlockState(pos.below(2)).m_60734_() == RegistryManager.STAMP_BASE.get()) {
            blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
            UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
            if (UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
                return;
            }
            StampBaseBlockEntity stamp = (StampBaseBlockEntity) level.getBlockEntity(pos.below(2));
            IFluidHandler handler = stamp.getTank();
            StampingContext context = new StampingContext(stamp.inventory, handler, blockEntity.stamp.getStackInSlot(0));
            blockEntity.cachedRecipe = Misc.getRecipe(blockEntity.cachedRecipe, RegistryManager.STAMPING.get(), context, level);
            if (blockEntity.cachedRecipe != null || blockEntity.powered) {
                boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                int stampTime = UpgradeUtil.getWorkTime(blockEntity, 70, blockEntity.upgrades);
                int retractTime = UpgradeUtil.getWorkTime(blockEntity, 10, blockEntity.upgrades);
                if (!cancel && !blockEntity.powered && blockEntity.ticksExisted >= (long) stampTime) {
                    double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, 80.0, blockEntity.upgrades);
                    if (blockEntity.capability.getEmber() >= emberCost) {
                        List<ItemStack> results = Lists.newArrayList(new ItemStack[] { blockEntity.cachedRecipe.getOutput(context).copy() });
                        UpgradeUtil.transformOutput(blockEntity, results, blockEntity.upgrades);
                        BlockEntity outputTile = level.getBlockEntity(pos.below(3));
                        if (outputTile instanceof IBin) {
                            for (ItemStack remainder : results) {
                                remainder = ((IBin) outputTile).getInventory().insertItem(0, remainder, true);
                                if (!remainder.isEmpty()) {
                                    return;
                                }
                            }
                        }
                        UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
                        blockEntity.capability.removeAmount(emberCost, true);
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(new SparkParticleOptions(GlowParticleOptions.EMBER_COLOR, 1.0F), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() - 1.1F), (double) ((float) pos.m_123343_() + 0.5F), 10, 0.25, 0.0, 0.25, 1.0);
                            serverLevel.sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 3.0F), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() - 1.1F), (double) ((float) pos.m_123343_() + 0.5F), 10, 0.25, 0.0, 0.25, 1.0);
                        }
                        level.playSound(null, pos.below(), EmbersSounds.STAMPER_DOWN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        blockEntity.powered = true;
                        blockEntity.ticksExisted = 0L;
                        UpgradeUtil.throwEvent(blockEntity, new MachineRecipeEvent.Success<>(blockEntity, blockEntity.cachedRecipe), blockEntity.upgrades);
                        blockEntity.cachedRecipe.m_5874_(context, level.registryAccess());
                        BlockPos middlePos = pos.below();
                        for (ItemStack remainderx : results) {
                            if (outputTile instanceof IBin) {
                                ((IBin) outputTile).getInventory().insertItem(0, remainderx, false);
                            } else {
                                level.m_7967_(new ItemEntity(level, (double) middlePos.m_123341_() + 0.5, (double) middlePos.m_123342_() + 0.5, (double) middlePos.m_123343_() + 0.5, remainderx));
                            }
                        }
                        stamp.m_6596_();
                    }
                    blockEntity.setChanged();
                } else if (!cancel && blockEntity.powered && blockEntity.ticksExisted >= (long) retractTime) {
                    blockEntity.retract();
                }
            }
        } else if (blockEntity.powered) {
            blockEntity.retract();
        }
    }

    private void retract() {
        this.f_58857_.playSound(null, this.f_58858_.below(), EmbersSounds.STAMPER_UP.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        this.powered = false;
        this.ticksExisted = 0L;
        this.setChanged();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY) {
            return this.capability.getCapability(cap, side);
        } else {
            return !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER ? this.holder.cast() : super.getCapability(cap, side);
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

    @Override
    public double getMechanicalSpeed(double power) {
        return Misc.getDiminishedPower(power, 20.0, 0.075);
    }

    @Override
    public double getNominalSpeed() {
        return 1.0;
    }

    @Override
    public double getMinimumPower() {
        return 10.0;
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
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.BOTH, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.stamp")));
        }
    }
}