package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SafetyNetBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(0.0, 7.0, 0.0, 16.0, 9.0, 16.0);

    public SafetyNetBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return super.m_7417_(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entityIn, fallDistance);
        } else {
            entityIn.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entityIn) {
        if (entityIn.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entityIn);
        } else {
            this.bounceEntity(entityIn);
        }
    }

    private void bounceEntity(Entity entityIn) {
        Vec3 vec3d = entityIn.getDeltaMovement();
        if (vec3d.y < 0.0) {
            double entityWeightOffset = entityIn instanceof LivingEntity ? 0.6 : 0.8;
            entityIn.setDeltaMovement(vec3d.x, -vec3d.y * entityWeightOffset, vec3d.z);
        }
    }
}