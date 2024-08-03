package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.common.block.tiles.SpringLauncherArmBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SpringLauncherBlock extends Block {

    protected static final VoxelShape PISTON_BASE_EAST_AABB = Block.box(0.0, 0.0, 0.0, 12.0, 16.0, 16.0);

    protected static final VoxelShape PISTON_BASE_WEST_AABB = Block.box(4.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape PISTON_BASE_SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 12.0);

    protected static final VoxelShape PISTON_BASE_NORTH_AABB = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape PISTON_BASE_UP_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    protected static final VoxelShape PISTON_BASE_DOWN_AABB = Block.box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    public SpringLauncherBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(EXTENDED, false));
    }

    @Nullable
    public PushReaction getPistonPushReaction(BlockState state) {
        return state.m_61143_(EXTENDED) ? PushReaction.BLOCK : PushReaction.NORMAL;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return (Boolean) state.m_61143_(EXTENDED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return (Boolean) state.m_61143_(EXTENDED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTENDED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if ((Boolean) state.m_61143_(EXTENDED)) {
            return switch((Direction) state.m_61143_(FACING)) {
                case DOWN ->
                    PISTON_BASE_DOWN_AABB;
                case NORTH ->
                    PISTON_BASE_NORTH_AABB;
                case SOUTH ->
                    PISTON_BASE_SOUTH_AABB;
                case WEST ->
                    PISTON_BASE_WEST_AABB;
                case EAST ->
                    PISTON_BASE_EAST_AABB;
                default ->
                    PISTON_BASE_UP_AABB;
            };
        } else {
            return Shapes.block();
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        this.checkForMove(state, level, pos);
    }

    private void checkForMove(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            Direction direction = (Direction) state.m_61143_(FACING);
            boolean isPowered = this.shouldBeExtended(level, pos, direction);
            BlockPos offset = pos.offset(direction.getNormal());
            if (isPowered && !(Boolean) state.m_61143_(EXTENDED)) {
                if (direction == Direction.UP) {
                    BlockState sand = level.getBlockState(offset);
                    if (sand.m_60734_() instanceof FallingBlock fb) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
                        fb.tick(sand, (ServerLevel) level, offset, level.random);
                        level.setBlock(pos, state, 4);
                    }
                }
                boolean hasBrokenBlock = false;
                BlockState targetBlock = level.getBlockState(offset);
                if (targetBlock.m_60811_() == PushReaction.DESTROY || targetBlock.m_60795_()) {
                    BlockEntity blockEntity = targetBlock.m_155947_() ? level.getBlockEntity(offset) : null;
                    m_49892_(targetBlock, level, offset, blockEntity);
                    hasBrokenBlock = true;
                }
                if (hasBrokenBlock) {
                    level.setBlockAndUpdate(offset, (BlockState) ((BlockState) ((Block) ModRegistry.SPRING_LAUNCHER_ARM.get()).defaultBlockState().m_61124_(SpringLauncherArmBlock.EXTENDING, true)).m_61124_(FACING, direction));
                    level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(EXTENDED, true));
                    level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.53F, level.random.nextFloat() * 0.25F + 0.45F);
                    level.m_142346_(null, GameEvent.BLOCK_ACTIVATE, pos);
                }
            } else if (!isPowered && (Boolean) state.m_61143_(EXTENDED)) {
                BlockState bs = level.getBlockState(offset);
                if (bs.m_60734_() instanceof SpringLauncherHeadBlock && direction == bs.m_61143_(FACING)) {
                    level.setBlockAndUpdate(offset, (BlockState) ((BlockState) ((Block) ModRegistry.SPRING_LAUNCHER_ARM.get()).defaultBlockState().m_61124_(SpringLauncherArmBlock.EXTENDING, false)).m_61124_(FACING, direction));
                    level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.53F, level.random.nextFloat() * 0.15F + 0.45F);
                    level.m_142346_(null, GameEvent.BLOCK_DEACTIVATE, pos);
                } else if (bs.m_60734_() instanceof SpringLauncherArmBlock && direction == bs.m_61143_(FACING) && level.getBlockEntity(offset) instanceof SpringLauncherArmBlockTile) {
                    level.m_186460_(pos, level.getBlockState(pos).m_60734_(), 1);
                }
            }
        }
    }

    private boolean shouldBeExtended(Level level, BlockPos pos, Direction facing) {
        for (Direction direction : Direction.values()) {
            if (direction != facing && level.m_276987_(pos.relative(direction), direction)) {
                return true;
            }
        }
        if (level.m_276987_(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockpos = pos.above();
            for (Direction direction1 : Direction.values()) {
                if (direction1 != Direction.DOWN && level.m_276987_(blockpos.relative(direction1), direction1)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.m_6861_(state, level, pos, neighborBlock, fromPos, moving);
        this.checkForMove(state, level, pos);
    }
}