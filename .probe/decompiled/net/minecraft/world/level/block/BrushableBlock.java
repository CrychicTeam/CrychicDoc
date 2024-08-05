package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BrushableBlock extends BaseEntityBlock implements Fallable {

    private static final IntegerProperty DUSTED = BlockStateProperties.DUSTED;

    public static final int TICK_DELAY = 2;

    private final Block turnsInto;

    private final SoundEvent brushSound;

    private final SoundEvent brushCompletedSound;

    public BrushableBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1, SoundEvent soundEvent2, SoundEvent soundEvent3) {
        super(blockBehaviourProperties1);
        this.turnsInto = block0;
        this.brushSound = soundEvent2;
        this.brushCompletedSound = soundEvent3;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(DUSTED, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(DUSTED);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        level1.m_186460_(blockPos2, this, 2);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        levelAccessor3.scheduleTick(blockPos4, this, 2);
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_7702_(blockPos2) instanceof BrushableBlockEntity $$4) {
            $$4.checkReset();
        }
        if (FallingBlock.isFree(serverLevel1.m_8055_(blockPos2.below())) && blockPos2.m_123342_() >= serverLevel1.m_141937_()) {
            FallingBlockEntity $$5 = FallingBlockEntity.fall(serverLevel1, blockPos2, blockState0);
            $$5.disableDrop();
        }
    }

    @Override
    public void onBrokenAfterFall(Level level0, BlockPos blockPos1, FallingBlockEntity fallingBlockEntity2) {
        Vec3 $$3 = fallingBlockEntity2.m_20191_().getCenter();
        level0.m_46796_(2001, BlockPos.containing($$3), Block.getId(fallingBlockEntity2.getBlockState()));
        level0.m_220400_(fallingBlockEntity2, GameEvent.BLOCK_DESTROY, $$3);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(16) == 0) {
            BlockPos $$4 = blockPos2.below();
            if (FallingBlock.isFree(level1.getBlockState($$4))) {
                double $$5 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
                double $$6 = (double) blockPos2.m_123342_() - 0.05;
                double $$7 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
                level1.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, blockState0), $$5, $$6, $$7, 0.0, 0.0, 0.0);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new BrushableBlockEntity(blockPos0, blockState1);
    }

    public Block getTurnsInto() {
        return this.turnsInto;
    }

    public SoundEvent getBrushSound() {
        return this.brushSound;
    }

    public SoundEvent getBrushCompletedSound() {
        return this.brushCompletedSound;
    }
}