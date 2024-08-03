package com.simibubi.create.content.kinetics.belt.transport;

import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

public class BeltFunnelInteractionHandler {

    public static boolean checkForFunnels(BeltInventory beltInventory, TransportedItemStack currentItem, float nextOffset) {
        boolean beltMovementPositive = beltInventory.beltMovementPositive;
        int firstUpcomingSegment = (int) Math.floor((double) currentItem.beltPosition);
        int step = beltMovementPositive ? 1 : -1;
        firstUpcomingSegment = Mth.clamp(firstUpcomingSegment, 0, beltInventory.belt.beltLength - 1);
        for (int segment = firstUpcomingSegment; beltMovementPositive ? (float) segment <= nextOffset : (float) (segment + 1) >= nextOffset; segment += step) {
            BlockPos funnelPos = BeltHelper.getPositionForOffset(beltInventory.belt, segment).above();
            Level world = beltInventory.belt.m_58904_();
            BlockState funnelState = world.getBlockState(funnelPos);
            if (funnelState.m_60734_() instanceof BeltFunnelBlock) {
                Direction funnelFacing = (Direction) funnelState.m_61143_(BeltFunnelBlock.HORIZONTAL_FACING);
                Direction movementFacing = beltInventory.belt.getMovementFacing();
                boolean blocking = funnelFacing == movementFacing.getOpposite();
                if (funnelFacing != movementFacing && funnelState.m_61143_(BeltFunnelBlock.SHAPE) != BeltFunnelBlock.Shape.PUSHING) {
                    float funnelEntry = (float) segment + 0.5F;
                    if (funnelState.m_61143_(BeltFunnelBlock.SHAPE) == BeltFunnelBlock.Shape.EXTENDED) {
                        funnelEntry += 0.499F * (float) (beltMovementPositive ? -1 : 1);
                    }
                    boolean hasCrossed = nextOffset > funnelEntry && beltMovementPositive || nextOffset < funnelEntry && !beltMovementPositive;
                    if (!hasCrossed) {
                        return false;
                    }
                    if (blocking) {
                        currentItem.beltPosition = funnelEntry;
                    }
                    if (!world.isClientSide && !(Boolean) funnelState.m_61145_(BeltFunnelBlock.POWERED).orElse(false)) {
                        BlockEntity be = world.getBlockEntity(funnelPos);
                        if (!(be instanceof FunnelBlockEntity)) {
                            return true;
                        }
                        FunnelBlockEntity funnelBE = (FunnelBlockEntity) be;
                        InvManipulationBehaviour inserting = funnelBE.getBehaviour(InvManipulationBehaviour.TYPE);
                        FilteringBehaviour filtering = funnelBE.getBehaviour(FilteringBehaviour.TYPE);
                        if (inserting != null && (filtering == null || filtering.test(currentItem.stack))) {
                            int amountToExtract = funnelBE.getAmountToExtract();
                            ItemHelper.ExtractionCountMode modeToExtract = funnelBE.getModeToExtract();
                            ItemStack toInsert = currentItem.stack.copy();
                            if (amountToExtract > toInsert.getCount() && modeToExtract != ItemHelper.ExtractionCountMode.UPTO) {
                                if (blocking) {
                                    return true;
                                }
                            } else {
                                if (amountToExtract != -1 && modeToExtract != ItemHelper.ExtractionCountMode.UPTO) {
                                    toInsert.setCount(Math.min(amountToExtract, toInsert.getCount()));
                                    ItemStack remainder = inserting.simulate().insert(toInsert);
                                    if (!remainder.isEmpty()) {
                                        if (blocking) {
                                            return true;
                                        }
                                        continue;
                                    }
                                }
                                ItemStack remainder = inserting.insert(toInsert);
                                if (toInsert.equals(remainder, false)) {
                                    if (blocking) {
                                        return true;
                                    }
                                } else {
                                    int notFilled = currentItem.stack.getCount() - toInsert.getCount();
                                    if (!remainder.isEmpty()) {
                                        remainder.grow(notFilled);
                                    } else if (notFilled > 0) {
                                        remainder = ItemHandlerHelper.copyStackWithSize(currentItem.stack, notFilled);
                                    }
                                    funnelBE.flap(true);
                                    funnelBE.onTransfer(toInsert);
                                    currentItem.stack = remainder;
                                    beltInventory.belt.sendData();
                                    if (blocking) {
                                        return true;
                                    }
                                }
                            }
                        } else if (blocking) {
                            return true;
                        }
                    } else if (blocking) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}