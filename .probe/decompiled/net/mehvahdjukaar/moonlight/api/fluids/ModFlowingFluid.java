package net.mehvahdjukaar.moonlight.api.fluids;

import java.util.Map;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class ModFlowingFluid extends FlowingFluid {

    @Nullable
    private final Supplier<? extends LiquidBlock> block;

    private final boolean convertsToSource;

    public final boolean hasCustomFluidType;

    protected ModFlowingFluid(ModFlowingFluid.Properties properties, Supplier<? extends LiquidBlock> block) {
        this.block = block;
        this.convertsToSource = properties.canConvertToSource;
        this.hasCustomFluidType = properties.copyFluid == null;
        this.afterInit(properties);
    }

    private void afterInit(ModFlowingFluid.Properties properties) {
    }

    public static ModFlowingFluid.Properties properties() {
        return new ModFlowingFluid.Properties();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return this.convertsToSource;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.m_155947_() ? worldIn.m_7702_(pos) : null;
        Block.dropResources(state, worldIn, pos, blockEntity);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction) {
        return direction == Direction.DOWN && !this.isSame(fluidIn);
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return this.block != null ? (BlockState) ((LiquidBlock) this.block.get()).m_49966_().m_61124_(LiquidBlock.LEVEL, m_76092_(state)) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSame(Fluid fluidIn) {
        return fluidIn == this.getSource() || fluidIn == this.getFlowing();
    }

    @Override
    public abstract Fluid getSource();

    @Override
    public abstract Fluid getFlowing();

    @OnlyIn(Dist.CLIENT)
    public abstract ModFluidRenderProperties createRenderProperties();

    public static final class Properties {

        public String descriptionId;

        public double motionScale = 0.014;

        public boolean canPushEntity = true;

        public boolean canSwim = true;

        public boolean canDrown = true;

        public float fallDistanceModifier = 0.5F;

        public boolean canExtinguish = false;

        public boolean supportsBoating = false;

        public boolean canConvertToSource = false;

        @Nullable
        public BlockPathTypes pathType = BlockPathTypes.WATER;

        @Nullable
        public BlockPathTypes adjacentPathType = BlockPathTypes.WATER_BORDER;

        public boolean canHydrate = false;

        public int lightLevel = 0;

        public int density = 1000;

        public int temperature = 300;

        public int viscosity = 1000;

        public Rarity rarity = Rarity.COMMON;

        public Map<String, SoundEvent> sounds;

        @Deprecated
        public Fluid copyFluid = null;

        @Deprecated(forRemoval = true)
        public ModFlowingFluid.Properties copyFluid(Fluid fluid) {
            return this;
        }

        public ModFlowingFluid.Properties descriptionId(String descriptionId) {
            this.descriptionId = descriptionId;
            return this;
        }

        public ModFlowingFluid.Properties motionScale(double motionScale) {
            this.motionScale = motionScale;
            return this;
        }

        public ModFlowingFluid.Properties setCanConvertToSource(boolean canConvertToSource) {
            this.canConvertToSource = canConvertToSource;
            return this;
        }

        public ModFlowingFluid.Properties canPushEntity(boolean canPushEntity) {
            this.canPushEntity = canPushEntity;
            return this;
        }

        public ModFlowingFluid.Properties canSwim(boolean canSwim) {
            this.canSwim = canSwim;
            return this;
        }

        public ModFlowingFluid.Properties canDrown(boolean canDrown) {
            this.canDrown = canDrown;
            return this;
        }

        public ModFlowingFluid.Properties fallDistanceModifier(float fallDistanceModifier) {
            this.fallDistanceModifier = fallDistanceModifier;
            return this;
        }

        public ModFlowingFluid.Properties canExtinguish(boolean canExtinguish) {
            this.canExtinguish = canExtinguish;
            return this;
        }

        public ModFlowingFluid.Properties supportsBoating(boolean supportsBoating) {
            this.supportsBoating = supportsBoating;
            return this;
        }

        public ModFlowingFluid.Properties pathType(@Nullable BlockPathTypes pathType) {
            this.pathType = pathType;
            return this;
        }

        public ModFlowingFluid.Properties adjacentPathType(@Nullable BlockPathTypes adjacentPathType) {
            this.adjacentPathType = adjacentPathType;
            return this;
        }

        public ModFlowingFluid.Properties sound(String soundActionId, SoundEvent sound) {
            this.sounds.put(soundActionId, sound);
            return this;
        }

        public ModFlowingFluid.Properties canHydrate(boolean canHydrate) {
            this.canHydrate = canHydrate;
            return this;
        }

        public ModFlowingFluid.Properties lightLevel(int lightLevel) {
            this.lightLevel = lightLevel;
            return this;
        }

        public ModFlowingFluid.Properties density(int density) {
            this.density = density;
            return this;
        }

        public ModFlowingFluid.Properties temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public ModFlowingFluid.Properties viscosity(int viscosity) {
            this.viscosity = viscosity;
            return this;
        }

        public ModFlowingFluid.Properties rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }
    }
}