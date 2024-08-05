package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrindstoneBlock extends FaceAttachedHorizontalDirectionalBlock {

    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_POST = Block.box(2.0, 0.0, 6.0, 4.0, 7.0, 10.0);

    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_POST = Block.box(12.0, 0.0, 6.0, 14.0, 7.0, 10.0);

    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0, 7.0, 5.0, 4.0, 13.0, 11.0);

    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0, 7.0, 5.0, 14.0, 13.0, 11.0);

    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_LEG = Shapes.or(FLOOR_NORTH_SOUTH_LEFT_POST, FLOOR_NORTH_SOUTH_LEFT_PIVOT);

    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_LEG = Shapes.or(FLOOR_NORTH_SOUTH_RIGHT_POST, FLOOR_NORTH_SOUTH_RIGHT_PIVOT);

    public static final VoxelShape FLOOR_NORTH_SOUTH_ALL_LEGS = Shapes.or(FLOOR_NORTH_SOUTH_LEFT_LEG, FLOOR_NORTH_SOUTH_RIGHT_LEG);

    public static final VoxelShape FLOOR_NORTH_SOUTH_GRINDSTONE = Shapes.or(FLOOR_NORTH_SOUTH_ALL_LEGS, Block.box(4.0, 4.0, 2.0, 12.0, 16.0, 14.0));

    public static final VoxelShape FLOOR_EAST_WEST_LEFT_POST = Block.box(6.0, 0.0, 2.0, 10.0, 7.0, 4.0);

    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_POST = Block.box(6.0, 0.0, 12.0, 10.0, 7.0, 14.0);

    public static final VoxelShape FLOOR_EAST_WEST_LEFT_PIVOT = Block.box(5.0, 7.0, 2.0, 11.0, 13.0, 4.0);

    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_PIVOT = Block.box(5.0, 7.0, 12.0, 11.0, 13.0, 14.0);

    public static final VoxelShape FLOOR_EAST_WEST_LEFT_LEG = Shapes.or(FLOOR_EAST_WEST_LEFT_POST, FLOOR_EAST_WEST_LEFT_PIVOT);

    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_LEG = Shapes.or(FLOOR_EAST_WEST_RIGHT_POST, FLOOR_EAST_WEST_RIGHT_PIVOT);

    public static final VoxelShape FLOOR_EAST_WEST_ALL_LEGS = Shapes.or(FLOOR_EAST_WEST_LEFT_LEG, FLOOR_EAST_WEST_RIGHT_LEG);

    public static final VoxelShape FLOOR_EAST_WEST_GRINDSTONE = Shapes.or(FLOOR_EAST_WEST_ALL_LEGS, Block.box(2.0, 4.0, 4.0, 14.0, 16.0, 12.0));

    public static final VoxelShape WALL_SOUTH_LEFT_POST = Block.box(2.0, 6.0, 0.0, 4.0, 10.0, 7.0);

    public static final VoxelShape WALL_SOUTH_RIGHT_POST = Block.box(12.0, 6.0, 0.0, 14.0, 10.0, 7.0);

    public static final VoxelShape WALL_SOUTH_LEFT_PIVOT = Block.box(2.0, 5.0, 7.0, 4.0, 11.0, 13.0);

    public static final VoxelShape WALL_SOUTH_RIGHT_PIVOT = Block.box(12.0, 5.0, 7.0, 14.0, 11.0, 13.0);

    public static final VoxelShape WALL_SOUTH_LEFT_LEG = Shapes.or(WALL_SOUTH_LEFT_POST, WALL_SOUTH_LEFT_PIVOT);

    public static final VoxelShape WALL_SOUTH_RIGHT_LEG = Shapes.or(WALL_SOUTH_RIGHT_POST, WALL_SOUTH_RIGHT_PIVOT);

    public static final VoxelShape WALL_SOUTH_ALL_LEGS = Shapes.or(WALL_SOUTH_LEFT_LEG, WALL_SOUTH_RIGHT_LEG);

    public static final VoxelShape WALL_SOUTH_GRINDSTONE = Shapes.or(WALL_SOUTH_ALL_LEGS, Block.box(4.0, 2.0, 4.0, 12.0, 14.0, 16.0));

    public static final VoxelShape WALL_NORTH_LEFT_POST = Block.box(2.0, 6.0, 7.0, 4.0, 10.0, 16.0);

    public static final VoxelShape WALL_NORTH_RIGHT_POST = Block.box(12.0, 6.0, 7.0, 14.0, 10.0, 16.0);

    public static final VoxelShape WALL_NORTH_LEFT_PIVOT = Block.box(2.0, 5.0, 3.0, 4.0, 11.0, 9.0);

    public static final VoxelShape WALL_NORTH_RIGHT_PIVOT = Block.box(12.0, 5.0, 3.0, 14.0, 11.0, 9.0);

    public static final VoxelShape WALL_NORTH_LEFT_LEG = Shapes.or(WALL_NORTH_LEFT_POST, WALL_NORTH_LEFT_PIVOT);

    public static final VoxelShape WALL_NORTH_RIGHT_LEG = Shapes.or(WALL_NORTH_RIGHT_POST, WALL_NORTH_RIGHT_PIVOT);

    public static final VoxelShape WALL_NORTH_ALL_LEGS = Shapes.or(WALL_NORTH_LEFT_LEG, WALL_NORTH_RIGHT_LEG);

    public static final VoxelShape WALL_NORTH_GRINDSTONE = Shapes.or(WALL_NORTH_ALL_LEGS, Block.box(4.0, 2.0, 0.0, 12.0, 14.0, 12.0));

    public static final VoxelShape WALL_WEST_LEFT_POST = Block.box(7.0, 6.0, 2.0, 16.0, 10.0, 4.0);

    public static final VoxelShape WALL_WEST_RIGHT_POST = Block.box(7.0, 6.0, 12.0, 16.0, 10.0, 14.0);

    public static final VoxelShape WALL_WEST_LEFT_PIVOT = Block.box(3.0, 5.0, 2.0, 9.0, 11.0, 4.0);

    public static final VoxelShape WALL_WEST_RIGHT_PIVOT = Block.box(3.0, 5.0, 12.0, 9.0, 11.0, 14.0);

    public static final VoxelShape WALL_WEST_LEFT_LEG = Shapes.or(WALL_WEST_LEFT_POST, WALL_WEST_LEFT_PIVOT);

    public static final VoxelShape WALL_WEST_RIGHT_LEG = Shapes.or(WALL_WEST_RIGHT_POST, WALL_WEST_RIGHT_PIVOT);

    public static final VoxelShape WALL_WEST_ALL_LEGS = Shapes.or(WALL_WEST_LEFT_LEG, WALL_WEST_RIGHT_LEG);

    public static final VoxelShape WALL_WEST_GRINDSTONE = Shapes.or(WALL_WEST_ALL_LEGS, Block.box(0.0, 2.0, 4.0, 12.0, 14.0, 12.0));

    public static final VoxelShape WALL_EAST_LEFT_POST = Block.box(0.0, 6.0, 2.0, 9.0, 10.0, 4.0);

    public static final VoxelShape WALL_EAST_RIGHT_POST = Block.box(0.0, 6.0, 12.0, 9.0, 10.0, 14.0);

    public static final VoxelShape WALL_EAST_LEFT_PIVOT = Block.box(7.0, 5.0, 2.0, 13.0, 11.0, 4.0);

    public static final VoxelShape WALL_EAST_RIGHT_PIVOT = Block.box(7.0, 5.0, 12.0, 13.0, 11.0, 14.0);

    public static final VoxelShape WALL_EAST_LEFT_LEG = Shapes.or(WALL_EAST_LEFT_POST, WALL_EAST_LEFT_PIVOT);

    public static final VoxelShape WALL_EAST_RIGHT_LEG = Shapes.or(WALL_EAST_RIGHT_POST, WALL_EAST_RIGHT_PIVOT);

    public static final VoxelShape WALL_EAST_ALL_LEGS = Shapes.or(WALL_EAST_LEFT_LEG, WALL_EAST_RIGHT_LEG);

    public static final VoxelShape WALL_EAST_GRINDSTONE = Shapes.or(WALL_EAST_ALL_LEGS, Block.box(4.0, 2.0, 4.0, 16.0, 14.0, 12.0));

    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_POST = Block.box(2.0, 9.0, 6.0, 4.0, 16.0, 10.0);

    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_POST = Block.box(12.0, 9.0, 6.0, 14.0, 16.0, 10.0);

    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0, 3.0, 5.0, 4.0, 9.0, 11.0);

    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0, 3.0, 5.0, 14.0, 9.0, 11.0);

    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_LEG = Shapes.or(CEILING_NORTH_SOUTH_LEFT_POST, CEILING_NORTH_SOUTH_LEFT_PIVOT);

    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_LEG = Shapes.or(CEILING_NORTH_SOUTH_RIGHT_POST, CEILING_NORTH_SOUTH_RIGHT_PIVOT);

    public static final VoxelShape CEILING_NORTH_SOUTH_ALL_LEGS = Shapes.or(CEILING_NORTH_SOUTH_LEFT_LEG, CEILING_NORTH_SOUTH_RIGHT_LEG);

    public static final VoxelShape CEILING_NORTH_SOUTH_GRINDSTONE = Shapes.or(CEILING_NORTH_SOUTH_ALL_LEGS, Block.box(4.0, 0.0, 2.0, 12.0, 12.0, 14.0));

    public static final VoxelShape CEILING_EAST_WEST_LEFT_POST = Block.box(6.0, 9.0, 2.0, 10.0, 16.0, 4.0);

    public static final VoxelShape CEILING_EAST_WEST_RIGHT_POST = Block.box(6.0, 9.0, 12.0, 10.0, 16.0, 14.0);

    public static final VoxelShape CEILING_EAST_WEST_LEFT_PIVOT = Block.box(5.0, 3.0, 2.0, 11.0, 9.0, 4.0);

    public static final VoxelShape CEILING_EAST_WEST_RIGHT_PIVOT = Block.box(5.0, 3.0, 12.0, 11.0, 9.0, 14.0);

    public static final VoxelShape CEILING_EAST_WEST_LEFT_LEG = Shapes.or(CEILING_EAST_WEST_LEFT_POST, CEILING_EAST_WEST_LEFT_PIVOT);

    public static final VoxelShape CEILING_EAST_WEST_RIGHT_LEG = Shapes.or(CEILING_EAST_WEST_RIGHT_POST, CEILING_EAST_WEST_RIGHT_PIVOT);

    public static final VoxelShape CEILING_EAST_WEST_ALL_LEGS = Shapes.or(CEILING_EAST_WEST_LEFT_LEG, CEILING_EAST_WEST_RIGHT_LEG);

    public static final VoxelShape CEILING_EAST_WEST_GRINDSTONE = Shapes.or(CEILING_EAST_WEST_ALL_LEGS, Block.box(2.0, 0.0, 4.0, 14.0, 12.0, 12.0));

    private static final Component CONTAINER_TITLE = Component.translatable("container.grindstone_title");

    protected GrindstoneBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(f_53179_, AttachFace.WALL));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    private VoxelShape getVoxelShape(BlockState blockState0) {
        Direction $$1 = (Direction) blockState0.m_61143_(f_54117_);
        switch((AttachFace) blockState0.m_61143_(f_53179_)) {
            case FLOOR:
                if ($$1 != Direction.NORTH && $$1 != Direction.SOUTH) {
                    return FLOOR_EAST_WEST_GRINDSTONE;
                }
                return FLOOR_NORTH_SOUTH_GRINDSTONE;
            case WALL:
                if ($$1 == Direction.NORTH) {
                    return WALL_NORTH_GRINDSTONE;
                } else if ($$1 == Direction.SOUTH) {
                    return WALL_SOUTH_GRINDSTONE;
                } else {
                    if ($$1 == Direction.EAST) {
                        return WALL_EAST_GRINDSTONE;
                    }
                    return WALL_WEST_GRINDSTONE;
                }
            case CEILING:
                if ($$1 != Direction.NORTH && $$1 != Direction.SOUTH) {
                    return CEILING_EAST_WEST_GRINDSTONE;
                }
                return CEILING_NORTH_SOUTH_GRINDSTONE;
            default:
                return FLOOR_EAST_WEST_GRINDSTONE;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.getVoxelShape(blockState0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.getVoxelShape(blockState0);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player3.openMenu(blockState0.m_60750_(level1, blockPos2));
            player3.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return new SimpleMenuProvider((p_53812_, p_53813_, p_53814_) -> new GrindstoneMenu(p_53812_, p_53813_, ContainerLevelAccess.create(level1, blockPos2)), CONTAINER_TITLE);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(f_54117_, rotation1.rotate((Direction) blockState0.m_61143_(f_54117_)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(f_54117_)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, f_53179_);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}