package net.minecraft.world.level.block.piston;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PistonBaseBlock extends DirectionalBlock {

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    public static final int TRIGGER_EXTEND = 0;

    public static final int TRIGGER_CONTRACT = 1;

    public static final int TRIGGER_DROP = 2;

    public static final float PLATFORM_THICKNESS = 4.0F;

    protected static final VoxelShape EAST_AABB = Block.box(0.0, 0.0, 0.0, 12.0, 16.0, 16.0);

    protected static final VoxelShape WEST_AABB = Block.box(4.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 12.0);

    protected static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape UP_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    protected static final VoxelShape DOWN_AABB = Block.box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);

    private final boolean isSticky;

    public PistonBaseBlock(boolean boolean0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.NORTH)).m_61124_(EXTENDED, false));
        this.isSticky = boolean0;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if ((Boolean) blockState0.m_61143_(EXTENDED)) {
            switch((Direction) blockState0.m_61143_(f_52588_)) {
                case DOWN:
                    return DOWN_AABB;
                case UP:
                default:
                    return UP_AABB;
                case NORTH:
                    return NORTH_AABB;
                case SOUTH:
                    return SOUTH_AABB;
                case WEST:
                    return WEST_AABB;
                case EAST:
                    return EAST_AABB;
            }
        } else {
            return Shapes.block();
        }
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (!level0.isClientSide) {
            this.checkIfExtend(level0, blockPos1, blockState2);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (!level1.isClientSide) {
            this.checkIfExtend(level1, blockPos2, blockState0);
        }
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            if (!level1.isClientSide && level1.getBlockEntity(blockPos2) == null) {
                this.checkIfExtend(level1, blockPos2, blockState0);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_52588_, blockPlaceContext0.getNearestLookingDirection().getOpposite())).m_61124_(EXTENDED, false);
    }

    private void checkIfExtend(Level level0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = (Direction) blockState2.m_61143_(f_52588_);
        boolean $$4 = this.getNeighborSignal(level0, blockPos1, $$3);
        if ($$4 && !(Boolean) blockState2.m_61143_(EXTENDED)) {
            if (new PistonStructureResolver(level0, blockPos1, $$3, true).resolve()) {
                level0.blockEvent(blockPos1, this, 0, $$3.get3DDataValue());
            }
        } else if (!$$4 && (Boolean) blockState2.m_61143_(EXTENDED)) {
            BlockPos $$5 = blockPos1.relative($$3, 2);
            BlockState $$6 = level0.getBlockState($$5);
            int $$7 = 1;
            if ($$6.m_60713_(Blocks.MOVING_PISTON) && $$6.m_61143_(f_52588_) == $$3 && level0.getBlockEntity($$5) instanceof PistonMovingBlockEntity $$9 && $$9.isExtending() && ($$9.getProgress(0.0F) < 0.5F || level0.getGameTime() == $$9.getLastTicked() || ((ServerLevel) level0).isHandlingTick())) {
                $$7 = 2;
            }
            level0.blockEvent(blockPos1, this, $$7, $$3.get3DDataValue());
        }
    }

    private boolean getNeighborSignal(SignalGetter signalGetter0, BlockPos blockPos1, Direction direction2) {
        for (Direction $$3 : Direction.values()) {
            if ($$3 != direction2 && signalGetter0.hasSignal(blockPos1.relative($$3), $$3)) {
                return true;
            }
        }
        if (signalGetter0.hasSignal(blockPos1, Direction.DOWN)) {
            return true;
        } else {
            BlockPos $$4 = blockPos1.above();
            for (Direction $$5 : Direction.values()) {
                if ($$5 != Direction.DOWN && signalGetter0.hasSignal($$4.relative($$5), $$5)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean triggerEvent(BlockState blockState0, Level level1, BlockPos blockPos2, int int3, int int4) {
        Direction $$5 = (Direction) blockState0.m_61143_(f_52588_);
        BlockState $$6 = (BlockState) blockState0.m_61124_(EXTENDED, true);
        if (!level1.isClientSide) {
            boolean $$7 = this.getNeighborSignal(level1, blockPos2, $$5);
            if ($$7 && (int3 == 1 || int3 == 2)) {
                level1.setBlock(blockPos2, $$6, 2);
                return false;
            }
            if (!$$7 && int3 == 0) {
                return false;
            }
        }
        if (int3 == 0) {
            if (!this.moveBlocks(level1, blockPos2, $$5, true)) {
                return false;
            }
            level1.setBlock(blockPos2, $$6, 67);
            level1.playSound(null, blockPos2, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, level1.random.nextFloat() * 0.25F + 0.6F);
            level1.m_220407_(GameEvent.BLOCK_ACTIVATE, blockPos2, GameEvent.Context.of($$6));
        } else if (int3 == 1 || int3 == 2) {
            BlockEntity $$8 = level1.getBlockEntity(blockPos2.relative($$5));
            if ($$8 instanceof PistonMovingBlockEntity) {
                ((PistonMovingBlockEntity) $$8).finalTick();
            }
            BlockState $$9 = (BlockState) ((BlockState) Blocks.MOVING_PISTON.defaultBlockState().m_61124_(MovingPistonBlock.FACING, $$5)).m_61124_(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
            level1.setBlock(blockPos2, $$9, 20);
            level1.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(blockPos2, $$9, (BlockState) this.m_49966_().m_61124_(f_52588_, Direction.from3DDataValue(int4 & 7)), $$5, false, true));
            level1.m_6289_(blockPos2, $$9.m_60734_());
            $$9.m_60701_(level1, blockPos2, 2);
            if (this.isSticky) {
                BlockPos $$10 = blockPos2.offset($$5.getStepX() * 2, $$5.getStepY() * 2, $$5.getStepZ() * 2);
                BlockState $$11 = level1.getBlockState($$10);
                boolean $$12 = false;
                if ($$11.m_60713_(Blocks.MOVING_PISTON) && level1.getBlockEntity($$10) instanceof PistonMovingBlockEntity $$14 && $$14.getDirection() == $$5 && $$14.isExtending()) {
                    $$14.finalTick();
                    $$12 = true;
                }
                if (!$$12) {
                    if (int3 != 1 || $$11.m_60795_() || !isPushable($$11, level1, $$10, $$5.getOpposite(), false, $$5) || $$11.m_60811_() != PushReaction.NORMAL && !$$11.m_60713_(Blocks.PISTON) && !$$11.m_60713_(Blocks.STICKY_PISTON)) {
                        level1.removeBlock(blockPos2.relative($$5), false);
                    } else {
                        this.moveBlocks(level1, blockPos2, $$5, false);
                    }
                }
            } else {
                level1.removeBlock(blockPos2.relative($$5), false);
            }
            level1.playSound(null, blockPos2, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, level1.random.nextFloat() * 0.15F + 0.6F);
            level1.m_220407_(GameEvent.BLOCK_DEACTIVATE, blockPos2, GameEvent.Context.of($$9));
        }
        return true;
    }

    public static boolean isPushable(BlockState blockState0, Level level1, BlockPos blockPos2, Direction direction3, boolean boolean4, Direction direction5) {
        if (blockPos2.m_123342_() < level1.m_141937_() || blockPos2.m_123342_() > level1.m_151558_() - 1 || !level1.getWorldBorder().isWithinBounds(blockPos2)) {
            return false;
        } else if (blockState0.m_60795_()) {
            return true;
        } else if (blockState0.m_60713_(Blocks.OBSIDIAN) || blockState0.m_60713_(Blocks.CRYING_OBSIDIAN) || blockState0.m_60713_(Blocks.RESPAWN_ANCHOR) || blockState0.m_60713_(Blocks.REINFORCED_DEEPSLATE)) {
            return false;
        } else if (direction3 == Direction.DOWN && blockPos2.m_123342_() == level1.m_141937_()) {
            return false;
        } else if (direction3 == Direction.UP && blockPos2.m_123342_() == level1.m_151558_() - 1) {
            return false;
        } else {
            if (!blockState0.m_60713_(Blocks.PISTON) && !blockState0.m_60713_(Blocks.STICKY_PISTON)) {
                if (blockState0.m_60800_(level1, blockPos2) == -1.0F) {
                    return false;
                }
                switch(blockState0.m_60811_()) {
                    case BLOCK:
                        return false;
                    case DESTROY:
                        return boolean4;
                    case PUSH_ONLY:
                        return direction3 == direction5;
                }
            } else if ((Boolean) blockState0.m_61143_(EXTENDED)) {
                return false;
            }
            return !blockState0.m_155947_();
        }
    }

    private boolean moveBlocks(Level level0, BlockPos blockPos1, Direction direction2, boolean boolean3) {
        BlockPos $$4 = blockPos1.relative(direction2);
        if (!boolean3 && level0.getBlockState($$4).m_60713_(Blocks.PISTON_HEAD)) {
            level0.setBlock($$4, Blocks.AIR.defaultBlockState(), 20);
        }
        PistonStructureResolver $$5 = new PistonStructureResolver(level0, blockPos1, direction2, boolean3);
        if (!$$5.resolve()) {
            return false;
        } else {
            Map<BlockPos, BlockState> $$6 = Maps.newHashMap();
            List<BlockPos> $$7 = $$5.getToPush();
            List<BlockState> $$8 = Lists.newArrayList();
            for (int $$9 = 0; $$9 < $$7.size(); $$9++) {
                BlockPos $$10 = (BlockPos) $$7.get($$9);
                BlockState $$11 = level0.getBlockState($$10);
                $$8.add($$11);
                $$6.put($$10, $$11);
            }
            List<BlockPos> $$12 = $$5.getToDestroy();
            BlockState[] $$13 = new BlockState[$$7.size() + $$12.size()];
            Direction $$14 = boolean3 ? direction2 : direction2.getOpposite();
            int $$15 = 0;
            for (int $$16 = $$12.size() - 1; $$16 >= 0; $$16--) {
                BlockPos $$17 = (BlockPos) $$12.get($$16);
                BlockState $$18 = level0.getBlockState($$17);
                BlockEntity $$19 = $$18.m_155947_() ? level0.getBlockEntity($$17) : null;
                m_49892_($$18, level0, $$17, $$19);
                level0.setBlock($$17, Blocks.AIR.defaultBlockState(), 18);
                level0.m_220407_(GameEvent.BLOCK_DESTROY, $$17, GameEvent.Context.of($$18));
                if (!$$18.m_204336_(BlockTags.FIRE)) {
                    level0.addDestroyBlockEffect($$17, $$18);
                }
                $$13[$$15++] = $$18;
            }
            for (int $$20 = $$7.size() - 1; $$20 >= 0; $$20--) {
                BlockPos $$21 = (BlockPos) $$7.get($$20);
                BlockState $$22 = level0.getBlockState($$21);
                $$21 = $$21.relative($$14);
                $$6.remove($$21);
                BlockState $$23 = (BlockState) Blocks.MOVING_PISTON.defaultBlockState().m_61124_(f_52588_, direction2);
                level0.setBlock($$21, $$23, 68);
                level0.setBlockEntity(MovingPistonBlock.newMovingBlockEntity($$21, $$23, (BlockState) $$8.get($$20), direction2, boolean3, false));
                $$13[$$15++] = $$22;
            }
            if (boolean3) {
                PistonType $$24 = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
                BlockState $$25 = (BlockState) ((BlockState) Blocks.PISTON_HEAD.defaultBlockState().m_61124_(PistonHeadBlock.f_52588_, direction2)).m_61124_(PistonHeadBlock.TYPE, $$24);
                BlockState $$26 = (BlockState) ((BlockState) Blocks.MOVING_PISTON.defaultBlockState().m_61124_(MovingPistonBlock.FACING, direction2)).m_61124_(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
                $$6.remove($$4);
                level0.setBlock($$4, $$26, 68);
                level0.setBlockEntity(MovingPistonBlock.newMovingBlockEntity($$4, $$26, $$25, direction2, true, true));
            }
            BlockState $$27 = Blocks.AIR.defaultBlockState();
            for (BlockPos $$28 : $$6.keySet()) {
                level0.setBlock($$28, $$27, 82);
            }
            for (Entry<BlockPos, BlockState> $$29 : $$6.entrySet()) {
                BlockPos $$30 = (BlockPos) $$29.getKey();
                BlockState $$31 = (BlockState) $$29.getValue();
                $$31.m_60758_(level0, $$30, 2);
                $$27.m_60701_(level0, $$30, 2);
                $$27.m_60758_(level0, $$30, 2);
            }
            $$15 = 0;
            for (int $$32 = $$12.size() - 1; $$32 >= 0; $$32--) {
                BlockState $$33 = $$13[$$15++];
                BlockPos $$34 = (BlockPos) $$12.get($$32);
                $$33.m_60758_(level0, $$34, 2);
                level0.updateNeighborsAt($$34, $$33.m_60734_());
            }
            for (int $$35 = $$7.size() - 1; $$35 >= 0; $$35--) {
                level0.updateNeighborsAt((BlockPos) $$7.get($$35), $$13[$$15++].m_60734_());
            }
            if (boolean3) {
                level0.updateNeighborsAt($$4, Blocks.PISTON_HEAD);
            }
            return true;
        }
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(f_52588_, rotation1.rotate((Direction) blockState0.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(f_52588_)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52588_, EXTENDED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return (Boolean) blockState0.m_61143_(EXTENDED);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}