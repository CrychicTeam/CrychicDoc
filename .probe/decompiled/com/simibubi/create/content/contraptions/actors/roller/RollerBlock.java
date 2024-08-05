package com.simibubi.create.content.contraptions.actors.roller;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.actors.AttachedActorBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PoleHelper;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RollerBlock extends AttachedActorBlock implements IBE<RollerBlockEntity> {

    private static final int placementHelperId = PlacementHelpers.register(new RollerBlock.PlacementHelper());

    public RollerBlock(BlockBehaviour.Properties p_i48377_1_) {
        super(p_i48377_1_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.withWater((BlockState) this.m_49966_().m_61124_(f_54117_, context.m_8125_().getOpposite()), context);
    }

    @Override
    public Class<RollerBlockEntity> getBlockEntityClass() {
        return RollerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RollerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends RollerBlockEntity>) AllBlockEntityTypes.MECHANICAL_ROLLER.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.m_6402_(pLevel, pPos, pState, pPlacer, pStack);
        this.withBlockEntityDo(pLevel, pPos, RollerBlockEntity::searchForSharedValues);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        ItemStack heldItem = player.m_21120_(hand);
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (!player.m_6144_() && player.mayBuild() && placementHelper.matchesItem(heldItem)) {
            placementHelper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    private static class PlacementHelper extends PoleHelper<Direction> {

        public PlacementHelper() {
            super(AllBlocks.MECHANICAL_ROLLER::has, state -> ((Direction) state.m_61143_(HorizontalDirectionalBlock.FACING)).getClockWise().getAxis(), HorizontalDirectionalBlock.FACING);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.MECHANICAL_ROLLER::isIn;
        }
    }
}