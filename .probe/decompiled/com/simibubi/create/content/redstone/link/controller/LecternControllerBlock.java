package com.simibubi.create.content.redstone.link.controller;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class LecternControllerBlock extends LecternBlock implements IBE<LecternControllerBlockEntity>, ISpecialBlockItemRequirement {

    public LecternControllerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(f_54467_, true));
    }

    @Override
    public Class<LecternControllerBlockEntity> getBlockEntityClass() {
        return LecternControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LecternControllerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends LecternControllerBlockEntity>) AllBlockEntityTypes.LECTERN_CONTROLLER.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return IBE.super.newBlockEntity(blockPos0, blockState1);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.m_6144_() && LecternControllerBlockEntity.playerInRange(player, world, pos)) {
            if (!world.isClientSide) {
                this.withBlockEntityDo(world, pos, be -> be.tryStartUsing(player));
            }
            return InteractionResult.SUCCESS;
        } else if (player.m_6144_()) {
            if (!world.isClientSide) {
                this.replaceWithLectern(state, world, pos);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            if (!world.isClientSide) {
                this.withBlockEntityDo(world, pos, be -> be.dropController(state));
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return 15;
    }

    public void replaceLectern(BlockState lecternState, Level world, BlockPos pos, ItemStack controller) {
        world.setBlockAndUpdate(pos, (BlockState) ((BlockState) this.m_49966_().m_61124_(f_54465_, (Direction) lecternState.m_61143_(f_54465_))).m_61124_(f_54466_, (Boolean) lecternState.m_61143_(f_54466_)));
        this.withBlockEntityDo(world, pos, be -> be.setController(controller));
    }

    public void replaceWithLectern(BlockState state, Level world, BlockPos pos) {
        AllSoundEvents.CONTROLLER_TAKE.playOnServer(world, pos);
        world.setBlockAndUpdate(pos, (BlockState) ((BlockState) Blocks.LECTERN.defaultBlockState().m_61124_(f_54465_, (Direction) state.m_61143_(f_54465_))).m_61124_(f_54466_, (Boolean) state.m_61143_(f_54466_)));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return Blocks.LECTERN.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        ArrayList<ItemStack> requiredItems = new ArrayList();
        requiredItems.add(new ItemStack(Blocks.LECTERN));
        requiredItems.add(new ItemStack((ItemLike) AllItems.LINKED_CONTROLLER.get()));
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, requiredItems);
    }
}