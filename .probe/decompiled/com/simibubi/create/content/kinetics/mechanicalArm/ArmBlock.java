package com.simibubi.create.content.kinetics.mechanicalArm;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ArmBlock extends KineticBlock implements IBE<ArmBlockEntity>, ICogWheel {

    public static final BooleanProperty CEILING = BooleanProperty.create("ceiling");

    public ArmBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(CEILING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.m_7926_(p_206840_1_.add(CEILING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) this.m_49966_().m_61124_(CEILING, ctx.m_43719_() == Direction.DOWN);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return state.m_61143_(CEILING) ? AllShapes.MECHANICAL_ARM_CEILING : AllShapes.MECHANICAL_ARM;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        this.withBlockEntityDo(world, pos, ArmBlockEntity::redstoneUpdate);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        this.withBlockEntityDo(world, pos, ArmBlockEntity::redstoneUpdate);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<ArmBlockEntity> getBlockEntityClass() {
        return ArmBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ArmBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ArmBlockEntity>) AllBlockEntityTypes.MECHANICAL_ARM.get();
    }

    @Override
    public InteractionResult use(BlockState p_225533_1_, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        ItemStack heldItem = player.m_21120_(hand);
        if (AllItems.GOGGLES.isIn(heldItem)) {
            InteractionResult gogglesResult = this.onBlockEntityUse(world, pos, ate -> {
                if (ate.goggles) {
                    return InteractionResult.PASS;
                } else {
                    ate.goggles = true;
                    ate.notifyUpdate();
                    return InteractionResult.SUCCESS;
                }
            });
            if (gogglesResult.consumesAction()) {
                return gogglesResult;
            }
        }
        MutableBoolean success = new MutableBoolean(false);
        this.withBlockEntityDo(world, pos, be -> {
            if (!be.heldItem.isEmpty()) {
                success.setTrue();
                if (!world.isClientSide) {
                    player.getInventory().placeItemBackInInventory(be.heldItem);
                    be.heldItem = ItemStack.EMPTY;
                    be.phase = ArmBlockEntity.Phase.SEARCH_INPUTS;
                    be.m_6596_();
                    be.sendData();
                }
            }
        });
        return success.booleanValue() ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
}