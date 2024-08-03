package dev.architectury.core.fluid;

import com.google.common.base.MoreObjects;
import com.google.common.base.Suppliers;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class ArchitecturyFlowingFluid extends ForgeFlowingFluid {

    private static final Map<ArchitecturyFluidAttributes, FluidType> FLUID_TYPE_MAP = new IdentityHashMap();

    private final ArchitecturyFluidAttributes attributes;

    ArchitecturyFlowingFluid(ArchitecturyFluidAttributes attributes) {
        super(toForgeProperties(attributes));
        this.attributes = attributes;
    }

    private static ForgeFlowingFluid.Properties toForgeProperties(ArchitecturyFluidAttributes attributes) {
        ForgeFlowingFluid.Properties forge = new ForgeFlowingFluid.Properties(Suppliers.memoize(() -> (FluidType) FLUID_TYPE_MAP.computeIfAbsent(attributes, attr -> new ArchitecturyFluidAttributesForge(FluidType.Properties.create(), attr.getSourceFluid(), attr))), attributes::getSourceFluid, attributes::getFlowingFluid);
        forge.slopeFindDistance(attributes.getSlopeFindDistance());
        forge.levelDecreasePerBlock(attributes.getDropOff());
        forge.bucket(() -> (Item) MoreObjects.firstNonNull(attributes.getBucketItem(), Items.AIR));
        forge.tickRate(attributes.getTickDelay());
        forge.explosionResistance(attributes.getExplosionResistance());
        forge.block(() -> (LiquidBlock) MoreObjects.firstNonNull(attributes.getBlock(), (LiquidBlock) Blocks.WATER));
        return forge;
    }

    @Override
    public Fluid getFlowing() {
        return this.attributes.getFlowingFluid();
    }

    @Override
    public Fluid getSource() {
        return this.attributes.getSourceFluid();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return this.attributes.canConvertToSource();
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.m_155947_() ? level.m_7702_(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return this.attributes.getSlopeFindDistance(level);
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return this.attributes.getDropOff(level);
    }

    @Override
    public Item getBucket() {
        Item item = this.attributes.getBucketItem();
        return item == null ? Items.AIR : item;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !this.isSame(fluid);
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return this.attributes.getTickDelay(level);
    }

    @Override
    protected float getExplosionResistance() {
        return this.attributes.getExplosionResistance();
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        LiquidBlock block = this.attributes.getBlock();
        return block == null ? Blocks.AIR.defaultBlockState() : (BlockState) block.m_49966_().m_61124_(LiquidBlock.LEVEL, m_76092_(state));
    }

    @NotNull
    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.ofNullable(this.attributes.getFillSound());
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == this.getSource() || fluid == this.getFlowing();
    }

    public static class Flowing extends ArchitecturyFlowingFluid {

        public Flowing(ArchitecturyFluidAttributes attributes) {
            super(attributes);
            this.m_76142_((FluidState) ((FluidState) this.m_76144_().any()).m_61124_(f_75948_, 7));
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.m_7180_(builder);
            builder.add(f_75948_);
        }

        @Override
        public int getAmount(FluidState state) {
            return (Integer) state.m_61143_(f_75948_);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends ArchitecturyFlowingFluid {

        public Source(ArchitecturyFluidAttributes attributes) {
            super(attributes);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}