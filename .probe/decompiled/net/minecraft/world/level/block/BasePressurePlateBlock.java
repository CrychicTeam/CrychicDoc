package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BasePressurePlateBlock extends Block {

    protected static final VoxelShape PRESSED_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 0.5, 15.0);

    protected static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);

    protected static final AABB TOUCH_AABB = new AABB(0.0625, 0.0, 0.0625, 0.9375, 0.25, 0.9375);

    private final BlockSetType type;

    protected BasePressurePlateBlock(BlockBehaviour.Properties blockBehaviourProperties0, BlockSetType blockSetType1) {
        super(blockBehaviourProperties0.sound(blockSetType1.soundType()));
        this.type = blockSetType1;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.getSignalForState(blockState0) > 0 ? PRESSED_AABB : AABB;
    }

    protected int getPressedTime() {
        return 20;
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState blockState0) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.DOWN && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.below();
        return m_49936_(levelReader1, $$3) || m_49863_(levelReader1, $$3, Direction.UP);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = this.getSignalForState(blockState0);
        if ($$4 > 0) {
            this.checkPressed(null, serverLevel1, blockPos2, blockState0, $$4);
        }
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!level1.isClientSide) {
            int $$4 = this.getSignalForState(blockState0);
            if ($$4 == 0) {
                this.checkPressed(entity3, level1, blockPos2, blockState0, $$4);
            }
        }
    }

    private void checkPressed(@Nullable Entity entity0, Level level1, BlockPos blockPos2, BlockState blockState3, int int4) {
        int $$5 = this.getSignalStrength(level1, blockPos2);
        boolean $$6 = int4 > 0;
        boolean $$7 = $$5 > 0;
        if (int4 != $$5) {
            BlockState $$8 = this.setSignalForState(blockState3, $$5);
            level1.setBlock(blockPos2, $$8, 2);
            this.updateNeighbours(level1, blockPos2);
            level1.setBlocksDirty(blockPos2, blockState3, $$8);
        }
        if (!$$7 && $$6) {
            level1.m_247517_(null, blockPos2, this.type.pressurePlateClickOff(), SoundSource.BLOCKS);
            level1.m_142346_(entity0, GameEvent.BLOCK_DEACTIVATE, blockPos2);
        } else if ($$7 && !$$6) {
            level1.m_247517_(null, blockPos2, this.type.pressurePlateClickOn(), SoundSource.BLOCKS);
            level1.m_142346_(entity0, GameEvent.BLOCK_ACTIVATE, blockPos2);
        }
        if ($$7) {
            level1.m_186460_(new BlockPos(blockPos2), this, this.getPressedTime());
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4 && !blockState0.m_60713_(blockState3.m_60734_())) {
            if (this.getSignalForState(blockState0) > 0) {
                this.updateNeighbours(level1, blockPos2);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    protected void updateNeighbours(Level level0, BlockPos blockPos1) {
        level0.updateNeighborsAt(blockPos1, this);
        level0.updateNeighborsAt(blockPos1.below(), this);
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return this.getSignalForState(blockState0);
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return direction3 == Direction.UP ? this.getSignalForState(blockState0) : 0;
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    protected static int getEntityCount(Level level0, AABB aABB1, Class<? extends Entity> classExtendsEntity2) {
        return level0.m_6443_(classExtendsEntity2, aABB1, EntitySelector.NO_SPECTATORS.and(p_289691_ -> !p_289691_.isIgnoringBlockTriggers())).size();
    }

    protected abstract int getSignalStrength(Level var1, BlockPos var2);

    protected abstract int getSignalForState(BlockState var1);

    protected abstract BlockState setSignalForState(BlockState var1, int var2);
}