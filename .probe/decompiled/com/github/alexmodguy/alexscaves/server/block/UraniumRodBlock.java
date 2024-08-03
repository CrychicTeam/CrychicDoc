package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class UraniumRodBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

    private static final VoxelShape SHAPE_X = ACMath.buildShape(Block.box(2.0, 6.0, 6.0, 14.0, 10.0, 10.0), Block.box(14.0, 5.0, 5.0, 16.0, 11.0, 11.0), Block.box(0.0, 5.0, 5.0, 2.0, 11.0, 11.0));

    private static final VoxelShape SHAPE_Y = ACMath.buildShape(Block.box(6.0, 2.0, 6.0, 10.0, 14.0, 10.0), Block.box(5.0, 0.0, 5.0, 11.0, 2.0, 11.0), Block.box(5.0, 14.0, 5.0, 11.0, 16.0, 11.0));

    private static final VoxelShape SHAPE_Z = ACMath.buildShape(Block.box(6.0, 6.0, 2.0, 10.0, 10.0, 14.0), Block.box(5.0, 5.0, 14.0, 11.0, 11.0, 16.0), Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 2.0));

    public static final IntegerProperty LIQUID_LOGGED = IntegerProperty.create("liquid_logged", 0, 2);

    public UraniumRodBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(1.5F).lightLevel(state -> 9).emissiveRendering((state, level, pos) -> true).sound(ACSoundTypes.URANIUM));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(LIQUID_LOGGED, 0)).m_61124_(f_55923_, Direction.Axis.Y));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        switch((Direction.Axis) state.m_61143_(f_55923_)) {
            case X:
                return SHAPE_X;
            case Y:
                return SHAPE_Y;
            case Z:
                return SHAPE_Z;
            default:
                return SHAPE_Y;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(80) == 0) {
            level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, ACSoundRegistry.URANIUM_HUM.get(), SoundSource.BLOCKS, 0.5F, randomSource.nextFloat() * 0.4F + 0.8F, false);
        }
        if (randomSource.nextInt(10) == 0) {
            Vec3 center = Vec3.upFromBottomCenterOf(pos, 0.5);
            level.addParticle(ACParticleRegistry.PROTON.get(), center.x, center.y, center.z, center.x, center.y, center.z);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        if (liquidType == 1) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        } else if (liquidType == 2) {
            levelAccessor.scheduleTick(blockPos, ACFluidRegistry.ACID_FLUID_SOURCE.get(), ACFluidRegistry.ACID_FLUID_SOURCE.get().m_6718_(levelAccessor));
        }
        if (!levelAccessor.m_5776_()) {
            levelAccessor.scheduleTick(blockPos, this, 1);
        }
        return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        return (BlockState) super.getStateForPlacement(context).m_61124_(LIQUID_LOGGED, getLiquidType(levelaccessor.m_6425_(blockpos)));
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter getter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return fluid == Fluids.WATER || fluid.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get();
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos pos, BlockState blockState, FluidState fluidState) {
        int liquidType = (Integer) blockState.m_61143_(LIQUID_LOGGED);
        if (liquidType == 0) {
            if (!levelAccessor.m_5776_()) {
                if (fluidState.getType() == Fluids.WATER) {
                    levelAccessor.m_7731_(pos, (BlockState) blockState.m_61124_(LIQUID_LOGGED, 1), 3);
                } else if (fluidState.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get()) {
                    levelAccessor.m_7731_(pos, (BlockState) blockState.m_61124_(LIQUID_LOGGED, 2), 3);
                }
                levelAccessor.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(levelAccessor));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState state) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        if (liquidType > 0) {
            levelAccessor.m_7731_(blockPos, (BlockState) state.m_61124_(LIQUID_LOGGED, 0), 3);
            if (!state.m_60710_(levelAccessor, blockPos)) {
                levelAccessor.m_46961_(blockPos, true);
            }
            return new ItemStack((ItemLike) (liquidType == 1 ? Items.WATER_BUCKET : ACItemRegistry.ACID_BUCKET.get()));
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.m_142520_();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        int liquidType = (Integer) state.m_61143_(LIQUID_LOGGED);
        return liquidType == 1 ? Fluids.WATER.getSource(false) : (liquidType == 2 ? ACFluidRegistry.ACID_FLUID_SOURCE.get().getSource(false) : super.m_5888_(state));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LIQUID_LOGGED, f_55923_);
    }

    public static int getLiquidType(FluidState fluidState) {
        if (fluidState.getType() == Fluids.WATER) {
            return 1;
        } else {
            return fluidState.getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get() && fluidState.isSource() ? 2 : 0;
        }
    }
}