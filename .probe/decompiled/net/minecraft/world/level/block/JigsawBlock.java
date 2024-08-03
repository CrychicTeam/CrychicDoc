package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.BlockHitResult;

public class JigsawBlock extends Block implements EntityBlock, GameMasterBlock {

    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    protected JigsawBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(ORIENTATION, FrontAndTop.NORTH_UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(ORIENTATION);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(ORIENTATION, rotation1.rotation().rotate((FrontAndTop) blockState0.m_61143_(ORIENTATION)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return (BlockState) blockState0.m_61124_(ORIENTATION, mirror1.rotation().rotate((FrontAndTop) blockState0.m_61143_(ORIENTATION)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Direction $$1 = blockPlaceContext0.m_43719_();
        Direction $$2;
        if ($$1.getAxis() == Direction.Axis.Y) {
            $$2 = blockPlaceContext0.m_8125_().getOpposite();
        } else {
            $$2 = Direction.UP;
        }
        return (BlockState) this.m_49966_().m_61124_(ORIENTATION, FrontAndTop.fromFrontAndTop($$1, $$2));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new JigsawBlockEntity(blockPos0, blockState1);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        BlockEntity $$6 = level1.getBlockEntity(blockPos2);
        if ($$6 instanceof JigsawBlockEntity && player3.canUseGameMasterBlocks()) {
            player3.openJigsawBlock((JigsawBlockEntity) $$6);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public static boolean canAttach(StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo0, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo1) {
        Direction $$2 = getFrontFacing(structureTemplateStructureBlockInfo0.state());
        Direction $$3 = getFrontFacing(structureTemplateStructureBlockInfo1.state());
        Direction $$4 = getTopFacing(structureTemplateStructureBlockInfo0.state());
        Direction $$5 = getTopFacing(structureTemplateStructureBlockInfo1.state());
        JigsawBlockEntity.JointType $$6 = (JigsawBlockEntity.JointType) JigsawBlockEntity.JointType.byName(structureTemplateStructureBlockInfo0.nbt().getString("joint")).orElseGet(() -> $$2.getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE);
        boolean $$7 = $$6 == JigsawBlockEntity.JointType.ROLLABLE;
        return $$2 == $$3.getOpposite() && ($$7 || $$4 == $$5) && structureTemplateStructureBlockInfo0.nbt().getString("target").equals(structureTemplateStructureBlockInfo1.nbt().getString("name"));
    }

    public static Direction getFrontFacing(BlockState blockState0) {
        return ((FrontAndTop) blockState0.m_61143_(ORIENTATION)).front();
    }

    public static Direction getTopFacing(BlockState blockState0) {
        return ((FrontAndTop) blockState0.m_61143_(ORIENTATION)).top();
    }
}