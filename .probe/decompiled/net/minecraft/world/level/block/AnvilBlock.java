package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AnvilBlock extends FallingBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape BASE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    private static final VoxelShape X_LEG1 = Block.box(3.0, 4.0, 4.0, 13.0, 5.0, 12.0);

    private static final VoxelShape X_LEG2 = Block.box(4.0, 5.0, 6.0, 12.0, 10.0, 10.0);

    private static final VoxelShape X_TOP = Block.box(0.0, 10.0, 3.0, 16.0, 16.0, 13.0);

    private static final VoxelShape Z_LEG1 = Block.box(4.0, 4.0, 3.0, 12.0, 5.0, 13.0);

    private static final VoxelShape Z_LEG2 = Block.box(6.0, 5.0, 4.0, 10.0, 10.0, 12.0);

    private static final VoxelShape Z_TOP = Block.box(3.0, 10.0, 0.0, 13.0, 16.0, 16.0);

    private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, X_LEG1, X_LEG2, X_TOP);

    private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, Z_LEG1, Z_LEG2, Z_TOP);

    private static final Component CONTAINER_TITLE = Component.translatable("container.repair");

    private static final float FALL_DAMAGE_PER_DISTANCE = 2.0F;

    private static final int FALL_DAMAGE_MAX = 40;

    public AnvilBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_().getClockWise());
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player3.openMenu(blockState0.m_60750_(level1, blockPos2));
            player3.awardStat(Stats.INTERACT_WITH_ANVIL);
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return new SimpleMenuProvider((p_48785_, p_48786_, p_48787_) -> new AnvilMenu(p_48785_, p_48786_, ContainerLevelAccess.create(level1, blockPos2)), CONTAINER_TITLE);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Direction $$4 = (Direction) blockState0.m_61143_(FACING);
        return $$4.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Override
    protected void falling(FallingBlockEntity fallingBlockEntity0) {
        fallingBlockEntity0.setHurtsEntities(2.0F, 40);
    }

    @Override
    public void onLand(Level level0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3, FallingBlockEntity fallingBlockEntity4) {
        if (!fallingBlockEntity4.m_20067_()) {
            level0.m_46796_(1031, blockPos1, 0);
        }
    }

    @Override
    public void onBrokenAfterFall(Level level0, BlockPos blockPos1, FallingBlockEntity fallingBlockEntity2) {
        if (!fallingBlockEntity2.m_20067_()) {
            level0.m_46796_(1029, blockPos1, 0);
        }
    }

    @Override
    public DamageSource getFallDamageSource(Entity entity0) {
        return entity0.damageSources().anvil(entity0);
    }

    @Nullable
    public static BlockState damage(BlockState blockState0) {
        if (blockState0.m_60713_(Blocks.ANVIL)) {
            return (BlockState) Blocks.CHIPPED_ANVIL.defaultBlockState().m_61124_(FACING, (Direction) blockState0.m_61143_(FACING));
        } else {
            return blockState0.m_60713_(Blocks.CHIPPED_ANVIL) ? (BlockState) Blocks.DAMAGED_ANVIL.defaultBlockState().m_61124_(FACING, (Direction) blockState0.m_61143_(FACING)) : null;
        }
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public int getDustColor(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_284242_(blockGetter1, blockPos2).col;
    }
}