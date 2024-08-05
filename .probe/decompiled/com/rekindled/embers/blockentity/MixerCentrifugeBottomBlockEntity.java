package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.tile.IMechanicallyPowered;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.block.FluidDialBlock;
import com.rekindled.embers.datagen.EmbersFluidTags;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.recipe.IMixingRecipe;
import com.rekindled.embers.recipe.MixingContext;
import com.rekindled.embers.util.FluidAmounts;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.HashSet;
import java.util.List;
import net.minecraft.ChatFormatting;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MixerCentrifugeBottomBlockEntity extends BlockEntity implements IMechanicallyPowered, ISoundController, IExtraDialInformation, IExtraCapabilityInformation {

    public static final double EMBER_COST = 2.0;

    public MixerCentrifugeBottomBlockEntity.MixerFluidTank north = new MixerCentrifugeBottomBlockEntity.MixerFluidTank(8000, this);

    public MixerCentrifugeBottomBlockEntity.MixerFluidTank south = new MixerCentrifugeBottomBlockEntity.MixerFluidTank(8000, this);

    public MixerCentrifugeBottomBlockEntity.MixerFluidTank east = new MixerCentrifugeBottomBlockEntity.MixerFluidTank(8000, this);

    public MixerCentrifugeBottomBlockEntity.MixerFluidTank west = new MixerCentrifugeBottomBlockEntity.MixerFluidTank(8000, this);

    public MixerCentrifugeBottomBlockEntity.MixerFluidTank[] tanks = new MixerCentrifugeBottomBlockEntity.MixerFluidTank[] { this.north, this.south, this.east, this.west };

    private final LazyOptional<IFluidHandler> holderNorth = LazyOptional.of(() -> this.north);

    private final LazyOptional<IFluidHandler> holderSouth = LazyOptional.of(() -> this.south);

    private final LazyOptional<IFluidHandler> holderEast = LazyOptional.of(() -> this.east);

    private final LazyOptional<IFluidHandler> holderWest = LazyOptional.of(() -> this.west);

    public boolean loaded = false;

    boolean isWorking;

    public static final int SOUND_PROCESS = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    protected List<UpgradeContext> upgrades;

    private double powerRatio;

    public IMixingRecipe cachedRecipe = null;

    public MixerCentrifugeBottomBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MIXER_CENTRIFUGE_BOTTOM_ENTITY.get(), pPos, pBlockState);
    }

    public MixerCentrifugeBottomBlockEntity.MixerFluidTank[] getTanks() {
        return this.tanks;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.north.readFromNBT(nbt.getCompound("northTank"));
        this.south.readFromNBT(nbt.getCompound("southTank"));
        this.east.readFromNBT(nbt.getCompound("eastTank"));
        this.west.readFromNBT(nbt.getCompound("westTank"));
        this.isWorking = nbt.getBoolean("working");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("northTank", this.north.writeToNBT(new CompoundTag()));
        nbt.put("southTank", this.south.writeToNBT(new CompoundTag()));
        nbt.put("eastTank", this.east.writeToNBT(new CompoundTag()));
        nbt.put("westTank", this.west.writeToNBT(new CompoundTag()));
        nbt.putBoolean("working", this.isWorking);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put("northTank", this.north.writeToNBT(new CompoundTag()));
        nbt.put("southTank", this.south.writeToNBT(new CompoundTag()));
        nbt.put("eastTank", this.east.writeToNBT(new CompoundTag()));
        nbt.put("westTank", this.west.writeToNBT(new CompoundTag()));
        nbt.putBoolean("working", this.isWorking);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MixerCentrifugeBottomBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos.above(), Direction.values());
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        blockEntity.handleSound();
        if (!blockEntity.loaded) {
            for (MixerCentrifugeBottomBlockEntity.MixerFluidTank tank : blockEntity.tanks) {
                tank.previousFluid = tank.getFluidAmount();
            }
            blockEntity.loaded = true;
        }
        for (MixerCentrifugeBottomBlockEntity.MixerFluidTank tank : blockEntity.tanks) {
            if (tank.getFluidAmount() != tank.previousFluid) {
                tank.renderOffset = tank.renderOffset + (float) tank.getFluidAmount() - (float) tank.previousFluid;
                tank.previousFluid = tank.getFluidAmount();
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MixerCentrifugeBottomBlockEntity blockEntity) {
        MixerCentrifugeTopBlockEntity top = (MixerCentrifugeTopBlockEntity) level.getBlockEntity(pos.above());
        boolean wasWorking = blockEntity.isWorking;
        blockEntity.isWorking = false;
        if (top != null) {
            blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos.above(), Direction.values());
            UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
            if (UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
                return;
            }
            MixingContext context = new MixingContext(blockEntity.tanks);
            blockEntity.cachedRecipe = Misc.getRecipe(blockEntity.cachedRecipe, RegistryManager.MIXING.get(), context, level);
            blockEntity.powerRatio = 0.0;
            double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, 2.0, blockEntity.upgrades);
            if (top.capability.getEmber() >= emberCost && blockEntity.cachedRecipe != null) {
                boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                if (!cancel) {
                    IFluidHandler tank = (IFluidHandler) top.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
                    FluidStack output = blockEntity.cachedRecipe.getOutput(context);
                    output = UpgradeUtil.transformOutput(blockEntity, output, blockEntity.upgrades);
                    int amount = tank.fill(output, IFluidHandler.FluidAction.SIMULATE);
                    if (amount != 0) {
                        UpgradeUtil.throwEvent(blockEntity, new MachineRecipeEvent.Success<>(blockEntity, blockEntity.cachedRecipe), blockEntity.upgrades);
                        blockEntity.isWorking = true;
                        tank.fill(output, IFluidHandler.FluidAction.EXECUTE);
                        blockEntity.cachedRecipe.process(context);
                        UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
                        top.capability.removeAmount(emberCost, true);
                    }
                }
            }
        }
        if (wasWorking != blockEntity.isWorking) {
            blockEntity.setChanged();
        }
    }

    @Override
    public void playSound(int id) {
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.MIXER_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 1.0F, (float) this.f_58858_.m_123343_() + 0.5F);
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

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side != null) {
            switch(side) {
                case DOWN:
                case UP:
                default:
                    break;
                case EAST:
                    return this.holderEast.cast();
                case NORTH:
                    return this.holderNorth.cast();
                case SOUTH:
                    return this.holderSouth.cast();
                case WEST:
                    return this.holderWest.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holderNorth.invalidate();
        this.holderSouth.invalidate();
        this.holderEast.invalidate();
        this.holderWest.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.FLUID_HANDLER;
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        if ("fluid".equals(dialType)) {
            information.clear();
            information.add(Component.translatable("embers.tooltip.colon", Component.translatable("embers.tooltip.side.north").withStyle(facing == Direction.NORTH ? ChatFormatting.BOLD : ChatFormatting.RESET), FluidDialBlock.formatFluidStack(this.north.getFluid(), this.north.getCapacity())));
            if (this.north.getFluid().getFluid().is(EmbersFluidTags.INGOT_TOOLTIP) && this.north.getFluid().getAmount() >= 10) {
                information.add(FluidAmounts.getIngotTooltip(this.north.getFluid().getAmount()));
            }
            information.add(Component.translatable("embers.tooltip.colon", Component.translatable("embers.tooltip.side.east").withStyle(facing == Direction.EAST ? ChatFormatting.BOLD : ChatFormatting.RESET), FluidDialBlock.formatFluidStack(this.east.getFluid(), this.east.getCapacity())));
            if (this.east.getFluid().getFluid().is(EmbersFluidTags.INGOT_TOOLTIP) && this.east.getFluid().getAmount() >= 10) {
                information.add(FluidAmounts.getIngotTooltip(this.east.getFluid().getAmount()));
            }
            information.add(Component.translatable("embers.tooltip.colon", Component.translatable("embers.tooltip.side.south").withStyle(facing == Direction.SOUTH ? ChatFormatting.BOLD : ChatFormatting.RESET), FluidDialBlock.formatFluidStack(this.south.getFluid(), this.south.getCapacity())));
            if (this.south.getFluid().getFluid().is(EmbersFluidTags.INGOT_TOOLTIP) && this.south.getFluid().getAmount() >= 10) {
                information.add(FluidAmounts.getIngotTooltip(this.south.getFluid().getAmount()));
            }
            information.add(Component.translatable("embers.tooltip.colon", Component.translatable("embers.tooltip.side.west").withStyle(facing == Direction.WEST ? ChatFormatting.BOLD : ChatFormatting.RESET), FluidDialBlock.formatFluidStack(this.west.getFluid(), this.west.getCapacity())));
            if (this.west.getFluid().getFluid().is(EmbersFluidTags.INGOT_TOOLTIP) && this.west.getFluid().getAmount() >= 10) {
                information.add(FluidAmounts.getIngotTooltip(this.west.getFluid().getAmount()));
            }
        }
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.metal")));
    }

    @Override
    public double getMinimumPower() {
        return 20.0;
    }

    @Override
    public double getMechanicalSpeed(double power) {
        return Misc.getDiminishedPower(power, 80.0, 0.01875);
    }

    @Override
    public double getNominalSpeed() {
        return 1.0;
    }

    @Override
    public double getStandardPowerRatio() {
        return this.powerRatio;
    }

    public static class MixerFluidTank extends FluidTank {

        public final BlockEntity entity;

        public float renderOffset;

        public int previousFluid;

        public MixerFluidTank(int capacity, BlockEntity entity) {
            super(capacity);
            this.entity = entity;
        }

        @Override
        public void onContentsChanged() {
            this.entity.setChanged();
        }
    }
}