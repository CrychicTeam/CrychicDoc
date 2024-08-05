package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class JukeboxBlock extends BaseEntityBlock {

    public static final BooleanProperty HAS_RECORD = BlockStateProperties.HAS_RECORD;

    protected JukeboxBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HAS_RECORD, false));
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        super.m_6402_(level0, blockPos1, blockState2, livingEntity3, itemStack4);
        CompoundTag $$5 = BlockItem.getBlockEntityData(itemStack4);
        if ($$5 != null && $$5.contains("RecordItem")) {
            level0.setBlock(blockPos1, (BlockState) blockState2.m_61124_(HAS_RECORD, true), 2);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if ((Boolean) blockState0.m_61143_(HAS_RECORD) && level1.getBlockEntity(blockPos2) instanceof JukeboxBlockEntity $$6) {
            $$6.popOutRecord();
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if (level1.getBlockEntity(blockPos2) instanceof JukeboxBlockEntity $$5) {
                $$5.popOutRecord();
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new JukeboxBlockEntity(blockPos0, blockState1);
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        if (blockGetter1.getBlockEntity(blockPos2) instanceof JukeboxBlockEntity $$4 && $$4.isRecordPlaying()) {
            return 15;
        }
        return 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        if (level1.getBlockEntity(blockPos2) instanceof JukeboxBlockEntity $$3 && $$3.m_272036_().getItem() instanceof RecordItem $$4) {
            return $$4.getAnalogOutput();
        }
        return 0;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HAS_RECORD);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return blockState1.m_61143_(HAS_RECORD) ? m_152132_(blockEntityTypeT2, BlockEntityType.JUKEBOX, JukeboxBlockEntity::m_239937_) : null;
    }
}