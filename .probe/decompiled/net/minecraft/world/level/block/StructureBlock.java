package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.phys.BlockHitResult;

public class StructureBlock extends BaseEntityBlock implements GameMasterBlock {

    public static final EnumProperty<StructureMode> MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;

    protected StructureBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(MODE, StructureMode.LOAD));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new StructureBlockEntity(blockPos0, blockState1);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        BlockEntity $$6 = level1.getBlockEntity(blockPos2);
        if ($$6 instanceof StructureBlockEntity) {
            return ((StructureBlockEntity) $$6).usedBy(player3) ? InteractionResult.sidedSuccess(level1.isClientSide) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        if (!level0.isClientSide) {
            if (livingEntity3 != null) {
                BlockEntity $$5 = level0.getBlockEntity(blockPos1);
                if ($$5 instanceof StructureBlockEntity) {
                    ((StructureBlockEntity) $$5).createdBy(livingEntity3);
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(MODE);
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (level1 instanceof ServerLevel) {
            if (level1.getBlockEntity(blockPos2) instanceof StructureBlockEntity $$7) {
                boolean $$8 = level1.m_276867_(blockPos2);
                boolean $$9 = $$7.isPowered();
                if ($$8 && !$$9) {
                    $$7.setPowered(true);
                    this.trigger((ServerLevel) level1, $$7);
                } else if (!$$8 && $$9) {
                    $$7.setPowered(false);
                }
            }
        }
    }

    private void trigger(ServerLevel serverLevel0, StructureBlockEntity structureBlockEntity1) {
        switch(structureBlockEntity1.getMode()) {
            case SAVE:
                structureBlockEntity1.saveStructure(false);
                break;
            case LOAD:
                structureBlockEntity1.loadStructure(serverLevel0, false);
                break;
            case CORNER:
                structureBlockEntity1.unloadStructure();
            case DATA:
        }
    }
}