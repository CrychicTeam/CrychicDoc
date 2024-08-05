package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ButtonBlock extends FaceAttachedHorizontalDirectionalBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final int PRESSED_DEPTH = 1;

    private static final int UNPRESSED_DEPTH = 2;

    protected static final int HALF_AABB_HEIGHT = 2;

    protected static final int HALF_AABB_WIDTH = 3;

    protected static final VoxelShape CEILING_AABB_X = Block.box(6.0, 14.0, 5.0, 10.0, 16.0, 11.0);

    protected static final VoxelShape CEILING_AABB_Z = Block.box(5.0, 14.0, 6.0, 11.0, 16.0, 10.0);

    protected static final VoxelShape FLOOR_AABB_X = Block.box(6.0, 0.0, 5.0, 10.0, 2.0, 11.0);

    protected static final VoxelShape FLOOR_AABB_Z = Block.box(5.0, 0.0, 6.0, 11.0, 2.0, 10.0);

    protected static final VoxelShape NORTH_AABB = Block.box(5.0, 6.0, 14.0, 11.0, 10.0, 16.0);

    protected static final VoxelShape SOUTH_AABB = Block.box(5.0, 6.0, 0.0, 11.0, 10.0, 2.0);

    protected static final VoxelShape WEST_AABB = Block.box(14.0, 6.0, 5.0, 16.0, 10.0, 11.0);

    protected static final VoxelShape EAST_AABB = Block.box(0.0, 6.0, 5.0, 2.0, 10.0, 11.0);

    protected static final VoxelShape PRESSED_CEILING_AABB_X = Block.box(6.0, 15.0, 5.0, 10.0, 16.0, 11.0);

    protected static final VoxelShape PRESSED_CEILING_AABB_Z = Block.box(5.0, 15.0, 6.0, 11.0, 16.0, 10.0);

    protected static final VoxelShape PRESSED_FLOOR_AABB_X = Block.box(6.0, 0.0, 5.0, 10.0, 1.0, 11.0);

    protected static final VoxelShape PRESSED_FLOOR_AABB_Z = Block.box(5.0, 0.0, 6.0, 11.0, 1.0, 10.0);

    protected static final VoxelShape PRESSED_NORTH_AABB = Block.box(5.0, 6.0, 15.0, 11.0, 10.0, 16.0);

    protected static final VoxelShape PRESSED_SOUTH_AABB = Block.box(5.0, 6.0, 0.0, 11.0, 10.0, 1.0);

    protected static final VoxelShape PRESSED_WEST_AABB = Block.box(15.0, 6.0, 5.0, 16.0, 10.0, 11.0);

    protected static final VoxelShape PRESSED_EAST_AABB = Block.box(0.0, 6.0, 5.0, 1.0, 10.0, 11.0);

    private final BlockSetType type;

    private final int ticksToStayPressed;

    private final boolean arrowsCanPress;

    protected ButtonBlock(BlockBehaviour.Properties blockBehaviourProperties0, BlockSetType blockSetType1, int int2, boolean boolean3) {
        super(blockBehaviourProperties0.sound(blockSetType1.soundType()));
        this.type = blockSetType1;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(POWERED, false)).m_61124_(f_53179_, AttachFace.WALL));
        this.ticksToStayPressed = int2;
        this.arrowsCanPress = boolean3;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Direction $$4 = (Direction) blockState0.m_61143_(f_54117_);
        boolean $$5 = (Boolean) blockState0.m_61143_(POWERED);
        switch((AttachFace) blockState0.m_61143_(f_53179_)) {
            case FLOOR:
                if ($$4.getAxis() == Direction.Axis.X) {
                    return $$5 ? PRESSED_FLOOR_AABB_X : FLOOR_AABB_X;
                }
                return $$5 ? PRESSED_FLOOR_AABB_Z : FLOOR_AABB_Z;
            case WALL:
                return switch($$4) {
                    case EAST ->
                        $$5 ? PRESSED_EAST_AABB : EAST_AABB;
                    case WEST ->
                        $$5 ? PRESSED_WEST_AABB : WEST_AABB;
                    case SOUTH ->
                        $$5 ? PRESSED_SOUTH_AABB : SOUTH_AABB;
                    case NORTH, UP, DOWN ->
                        $$5 ? PRESSED_NORTH_AABB : NORTH_AABB;
                };
            case CEILING:
            default:
                if ($$4.getAxis() == Direction.Axis.X) {
                    return $$5 ? PRESSED_CEILING_AABB_X : CEILING_AABB_X;
                } else {
                    return $$5 ? PRESSED_CEILING_AABB_Z : CEILING_AABB_Z;
                }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if ((Boolean) blockState0.m_61143_(POWERED)) {
            return InteractionResult.CONSUME;
        } else {
            this.press(blockState0, level1, blockPos2);
            this.playSound(player3, level1, blockPos2, true);
            level1.m_142346_(player3, GameEvent.BLOCK_ACTIVATE, blockPos2);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    public void press(BlockState blockState0, Level level1, BlockPos blockPos2) {
        level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, true), 3);
        this.updateNeighbours(blockState0, level1, blockPos2);
        level1.m_186460_(blockPos2, this, this.ticksToStayPressed);
    }

    protected void playSound(@Nullable Player player0, LevelAccessor levelAccessor1, BlockPos blockPos2, boolean boolean3) {
        levelAccessor1.playSound(boolean3 ? player0 : null, blockPos2, this.getSound(boolean3), SoundSource.BLOCKS);
    }

    protected SoundEvent getSound(boolean boolean0) {
        return boolean0 ? this.type.buttonClickOn() : this.type.buttonClickOff();
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4 && !blockState0.m_60713_(blockState3.m_60734_())) {
            if ((Boolean) blockState0.m_61143_(POWERED)) {
                this.updateNeighbours(blockState0, level1, blockPos2);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) && m_53200_(blockState0) == direction3 ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(POWERED)) {
            this.checkPressed(blockState0, serverLevel1, blockPos2);
        }
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!level1.isClientSide && this.arrowsCanPress && !(Boolean) blockState0.m_61143_(POWERED)) {
            this.checkPressed(blockState0, level1, blockPos2);
        }
    }

    protected void checkPressed(BlockState blockState0, Level level1, BlockPos blockPos2) {
        AbstractArrow $$3 = this.arrowsCanPress ? (AbstractArrow) level1.m_45976_(AbstractArrow.class, blockState0.m_60808_(level1, blockPos2).bounds().move(blockPos2)).stream().findFirst().orElse(null) : null;
        boolean $$4 = $$3 != null;
        boolean $$5 = (Boolean) blockState0.m_61143_(POWERED);
        if ($$4 != $$5) {
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, $$4), 3);
            this.updateNeighbours(blockState0, level1, blockPos2);
            this.playSound(null, level1, blockPos2, $$4);
            level1.m_142346_($$3, $$4 ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, blockPos2);
        }
        if ($$4) {
            level1.m_186460_(new BlockPos(blockPos2), this, this.ticksToStayPressed);
        }
    }

    private void updateNeighbours(BlockState blockState0, Level level1, BlockPos blockPos2) {
        level1.updateNeighborsAt(blockPos2, this);
        level1.updateNeighborsAt(blockPos2.relative(m_53200_(blockState0).getOpposite()), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, POWERED, f_53179_);
    }
}