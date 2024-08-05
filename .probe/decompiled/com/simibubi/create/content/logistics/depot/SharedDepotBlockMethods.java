package com.simibubi.create.content.logistics.depot;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;

public class SharedDepotBlockMethods {

    protected static DepotBehaviour get(BlockGetter worldIn, BlockPos pos) {
        return BlockEntityBehaviour.get(worldIn, pos, DepotBehaviour.TYPE);
    }

    public static InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (ray.getDirection() != Direction.UP) {
            return InteractionResult.PASS;
        } else if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            DepotBehaviour behaviour = get(world, pos);
            if (behaviour == null) {
                return InteractionResult.PASS;
            } else if (!(Boolean) behaviour.canAcceptItems.get()) {
                return InteractionResult.SUCCESS;
            } else {
                ItemStack heldItem = player.m_21120_(hand);
                boolean wasEmptyHanded = heldItem.isEmpty();
                boolean shouldntPlaceItem = AllBlocks.MECHANICAL_ARM.isIn(heldItem);
                ItemStack mainItemStack = behaviour.getHeldItemStack();
                if (!mainItemStack.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(mainItemStack);
                    behaviour.removeHeldItem();
                    world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
                }
                ItemStackHandler outputs = behaviour.processingOutputBuffer;
                for (int i = 0; i < outputs.getSlots(); i++) {
                    player.getInventory().placeItemBackInInventory(outputs.extractItem(i, 64, false));
                }
                if (!wasEmptyHanded && !shouldntPlaceItem) {
                    TransportedItemStack transported = new TransportedItemStack(heldItem);
                    transported.insertedFrom = player.m_6350_();
                    transported.prevBeltPosition = 0.25F;
                    transported.beltPosition = 0.25F;
                    behaviour.setHeldItem(transported);
                    player.m_21008_(hand, ItemStack.EMPTY);
                    AllSoundEvents.DEPOT_SLIDE.playOnServer(world, pos);
                }
                behaviour.blockEntity.notifyUpdate();
                return InteractionResult.SUCCESS;
            }
        }
    }

    public static void onLanded(BlockGetter worldIn, Entity entityIn) {
        if (entityIn instanceof ItemEntity) {
            if (entityIn.isAlive()) {
                if (!entityIn.level().isClientSide) {
                    ItemEntity itemEntity = (ItemEntity) entityIn;
                    DirectBeltInputBehaviour inputBehaviour = BlockEntityBehaviour.get(worldIn, entityIn.blockPosition(), DirectBeltInputBehaviour.TYPE);
                    if (inputBehaviour != null) {
                        ItemStack remainder = inputBehaviour.handleInsertion(itemEntity.getItem(), Direction.DOWN, false);
                        itemEntity.setItem(remainder);
                        if (remainder.isEmpty()) {
                            itemEntity.m_146870_();
                        }
                    }
                }
            }
        }
    }

    public static int getComparatorInputOverride(BlockState blockState, Level worldIn, BlockPos pos) {
        DepotBehaviour depotBehaviour = get(worldIn, pos);
        if (depotBehaviour == null) {
            return 0;
        } else {
            float f = (float) depotBehaviour.getPresentStackSize();
            Integer max = (Integer) depotBehaviour.maxStackSize.get();
            f /= (float) (max == 0 ? 64 : max);
            return Mth.clamp(Mth.floor(f * 14.0F) + (f > 0.0F ? 1 : 0), 0, 15);
        }
    }
}