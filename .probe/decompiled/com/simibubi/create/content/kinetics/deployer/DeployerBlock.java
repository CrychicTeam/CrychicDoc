package com.simibubi.create.content.kinetics.deployer;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.processing.AssemblyOperatorUseContext;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DeployerBlock extends DirectionalAxisKineticBlock implements IBE<DeployerBlockEntity> {

    private static final int placementHelperId = PlacementHelpers.register(new DeployerBlock.PlacementHelper());

    public DeployerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.DEPLOYER_INTERACTION.get((Direction) state.m_61143_(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_12PX.get((Direction) state.m_61143_(FACING));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Vec3 normal = Vec3.atLowerCornerOf(((Direction) state.m_61143_(FACING)).getNormal());
        Vec3 location = context.getClickLocation().subtract(Vec3.atCenterOf(context.getClickedPos()).subtract(normal.scale(0.5))).multiply(normal);
        if (location.length() > 0.75) {
            if (!context.getLevel().isClientSide) {
                this.withBlockEntityDo(context.getLevel(), context.getClickedPos(), DeployerBlockEntity::changeMode);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.onWrenched(state, context);
        }
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.m_6402_(worldIn, pos, state, placer, stack);
        if (placer instanceof ServerPlayer) {
            this.withBlockEntityDo(worldIn, pos, dbe -> dbe.owner = placer.m_20148_());
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.m_60713_(newState.m_60734_())) {
            this.withBlockEntityDo(worldIn, pos, DeployerBlockEntity::discardPlayer);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldByPlayer = player.m_21120_(handIn).copy();
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (!player.m_6144_() && player.mayBuild() && placementHelper.matchesItem(heldByPlayer) && placementHelper.getOffset(player, worldIn, state, pos, hit).placeInWorld(worldIn, (BlockItem) heldByPlayer.getItem(), player, handIn, hit).consumesAction()) {
            return InteractionResult.SUCCESS;
        } else if (AllItems.WRENCH.isIn(heldByPlayer)) {
            return InteractionResult.PASS;
        } else {
            Vec3 normal = Vec3.atLowerCornerOf(((Direction) state.m_61143_(FACING)).getNormal());
            Vec3 location = hit.m_82450_().subtract(Vec3.atCenterOf(pos).subtract(normal.scale(0.5))).multiply(normal);
            if (location.length() < 0.75) {
                return InteractionResult.PASS;
            } else if (worldIn.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    ItemStack heldByDeployer = be.player.m_21205_().copy();
                    if (!heldByDeployer.isEmpty() || !heldByPlayer.isEmpty()) {
                        player.m_21008_(handIn, heldByDeployer);
                        be.player.m_21008_(InteractionHand.MAIN_HAND, heldByPlayer);
                        be.sendData();
                    }
                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public Class<DeployerBlockEntity> getBlockEntityClass() {
        return DeployerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DeployerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends DeployerBlockEntity>) AllBlockEntityTypes.DEPLOYER.get();
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, world, pos, oldState, isMoving);
        this.withBlockEntityDo(world, pos, DeployerBlockEntity::redstoneUpdate);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        this.withBlockEntityDo(world, pos, DeployerBlockEntity::redstoneUpdate);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    protected Direction getFacingForPlacement(BlockPlaceContext context) {
        return context instanceof AssemblyOperatorUseContext ? Direction.DOWN : super.getFacingForPlacement(context);
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.DEPLOYER::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return AllBlocks.DEPLOYER::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), ((Direction) state.m_61143_(DirectionalKineticBlock.FACING)).getAxis(), dir -> world.getBlockState(pos.relative(dir)).m_247087_());
            return directions.isEmpty() ? PlacementOffset.fail() : PlacementOffset.success(pos.relative((Direction) directions.get(0)), s -> (BlockState) ((BlockState) s.m_61124_(DirectionalKineticBlock.FACING, (Direction) state.m_61143_(DirectionalKineticBlock.FACING))).m_61124_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE, (Boolean) state.m_61143_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE)));
        }
    }
}