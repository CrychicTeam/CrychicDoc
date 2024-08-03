package com.simibubi.create.content.fluids;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.pipes.VanillaFluidTargets;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class OpenEndedPipe extends FlowSource {

    private static final List<OpenEndedPipe.IEffectHandler> EFFECT_HANDLERS = new ArrayList();

    private Level world;

    private BlockPos pos;

    private AABB aoe;

    private OpenEndedPipe.OpenEndFluidHandler fluidHandler = new OpenEndedPipe.OpenEndFluidHandler();

    private BlockPos outputPos;

    private boolean wasPulling;

    private FluidStack cachedFluid;

    private List<MobEffectInstance> cachedEffects;

    public OpenEndedPipe(BlockFace face) {
        super(face);
        this.outputPos = face.getConnectedPos();
        this.pos = face.getPos();
        this.aoe = new AABB(this.outputPos).expandTowards(0.0, -1.0, 0.0);
        if (face.getFace() == Direction.DOWN) {
            this.aoe = this.aoe.expandTowards(0.0, -1.0, 0.0);
        }
    }

    public static void registerEffectHandler(OpenEndedPipe.IEffectHandler handler) {
        EFFECT_HANDLERS.add(handler);
    }

    public Level getWorld() {
        return this.world;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockPos getOutputPos() {
        return this.outputPos;
    }

    public AABB getAOE() {
        return this.aoe;
    }

    @Override
    public void manageSource(Level world) {
        this.world = world;
    }

    @Override
    public LazyOptional<IFluidHandler> provideHandler() {
        return LazyOptional.of(() -> this.fluidHandler);
    }

    @Override
    public boolean isEndpoint() {
        return true;
    }

    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        this.fluidHandler.writeToNBT(compound);
        compound.putBoolean("Pulling", this.wasPulling);
        compound.put("Location", this.location.serializeNBT());
        return compound;
    }

    public static OpenEndedPipe fromNBT(CompoundTag compound, BlockPos blockEntityPos) {
        BlockFace fromNBT = BlockFace.fromNBT(compound.getCompound("Location"));
        OpenEndedPipe oep = new OpenEndedPipe(new BlockFace(blockEntityPos, fromNBT.getFace()));
        oep.fluidHandler.readFromNBT(compound);
        oep.wasPulling = compound.getBoolean("Pulling");
        return oep;
    }

    private FluidStack removeFluidFromSpace(boolean simulate) {
        FluidStack empty = FluidStack.EMPTY;
        if (this.world == null) {
            return empty;
        } else if (!this.world.isLoaded(this.outputPos)) {
            return empty;
        } else {
            BlockState state = this.world.getBlockState(this.outputPos);
            FluidState fluidState = state.m_60819_();
            boolean waterlog = state.m_61138_(BlockStateProperties.WATERLOGGED);
            FluidStack drainBlock = VanillaFluidTargets.drainBlock(this.world, this.outputPos, state, simulate);
            if (!drainBlock.isEmpty()) {
                if (!simulate && state.m_61138_(BlockStateProperties.LEVEL_HONEY) && AllFluids.HONEY.is(drainBlock.getFluid())) {
                    AdvancementBehaviour.tryAward(this.world, this.pos, AllAdvancements.HONEY_DRAIN);
                }
                return drainBlock;
            } else if (!waterlog && !state.m_247087_()) {
                return empty;
            } else if (!fluidState.isEmpty() && fluidState.isSource()) {
                FluidStack stack = new FluidStack(fluidState.getType(), 1000);
                if (simulate) {
                    return stack;
                } else {
                    if (FluidHelper.isWater(stack.getFluid())) {
                        AdvancementBehaviour.tryAward(this.world, this.pos, AllAdvancements.WATER_SUPPLY);
                    }
                    if (waterlog) {
                        this.world.setBlock(this.outputPos, (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, false), 3);
                        this.world.m_186469_(this.outputPos, Fluids.WATER, 1);
                        return stack;
                    } else {
                        this.world.setBlock(this.outputPos, (BlockState) fluidState.createLegacyBlock().m_61124_(LiquidBlock.LEVEL, 14), 3);
                        return stack;
                    }
                }
            } else {
                return empty;
            }
        }
    }

    private boolean provideFluidToSpace(FluidStack fluid, boolean simulate) {
        if (this.world == null) {
            return false;
        } else if (!this.world.isLoaded(this.outputPos)) {
            return false;
        } else {
            BlockState state = this.world.getBlockState(this.outputPos);
            FluidState fluidState = state.m_60819_();
            boolean waterlog = state.m_61138_(BlockStateProperties.WATERLOGGED);
            if (!waterlog && !state.m_247087_()) {
                return false;
            } else if (fluid.isEmpty()) {
                return false;
            } else if (!FluidHelper.hasBlockState(fluid.getFluid())) {
                return true;
            } else if (!fluidState.isEmpty() && fluidState.getType() != fluid.getFluid()) {
                FluidReactions.handlePipeSpillCollision(this.world, this.outputPos, fluid.getFluid(), fluidState);
                return false;
            } else if (fluidState.isSource()) {
                return false;
            } else if (waterlog && fluid.getFluid() != Fluids.WATER) {
                return false;
            } else if (simulate) {
                return true;
            } else if (this.world.dimensionType().ultraWarm() && FluidHelper.isTag(fluid, FluidTags.WATER)) {
                int i = this.outputPos.m_123341_();
                int j = this.outputPos.m_123342_();
                int k = this.outputPos.m_123343_();
                this.world.playSound(null, (double) i, (double) j, (double) k, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.8F);
                return true;
            } else if (waterlog) {
                this.world.setBlock(this.outputPos, (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, true), 3);
                this.world.m_186469_(this.outputPos, Fluids.WATER, 1);
                return true;
            } else if (!AllConfigs.server().fluids.pipesPlaceFluidSourceBlocks.get()) {
                return true;
            } else {
                this.world.setBlock(this.outputPos, fluid.getFluid().defaultFluidState().createLegacyBlock(), 3);
                return true;
            }
        }
    }

    private boolean canApplyEffects(FluidStack fluid) {
        for (OpenEndedPipe.IEffectHandler handler : EFFECT_HANDLERS) {
            if (handler.canApplyEffects(this, fluid)) {
                return true;
            }
        }
        return false;
    }

    private void applyEffects(FluidStack fluid) {
        for (OpenEndedPipe.IEffectHandler handler : EFFECT_HANDLERS) {
            if (handler.canApplyEffects(this, fluid)) {
                handler.applyEffects(this, fluid);
            }
        }
    }

    static {
        registerEffectHandler(new OpenEndedPipe.PotionEffectHandler());
        registerEffectHandler(new OpenEndedPipe.MilkEffectHandler());
        registerEffectHandler(new OpenEndedPipe.WaterEffectHandler());
        registerEffectHandler(new OpenEndedPipe.LavaEffectHandler());
        registerEffectHandler(new OpenEndedPipe.TeaEffectHandler());
    }

    public interface IEffectHandler {

        boolean canApplyEffects(OpenEndedPipe var1, FluidStack var2);

        void applyEffects(OpenEndedPipe var1, FluidStack var2);
    }

    public static class LavaEffectHandler implements OpenEndedPipe.IEffectHandler {

        @Override
        public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            return FluidHelper.isTag(fluid, FluidTags.LAVA);
        }

        @Override
        public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            Level world = pipe.getWorld();
            if (world.getGameTime() % 5L == 0L) {
                for (Entity entity : world.getEntities((Entity) null, pipe.getAOE(), entityx -> !entityx.fireImmune())) {
                    entity.setSecondsOnFire(3);
                }
            }
        }
    }

    public static class MilkEffectHandler implements OpenEndedPipe.IEffectHandler {

        @Override
        public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            return FluidHelper.isTag(fluid, Tags.Fluids.MILK);
        }

        @Override
        public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            Level world = pipe.getWorld();
            if (world.getGameTime() % 5L == 0L) {
                List<LivingEntity> entities = world.m_6443_(LivingEntity.class, pipe.getAOE(), LivingEntity::m_5801_);
                ItemStack curativeItem = new ItemStack(Items.MILK_BUCKET);
                for (LivingEntity entity : entities) {
                    entity.curePotionEffects(curativeItem);
                }
            }
        }
    }

    private class OpenEndFluidHandler extends FluidTank {

        public OpenEndFluidHandler() {
            super(1000);
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (OpenEndedPipe.this.world == null) {
                return 0;
            } else if (!OpenEndedPipe.this.world.isLoaded(OpenEndedPipe.this.outputPos)) {
                return 0;
            } else if (resource.isEmpty()) {
                return 0;
            } else if (!OpenEndedPipe.this.provideFluidToSpace(resource, true)) {
                return 0;
            } else {
                FluidStack containedFluidStack = this.getFluid();
                boolean hasBlockState = FluidHelper.hasBlockState(containedFluidStack.getFluid());
                if (!containedFluidStack.isEmpty() && !containedFluidStack.isFluidEqual(resource)) {
                    this.setFluid(FluidStack.EMPTY);
                }
                if (OpenEndedPipe.this.wasPulling) {
                    OpenEndedPipe.this.wasPulling = false;
                }
                if (OpenEndedPipe.this.canApplyEffects(resource) && !hasBlockState) {
                    resource = FluidHelper.copyStackWithAmount(resource, 1);
                }
                int fill = super.fill(resource, action);
                if (action.simulate()) {
                    return fill;
                } else {
                    if (!resource.isEmpty()) {
                        OpenEndedPipe.this.applyEffects(resource);
                    }
                    if ((this.getFluidAmount() == 1000 || !hasBlockState) && OpenEndedPipe.this.provideFluidToSpace(containedFluidStack, false)) {
                        this.setFluid(FluidStack.EMPTY);
                    }
                    return fill;
                }
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            return this.drainInner(resource.getAmount(), resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            return this.drainInner(maxDrain, null, action);
        }

        private FluidStack drainInner(int amount, @Nullable FluidStack filter, IFluidHandler.FluidAction action) {
            FluidStack empty = FluidStack.EMPTY;
            boolean filterPresent = filter != null;
            if (OpenEndedPipe.this.world == null) {
                return empty;
            } else if (!OpenEndedPipe.this.world.isLoaded(OpenEndedPipe.this.outputPos)) {
                return empty;
            } else if (amount == 0) {
                return empty;
            } else {
                if (amount > 1000) {
                    amount = 1000;
                    if (filterPresent) {
                        filter = FluidHelper.copyStackWithAmount(filter, amount);
                    }
                }
                if (!OpenEndedPipe.this.wasPulling) {
                    OpenEndedPipe.this.wasPulling = true;
                }
                FluidStack drainedFromInternal = filterPresent ? super.drain(filter, action) : super.drain(amount, action);
                if (!drainedFromInternal.isEmpty()) {
                    return drainedFromInternal;
                } else {
                    FluidStack drainedFromWorld = OpenEndedPipe.this.removeFluidFromSpace(action.simulate());
                    if (drainedFromWorld.isEmpty()) {
                        return FluidStack.EMPTY;
                    } else if (filterPresent && !drainedFromWorld.isFluidEqual(filter)) {
                        return FluidStack.EMPTY;
                    } else {
                        int remainder = drainedFromWorld.getAmount() - amount;
                        drainedFromWorld.setAmount(amount);
                        if (!action.simulate() && remainder > 0) {
                            if (!this.getFluid().isEmpty() && !this.getFluid().isFluidEqual(drainedFromWorld)) {
                                this.setFluid(FluidStack.EMPTY);
                            }
                            super.fill(FluidHelper.copyStackWithAmount(drainedFromWorld, remainder), IFluidHandler.FluidAction.EXECUTE);
                        }
                        return drainedFromWorld;
                    }
                }
            }
        }
    }

    public static class PotionEffectHandler implements OpenEndedPipe.IEffectHandler {

        @Override
        public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            return fluid.getFluid().isSame((Fluid) AllFluids.POTION.get());
        }

        @Override
        public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            if (pipe.cachedFluid == null || pipe.cachedEffects == null || !fluid.isFluidEqual(pipe.cachedFluid)) {
                FluidStack copy = fluid.copy();
                copy.setAmount(250);
                ItemStack bottle = PotionFluidHandler.fillBottle(new ItemStack(Items.GLASS_BOTTLE), fluid);
                pipe.cachedEffects = PotionUtils.getMobEffects(bottle);
            }
            if (!pipe.cachedEffects.isEmpty()) {
                for (LivingEntity entity : pipe.getWorld().m_6443_(LivingEntity.class, pipe.getAOE(), LivingEntity::m_5801_)) {
                    for (MobEffectInstance effectInstance : pipe.cachedEffects) {
                        MobEffect effect = effectInstance.getEffect();
                        if (effect.isInstantenous()) {
                            effect.applyInstantenousEffect(null, null, entity, effectInstance.getAmplifier(), 0.5);
                        } else {
                            entity.addEffect(new MobEffectInstance(effectInstance));
                        }
                    }
                }
            }
        }
    }

    public static class TeaEffectHandler implements OpenEndedPipe.IEffectHandler {

        @Override
        public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            return fluid.getFluid().isSame((Fluid) AllFluids.TEA.get());
        }

        @Override
        public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            Level world = pipe.getWorld();
            if (world.getGameTime() % 5L == 0L) {
                for (LivingEntity entity : world.m_6443_(LivingEntity.class, pipe.getAOE(), LivingEntity::m_5801_)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 21, 0, false, false, false));
                }
            }
        }
    }

    public static class WaterEffectHandler implements OpenEndedPipe.IEffectHandler {

        @Override
        public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            return FluidHelper.isTag(fluid, FluidTags.WATER);
        }

        @Override
        public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
            Level world = pipe.getWorld();
            if (world.getGameTime() % 5L == 0L) {
                for (Entity entity : world.getEntities((Entity) null, pipe.getAOE(), Entity::m_6060_)) {
                    entity.clearFire();
                }
                BlockPos.betweenClosedStream(pipe.getAOE()).forEach(pos -> dowseFire(world, pos));
            }
        }

        private static void dowseFire(Level level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            if (state.m_204336_(BlockTags.FIRE)) {
                level.removeBlock(pos, false);
            } else if (AbstractCandleBlock.isLit(state)) {
                AbstractCandleBlock.extinguish(null, state, level, pos);
            } else if (CampfireBlock.isLitCampfire(state)) {
                level.m_5898_(null, 1009, pos, 0);
                CampfireBlock.dowse(null, level, pos, state);
                level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(CampfireBlock.LIT, false));
            }
        }
    }
}