package com.simibubi.create.content.kinetics.belt;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.item.BeltConnectorItem;
import com.simibubi.create.content.kinetics.belt.transport.BeltInventory;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BeltSlicer {

    public static InteractionResult useWrench(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, BeltSlicer.Feedback feedBack) {
        BeltBlockEntity controllerBE = BeltHelper.getControllerBE(world, pos);
        if (controllerBE == null) {
            return InteractionResult.PASS;
        } else if ((Boolean) state.m_61143_(BeltBlock.CASING) && hit.getDirection() != Direction.UP) {
            return InteractionResult.PASS;
        } else if (state.m_61143_(BeltBlock.PART) == BeltPart.PULLEY && hit.getDirection().getAxis() != Direction.Axis.Y) {
            return InteractionResult.PASS;
        } else {
            int beltLength = controllerBE.beltLength;
            if (beltLength == 2) {
                return InteractionResult.FAIL;
            } else {
                BlockPos beltVector = BlockPos.containing(BeltHelper.getBeltVector(state));
                BeltPart part = (BeltPart) state.m_61143_(BeltBlock.PART);
                List<BlockPos> beltChain = BeltBlock.getBeltChain(world, controllerBE.m_58899_());
                boolean creative = player.isCreative();
                if (hoveringEnd(state, hit)) {
                    if (world.isClientSide) {
                        return InteractionResult.SUCCESS;
                    } else {
                        for (BlockPos blockPos : beltChain) {
                            BeltBlockEntity belt = BeltHelper.getSegmentBE(world, blockPos);
                            if (belt != null) {
                                belt.detachKinetics();
                                belt.invalidateItemHandler();
                                belt.beltLength = 0;
                            }
                        }
                        BeltInventory inventory = controllerBE.inventory;
                        BlockPos next = part == BeltPart.END ? pos.subtract(beltVector) : pos.offset(beltVector);
                        BlockState replacedState = world.getBlockState(next);
                        BeltBlockEntity segmentBE = BeltHelper.getSegmentBE(world, next);
                        KineticBlockEntity.switchToBlockState(world, next, ProperWaterloggedBlock.withWater(world, (BlockState) state.m_61124_(BeltBlock.CASING, segmentBE != null && segmentBE.casing != BeltBlockEntity.CasingType.NONE), next));
                        world.setBlock(pos, ProperWaterloggedBlock.withWater(world, Blocks.AIR.defaultBlockState(), pos), 67);
                        world.removeBlockEntity(pos);
                        world.m_46796_(2001, pos, Block.getId(state));
                        if (!creative && AllBlocks.BELT.has(replacedState) && replacedState.m_61143_(BeltBlock.PART) == BeltPart.PULLEY) {
                            player.getInventory().placeItemBackInInventory(AllBlocks.SHAFT.asStack());
                        }
                        if (part == BeltPart.END && inventory != null) {
                            List<TransportedItemStack> toEject = new ArrayList();
                            for (TransportedItemStack transportedItemStack : inventory.getTransportedItems()) {
                                if (transportedItemStack.beltPosition > (float) (beltLength - 1)) {
                                    toEject.add(transportedItemStack);
                                }
                            }
                            toEject.forEach(inventory::eject);
                            toEject.forEach(inventory.getTransportedItems()::remove);
                        }
                        if (part == BeltPart.START && segmentBE != null && inventory != null) {
                            controllerBE.inventory = null;
                            segmentBE.inventory = null;
                            segmentBE.setController(next);
                            for (TransportedItemStack transportedItemStackx : inventory.getTransportedItems()) {
                                transportedItemStackx.beltPosition--;
                                if (transportedItemStackx.beltPosition <= 0.0F) {
                                    ItemEntity entity = new ItemEntity(world, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.6875F), (double) ((float) pos.m_123343_() + 0.5F), transportedItemStackx.stack);
                                    entity.m_20256_(Vec3.ZERO);
                                    entity.setDefaultPickUpDelay();
                                    entity.f_19864_ = true;
                                    world.m_7967_(entity);
                                } else {
                                    segmentBE.getInventory().addItem(transportedItemStackx);
                                }
                            }
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    BeltBlockEntity segmentBEx = BeltHelper.getSegmentBE(world, pos);
                    if (segmentBEx == null) {
                        return InteractionResult.PASS;
                    } else {
                        int hitSegment = segmentBEx.index;
                        Vec3 centerOf = VecHelper.getCenterOf(hit.getBlockPos());
                        Vec3 subtract = hit.m_82450_().subtract(centerOf);
                        boolean towardPositive = subtract.dot(Vec3.atLowerCornerOf(beltVector)) > 0.0;
                        BlockPos nextx = !towardPositive ? pos.subtract(beltVector) : pos.offset(beltVector);
                        if (hitSegment != 0 && (hitSegment != 1 || towardPositive)) {
                            if (hitSegment != controllerBE.beltLength - 1 && (hitSegment != controllerBE.beltLength - 2 || !towardPositive)) {
                                if (!creative) {
                                    int requiredShafts = 0;
                                    if (!segmentBEx.hasPulley()) {
                                        requiredShafts++;
                                    }
                                    BlockState other = world.getBlockState(nextx);
                                    if (AllBlocks.BELT.has(other) && other.m_61143_(BeltBlock.PART) == BeltPart.MIDDLE) {
                                        requiredShafts++;
                                    }
                                    int amountRetrieved = 0;
                                    boolean beltFound = false;
                                    int i = 0;
                                    while (true) {
                                        if (i >= player.getInventory().getContainerSize()) {
                                            if (!world.isClientSide) {
                                                player.getInventory().placeItemBackInInventory(AllBlocks.SHAFT.asStack(amountRetrieved));
                                            }
                                            return InteractionResult.FAIL;
                                        }
                                        if (amountRetrieved == requiredShafts && beltFound) {
                                            break;
                                        }
                                        ItemStack itemstack = player.getInventory().getItem(i);
                                        if (!itemstack.isEmpty()) {
                                            int count = itemstack.getCount();
                                            if (AllItems.BELT_CONNECTOR.isIn(itemstack)) {
                                                if (!world.isClientSide) {
                                                    itemstack.shrink(1);
                                                }
                                                beltFound = true;
                                            } else if (AllBlocks.SHAFT.isIn(itemstack)) {
                                                int taken = Math.min(count, requiredShafts - amountRetrieved);
                                                if (!world.isClientSide) {
                                                    if (taken == count) {
                                                        player.getInventory().setItem(i, ItemStack.EMPTY);
                                                    } else {
                                                        itemstack.shrink(taken);
                                                    }
                                                }
                                                amountRetrieved += taken;
                                            }
                                        }
                                        i++;
                                    }
                                }
                                if (!world.isClientSide) {
                                    for (BlockPos blockPosx : beltChain) {
                                        BeltBlockEntity belt = BeltHelper.getSegmentBE(world, blockPosx);
                                        if (belt != null) {
                                            belt.detachKinetics();
                                            belt.invalidateItemHandler();
                                            belt.beltLength = 0;
                                        }
                                    }
                                    BeltInventory inventoryx = controllerBE.inventory;
                                    KineticBlockEntity.switchToBlockState(world, pos, (BlockState) state.m_61124_(BeltBlock.PART, towardPositive ? BeltPart.END : BeltPart.START));
                                    KineticBlockEntity.switchToBlockState(world, nextx, (BlockState) world.getBlockState(nextx).m_61124_(BeltBlock.PART, towardPositive ? BeltPart.START : BeltPart.END));
                                    world.playSound(null, pos, SoundEvents.WOOL_HIT, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 0.5F, 2.3F);
                                    BeltBlockEntity newController = towardPositive ? BeltHelper.getSegmentBE(world, nextx) : segmentBEx;
                                    if (newController != null && inventoryx != null) {
                                        newController.inventory = null;
                                        newController.setController(newController.m_58899_());
                                        Iterator<TransportedItemStack> iterator = inventoryx.getTransportedItems().iterator();
                                        while (iterator.hasNext()) {
                                            TransportedItemStack transportedItemStackxx = (TransportedItemStack) iterator.next();
                                            float newPosition = transportedItemStackxx.beltPosition - (float) hitSegment - (float) (towardPositive ? 1 : 0);
                                            if (!(newPosition <= 0.0F)) {
                                                transportedItemStackxx.beltPosition = newPosition;
                                                iterator.remove();
                                                newController.getInventory().addItem(transportedItemStackxx);
                                            }
                                        }
                                    }
                                }
                                return InteractionResult.SUCCESS;
                            } else {
                                return InteractionResult.FAIL;
                            }
                        } else {
                            return InteractionResult.FAIL;
                        }
                    }
                }
            }
        }
    }

    public static InteractionResult useConnector(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, BeltSlicer.Feedback feedBack) {
        BeltBlockEntity controllerBE = BeltHelper.getControllerBE(world, pos);
        if (controllerBE == null) {
            return InteractionResult.PASS;
        } else {
            int beltLength = controllerBE.beltLength;
            if (beltLength == BeltConnectorItem.maxLength()) {
                return InteractionResult.FAIL;
            } else {
                BlockPos beltVector = BlockPos.containing(BeltHelper.getBeltVector(state));
                BeltPart part = (BeltPart) state.m_61143_(BeltBlock.PART);
                Direction facing = (Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING);
                List<BlockPos> beltChain = BeltBlock.getBeltChain(world, controllerBE.m_58899_());
                boolean creative = player.isCreative();
                if (!hoveringEnd(state, hit)) {
                    return InteractionResult.PASS;
                } else {
                    BlockPos next = part == BeltPart.START ? pos.subtract(beltVector) : pos.offset(beltVector);
                    BeltBlockEntity mergedController = null;
                    int mergedBeltLength = 0;
                    BlockState nextState = world.getBlockState(next);
                    if (!nextState.m_247087_()) {
                        if (!AllBlocks.BELT.has(nextState)) {
                            return InteractionResult.FAIL;
                        }
                        if (!beltStatesCompatible(state, nextState)) {
                            return InteractionResult.FAIL;
                        }
                        mergedController = BeltHelper.getControllerBE(world, next);
                        if (mergedController == null) {
                            return InteractionResult.FAIL;
                        }
                        if (mergedController.beltLength + beltLength > BeltConnectorItem.maxLength()) {
                            return InteractionResult.FAIL;
                        }
                        mergedBeltLength = mergedController.beltLength;
                        if (!world.isClientSide) {
                            boolean flipBelt = facing != nextState.m_61143_(BeltBlock.HORIZONTAL_FACING);
                            Optional<DyeColor> color = controllerBE.color;
                            for (BlockPos blockPos : BeltBlock.getBeltChain(world, mergedController.m_58899_())) {
                                BeltBlockEntity belt = BeltHelper.getSegmentBE(world, blockPos);
                                if (belt != null) {
                                    belt.detachKinetics();
                                    belt.invalidateItemHandler();
                                    belt.beltLength = 0;
                                    belt.color = color;
                                    if (flipBelt) {
                                        world.setBlock(blockPos, flipBelt(world.getBlockState(blockPos)), 67);
                                    }
                                }
                            }
                            if (flipBelt && mergedController.inventory != null) {
                                for (TransportedItemStack transportedItemStack : mergedController.inventory.getTransportedItems()) {
                                    transportedItemStack.beltPosition = (float) mergedBeltLength - transportedItemStack.beltPosition;
                                    transportedItemStack.prevBeltPosition = (float) mergedBeltLength - transportedItemStack.prevBeltPosition;
                                }
                            }
                        }
                    }
                    if (!world.isClientSide) {
                        for (BlockPos blockPosx : beltChain) {
                            BeltBlockEntity belt = BeltHelper.getSegmentBE(world, blockPosx);
                            if (belt != null) {
                                belt.detachKinetics();
                                belt.invalidateItemHandler();
                                belt.beltLength = 0;
                            }
                        }
                        BeltInventory inventory = controllerBE.inventory;
                        KineticBlockEntity.switchToBlockState(world, pos, (BlockState) state.m_61124_(BeltBlock.PART, BeltPart.MIDDLE));
                        if (mergedController == null) {
                            world.setBlock(next, ProperWaterloggedBlock.withWater(world, (BlockState) state.m_61124_(BeltBlock.CASING, false), next), 67);
                            BeltBlockEntity segmentBE = BeltHelper.getSegmentBE(world, next);
                            if (segmentBE != null) {
                                segmentBE.color = controllerBE.color;
                            }
                            world.playSound(null, pos, SoundEvents.WOOL_PLACE, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 0.5F, 1.0F);
                            if (part == BeltPart.START && segmentBE != null && inventory != null) {
                                segmentBE.setController(next);
                                for (TransportedItemStack transportedItemStack : inventory.getTransportedItems()) {
                                    transportedItemStack.beltPosition++;
                                    segmentBE.getInventory().addItem(transportedItemStack);
                                }
                            }
                        } else {
                            BeltInventory mergedInventory = mergedController.inventory;
                            world.playSound(null, pos, SoundEvents.WOOL_HIT, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 0.5F, 1.3F);
                            BeltBlockEntity segmentBEx = BeltHelper.getSegmentBE(world, next);
                            KineticBlockEntity.switchToBlockState(world, next, (BlockState) ((BlockState) state.m_61124_(BeltBlock.CASING, segmentBEx != null && segmentBEx.casing != BeltBlockEntity.CasingType.NONE)).m_61124_(BeltBlock.PART, BeltPart.MIDDLE));
                            if (!creative) {
                                player.getInventory().placeItemBackInInventory(AllBlocks.SHAFT.asStack(2));
                                player.getInventory().placeItemBackInInventory(AllItems.BELT_CONNECTOR.asStack());
                            }
                            BlockPos search = controllerBE.m_58899_();
                            for (int i = 0; i < 10000; i++) {
                                BlockState blockState = world.getBlockState(search);
                                if (!AllBlocks.BELT.has(blockState)) {
                                    break;
                                }
                                if (blockState.m_61143_(BeltBlock.PART) == BeltPart.START) {
                                    BeltBlockEntity newController = BeltHelper.getSegmentBE(world, search);
                                    if (newController != controllerBE && inventory != null) {
                                        newController.setController(search);
                                        controllerBE.inventory = null;
                                        for (TransportedItemStack transportedItemStack : inventory.getTransportedItems()) {
                                            transportedItemStack.beltPosition += (float) mergedBeltLength;
                                            newController.getInventory().addItem(transportedItemStack);
                                        }
                                    }
                                    if (newController != mergedController && mergedInventory != null) {
                                        newController.setController(search);
                                        mergedController.inventory = null;
                                        for (TransportedItemStack transportedItemStack : mergedInventory.getTransportedItems()) {
                                            if (newController == controllerBE) {
                                                transportedItemStack.beltPosition += (float) beltLength;
                                            }
                                            newController.getInventory().addItem(transportedItemStack);
                                        }
                                    }
                                    break;
                                }
                                search = search.subtract(beltVector);
                            }
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    static boolean beltStatesCompatible(BlockState state, BlockState nextState) {
        Direction facing1 = (Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING);
        BeltSlope slope1 = (BeltSlope) state.m_61143_(BeltBlock.SLOPE);
        Direction facing2 = (Direction) nextState.m_61143_(BeltBlock.HORIZONTAL_FACING);
        BeltSlope slope2 = (BeltSlope) nextState.m_61143_(BeltBlock.SLOPE);
        switch(slope1) {
            case UPWARD:
                if (slope2 == BeltSlope.DOWNWARD) {
                    return facing1 == facing2.getOpposite();
                }
                return slope2 == slope1 && facing1 == facing2;
            case DOWNWARD:
                if (slope2 == BeltSlope.UPWARD) {
                    return facing1 == facing2.getOpposite();
                }
                return slope2 == slope1 && facing1 == facing2;
            default:
                return slope2 == slope1 && facing2.getAxis() == facing1.getAxis();
        }
    }

    static BlockState flipBelt(BlockState state) {
        Direction facing = (Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING);
        BeltSlope slope = (BeltSlope) state.m_61143_(BeltBlock.SLOPE);
        BeltPart part = (BeltPart) state.m_61143_(BeltBlock.PART);
        if (slope == BeltSlope.UPWARD) {
            state = (BlockState) state.m_61124_(BeltBlock.SLOPE, BeltSlope.DOWNWARD);
        } else if (slope == BeltSlope.DOWNWARD) {
            state = (BlockState) state.m_61124_(BeltBlock.SLOPE, BeltSlope.UPWARD);
        }
        if (part == BeltPart.END) {
            state = (BlockState) state.m_61124_(BeltBlock.PART, BeltPart.START);
        } else if (part == BeltPart.START) {
            state = (BlockState) state.m_61124_(BeltBlock.PART, BeltPart.END);
        }
        return (BlockState) state.m_61124_(BeltBlock.HORIZONTAL_FACING, facing.getOpposite());
    }

    static boolean hoveringEnd(BlockState state, BlockHitResult hit) {
        BeltPart part = (BeltPart) state.m_61143_(BeltBlock.PART);
        if (part != BeltPart.MIDDLE && part != BeltPart.PULLEY) {
            Vec3 beltVector = BeltHelper.getBeltVector(state);
            Vec3 centerOf = VecHelper.getCenterOf(hit.getBlockPos());
            Vec3 subtract = hit.m_82450_().subtract(centerOf);
            return subtract.dot(beltVector) > 0.0 == (part == BeltPart.END);
        } else {
            return false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void tickHoveringInformation() {
        Minecraft mc = Minecraft.getInstance();
        HitResult target = mc.hitResult;
        if (target != null && target instanceof BlockHitResult result) {
            ClientLevel world = mc.level;
            BlockPos pos = result.getBlockPos();
            BlockState state = world.m_8055_(pos);
            ItemStack held = mc.player.m_21120_(InteractionHand.MAIN_HAND);
            ItemStack heldOffHand = mc.player.m_21120_(InteractionHand.OFF_HAND);
            if (!mc.player.isShiftKeyDown()) {
                if (AllBlocks.BELT.has(state)) {
                    BeltSlicer.Feedback feedback = new BeltSlicer.Feedback();
                    if (!AllItems.WRENCH.isIn(held) && !AllItems.WRENCH.isIn(heldOffHand)) {
                        if (!AllItems.BELT_CONNECTOR.isIn(held) && !AllItems.BELT_CONNECTOR.isIn(heldOffHand)) {
                            return;
                        }
                        useConnector(state, world, pos, mc.player, InteractionHand.MAIN_HAND, result, feedback);
                    } else {
                        useWrench(state, world, pos, mc.player, InteractionHand.MAIN_HAND, result, feedback);
                    }
                    if (feedback.langKey != null) {
                        mc.player.displayClientMessage(Lang.translateDirect(feedback.langKey).withStyle(feedback.formatting), true);
                    } else {
                        mc.player.displayClientMessage(Components.immutableEmpty(), true);
                    }
                    if (feedback.bb != null) {
                        CreateClient.OUTLINER.chaseAABB("BeltSlicer", feedback.bb).lineWidth(0.0625F).colored(feedback.color);
                    }
                }
            }
        }
    }

    public static class Feedback {

        int color = 16777215;

        AABB bb;

        String langKey;

        ChatFormatting formatting = ChatFormatting.WHITE;
    }
}