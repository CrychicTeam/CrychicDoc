package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class NoteBlock extends Block {

    public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = BlockStateProperties.NOTEBLOCK_INSTRUMENT;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final IntegerProperty NOTE = BlockStateProperties.NOTE;

    public static final int NOTE_VOLUME = 3;

    public NoteBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(INSTRUMENT, NoteBlockInstrument.HARP)).m_61124_(NOTE, 0)).m_61124_(POWERED, false));
    }

    private BlockState setInstrument(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        NoteBlockInstrument $$3 = levelAccessor0.m_8055_(blockPos1.above()).m_280603_();
        if ($$3.worksAboveNoteBlock()) {
            return (BlockState) blockState2.m_61124_(INSTRUMENT, $$3);
        } else {
            NoteBlockInstrument $$4 = levelAccessor0.m_8055_(blockPos1.below()).m_280603_();
            NoteBlockInstrument $$5 = $$4.worksAboveNoteBlock() ? NoteBlockInstrument.HARP : $$4;
            return (BlockState) blockState2.m_61124_(INSTRUMENT, $$5);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return this.setInstrument(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos(), this.m_49966_());
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        boolean $$6 = direction1.getAxis() == Direction.Axis.Y;
        return $$6 ? this.setInstrument(levelAccessor3, blockPos4, blockState0) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        boolean $$6 = level1.m_276867_(blockPos2);
        if ($$6 != (Boolean) blockState0.m_61143_(POWERED)) {
            if ($$6) {
                this.playNote(null, blockState0, level1, blockPos2);
            }
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, $$6), 3);
        }
    }

    private void playNote(@Nullable Entity entity0, BlockState blockState1, Level level2, BlockPos blockPos3) {
        if (((NoteBlockInstrument) blockState1.m_61143_(INSTRUMENT)).worksAboveNoteBlock() || level2.getBlockState(blockPos3.above()).m_60795_()) {
            level2.blockEvent(blockPos3, this, 0, 0);
            level2.m_142346_(entity0, GameEvent.NOTE_BLOCK_PLAY, blockPos3);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        if ($$6.is(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS) && blockHitResult5.getDirection() == Direction.UP) {
            return InteractionResult.PASS;
        } else if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            blockState0 = (BlockState) blockState0.m_61122_(NOTE);
            level1.setBlock(blockPos2, blockState0, 3);
            this.playNote(player3, blockState0, level1, blockPos2);
            player3.awardStat(Stats.TUNE_NOTEBLOCK);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void attack(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        if (!level1.isClientSide) {
            this.playNote(player3, blockState0, level1, blockPos2);
            player3.awardStat(Stats.PLAY_NOTEBLOCK);
        }
    }

    public static float getPitchFromNote(int int0) {
        return (float) Math.pow(2.0, (double) (int0 - 12) / 12.0);
    }

    @Override
    public boolean triggerEvent(BlockState blockState0, Level level1, BlockPos blockPos2, int int3, int int4) {
        NoteBlockInstrument $$5 = (NoteBlockInstrument) blockState0.m_61143_(INSTRUMENT);
        float $$7;
        if ($$5.isTunable()) {
            int $$6 = (Integer) blockState0.m_61143_(NOTE);
            $$7 = getPitchFromNote($$6);
            level1.addParticle(ParticleTypes.NOTE, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 1.2, (double) blockPos2.m_123343_() + 0.5, (double) $$6 / 24.0, 0.0, 0.0);
        } else {
            $$7 = 1.0F;
        }
        Holder<SoundEvent> $$10;
        if ($$5.hasCustomSound()) {
            ResourceLocation $$9 = this.getCustomSoundId(level1, blockPos2);
            if ($$9 == null) {
                return false;
            }
            $$10 = Holder.direct(SoundEvent.createVariableRangeEvent($$9));
        } else {
            $$10 = $$5.getSoundEvent();
        }
        level1.playSeededSound(null, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, $$10, SoundSource.RECORDS, 3.0F, $$7, level1.random.nextLong());
        return true;
    }

    @Nullable
    private ResourceLocation getCustomSoundId(Level level0, BlockPos blockPos1) {
        return level0.getBlockEntity(blockPos1.above()) instanceof SkullBlockEntity $$2 ? $$2.getNoteBlockSound() : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(INSTRUMENT, POWERED, NOTE);
    }
}