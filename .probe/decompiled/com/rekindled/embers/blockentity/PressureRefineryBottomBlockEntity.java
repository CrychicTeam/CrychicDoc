package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersBlockTags;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.recipe.BlockStateContext;
import com.rekindled.embers.recipe.IEmberActivationRecipe;
import com.rekindled.embers.recipe.IMetalCoefficientRecipe;
import com.rekindled.embers.recipe.SingleItemContainer;
import com.rekindled.embers.util.DecimalFormats;
import com.rekindled.embers.util.Misc;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class PressureRefineryBottomBlockEntity extends FluidHandlerBlockEntity implements IExtraDialInformation, IExtraCapabilityInformation {

    public static final float BASE_MULTIPLIER = 1.25F;

    public static final int FLUID_CONSUMED = 25;

    public static final float PER_BLOCK_MULTIPLIER = 0.25F;

    public static final int PROCESS_TIME = 20;

    public static int capacity = 8000;

    int progress = -1;

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            PressureRefineryBottomBlockEntity.this.m_6596_();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return Misc.getRecipe(PressureRefineryBottomBlockEntity.this.cachedRecipe, RegistryManager.EMBER_ACTIVATION.get(), new SingleItemContainer(stack), PressureRefineryBottomBlockEntity.this.f_58857_) != null ? super.insertItem(slot, stack, simulate) : stack;
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    protected List<UpgradeContext> upgrades = new ArrayList();

    public IEmberActivationRecipe cachedRecipe = null;

    public IMetalCoefficientRecipe cachedCoefficient = null;

    public PressureRefineryBottomBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.PRESSURE_REFINERY_BOTTOM_ENTITY.get(), pPos, pBlockState);
        this.tank = new FluidTank(capacity) {

            @Override
            protected void onContentsChanged() {
                PressureRefineryBottomBlockEntity.this.m_6596_();
            }
        };
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("progress");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", this.inventory.serializeNBT());
        nbt.putInt("progress", this.progress);
    }

    public double getMultiplier() {
        BlockState metalState = this.f_58857_.getBlockState(this.f_58858_.below());
        BlockStateContext context = new BlockStateContext(metalState);
        this.cachedCoefficient = Misc.getRecipe(this.cachedCoefficient, RegistryManager.METAL_COEFFICIENT.get(), context, this.f_58857_);
        double metalMultiplier = this.cachedCoefficient == null ? 1.25 : this.cachedCoefficient.getCoefficient(context) - 1.25;
        double totalMult = 1.25;
        if (this.cachedCoefficient == null) {
            totalMult = metalMultiplier;
        } else {
            for (Direction facing : Misc.horizontals) {
                if (this.f_58857_.getBlockState(this.f_58858_.below().relative(facing)).m_204336_(EmbersBlockTags.HEAT_SOURCES)) {
                    totalMult += 0.25 * metalMultiplier;
                }
            }
        }
        return totalMult;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, PressureRefineryBottomBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PressureRefineryBottomBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            if (!blockEntity.inventory.getStackInSlot(0).isEmpty()) {
                BlockEntity tile = level.getBlockEntity(pos.above());
                if (blockEntity.tank.getFluid() != null && blockEntity.tank.getFluid().getFluid().is(FluidTags.WATER) && blockEntity.tank.getFluidAmount() >= 25) {
                    boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                    if (!cancel && tile instanceof PressureRefineryTopBlockEntity top) {
                        blockEntity.progress++;
                        if (blockEntity.progress > UpgradeUtil.getWorkTime(blockEntity, 20, blockEntity.upgrades)) {
                            blockEntity.progress = 0;
                            if (blockEntity.inventory != null) {
                                RecipeWrapper wrapper = new RecipeWrapper(blockEntity.inventory);
                                blockEntity.cachedRecipe = Misc.getRecipe(blockEntity.cachedRecipe, RegistryManager.EMBER_ACTIVATION.get(), wrapper, level);
                                if (blockEntity.cachedRecipe != null) {
                                    double emberValue = (double) blockEntity.cachedRecipe.getOutput(wrapper) * blockEntity.getMultiplier();
                                    double ember = UpgradeUtil.getTotalEmberProduction(blockEntity, emberValue, blockEntity.upgrades);
                                    if ((ember > 0.0 || emberValue == 0.0) && top.capability.getEmber() + ember <= top.capability.getEmberCapacity()) {
                                        level.playSound(null, pos, EmbersSounds.PRESSURE_REFINERY.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                        if (level instanceof ServerLevel serverLevel) {
                                            serverLevel.sendParticles(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, new Vec3(0.0, 0.65F, 0.0), 4.7F), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.5F), (double) ((float) pos.m_123343_() + 0.5F), 80, 0.1, 0.1, 0.1, 1.0);
                                            serverLevel.sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 5.0F), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 1.5, (double) pos.m_123343_() + 0.5, 20, 0.1, 0.1, 0.1, 1.0);
                                        }
                                        UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.PRODUCE, ember), blockEntity.upgrades);
                                        top.capability.addAmount(ember, true);
                                        blockEntity.cachedRecipe.process(wrapper);
                                        blockEntity.tank.drain(25, IFluidHandler.FluidAction.EXECUTE);
                                    }
                                }
                            }
                        }
                        blockEntity.m_6596_();
                    }
                }
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && side != Direction.DOWN && cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        if ("ember".equals(dialType)) {
            DecimalFormat multiplierFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.ember_multiplier");
            double multiplier = this.getMultiplier();
            information.add(Component.translatable("embers.tooltip.dial.ember_multiplier", multiplierFormat.format(multiplier)));
        }
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.ember")));
        }
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.water")));
        }
    }
}