package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FenceBlock extends CrossCollisionBlock {

    private final VoxelShape[] occlusionByIndex;

    public FenceBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(2.0F, 2.0F, 16.0F, 16.0F, 24.0F, blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52309_, false)).m_61124_(f_52310_, false)).m_61124_(f_52311_, false)).m_61124_(f_52312_, false)).m_61124_(f_52313_, false));
        this.occlusionByIndex = this.m_52326_(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return this.occlusionByIndex[this.m_52363_(blockState0)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.m_5940_(blockState0, blockGetter1, blockPos2, collisionContext3);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    public boolean connectsTo(BlockState blockState0, boolean boolean1, Direction direction2) {
        Block $$3 = blockState0.m_60734_();
        boolean $$4 = this.isSameFence(blockState0);
        boolean $$5 = $$3 instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(blockState0, direction2);
        return !m_152463_(blockState0) && boolean1 || $$4 || $$5;
    }

    private boolean isSameFence(BlockState blockState0) {
        return blockState0.m_204336_(BlockTags.FENCES) && blockState0.m_204336_(BlockTags.WOODEN_FENCES) == this.m_49966_().m_204336_(BlockTags.WOODEN_FENCES);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            ItemStack $$6 = player3.m_21120_(interactionHand4);
            return $$6.is(Items.LEAD) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            return LeadItem.bindPlayerMobs(player3, level1, blockPos2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockGetter $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        FluidState $$3 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        BlockPos $$4 = $$2.north();
        BlockPos $$5 = $$2.east();
        BlockPos $$6 = $$2.south();
        BlockPos $$7 = $$2.west();
        BlockState $$8 = $$1.getBlockState($$4);
        BlockState $$9 = $$1.getBlockState($$5);
        BlockState $$10 = $$1.getBlockState($$6);
        BlockState $$11 = $$1.getBlockState($$7);
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) super.m_5573_(blockPlaceContext0).m_61124_(f_52309_, this.connectsTo($$8, $$8.m_60783_($$1, $$4, Direction.SOUTH), Direction.SOUTH))).m_61124_(f_52310_, this.connectsTo($$9, $$9.m_60783_($$1, $$5, Direction.WEST), Direction.WEST))).m_61124_(f_52311_, this.connectsTo($$10, $$10.m_60783_($$1, $$6, Direction.NORTH), Direction.NORTH))).m_61124_(f_52312_, this.connectsTo($$11, $$11.m_60783_($$1, $$7, Direction.EAST), Direction.EAST))).m_61124_(f_52313_, $$3.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(f_52313_)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? (BlockState) blockState0.m_61124_((Property) f_52314_.get(direction1), this.connectsTo(blockState2, blockState2.m_60783_(levelAccessor3, blockPos5, direction1.getOpposite()), direction1.getOpposite())) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52309_, f_52310_, f_52312_, f_52311_, f_52313_);
    }
}