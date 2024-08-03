package com.simibubi.create.content.processing.burner;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.content.trains.schedule.ScheduleItem;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class BlazeBurnerInteractionBehaviour extends MovingInteractionBehaviour {

    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        ItemStack itemInHand = player.m_21120_(activeHand);
        if (contraptionEntity instanceof CarriageContraptionEntity carriageEntity) {
            if (activeHand == InteractionHand.OFF_HAND) {
                return false;
            } else if (carriageEntity.getContraption() instanceof CarriageContraption carriageContraption) {
                StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) carriageContraption.getBlocks().get(localPos);
                if (info != null && info.state().m_61138_(BlazeBurnerBlock.HEAT_LEVEL) && info.state().m_61143_(BlazeBurnerBlock.HEAT_LEVEL) != BlazeBurnerBlock.HeatLevel.NONE) {
                    Direction assemblyDirection = carriageContraption.getAssemblyDirection();
                    for (Direction direction : Iterate.directionsInAxis(assemblyDirection.getAxis())) {
                        if (carriageContraption.inControl(localPos, direction)) {
                            Train train = carriageEntity.getCarriage().train;
                            if (train == null) {
                                return false;
                            }
                            if (player.m_9236_().isClientSide) {
                                return true;
                            }
                            if (train.runtime.getSchedule() != null) {
                                if (train.runtime.paused && !train.runtime.completed) {
                                    train.runtime.paused = false;
                                    AllSoundEvents.CONFIRM.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                                    player.displayClientMessage(Lang.translateDirect("schedule.continued"), true);
                                    return true;
                                }
                                if (!itemInHand.isEmpty()) {
                                    AllSoundEvents.DENY.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                                    player.displayClientMessage(Lang.translateDirect("schedule.remove_with_empty_hand"), true);
                                    return true;
                                }
                                AllSoundEvents.playItemPickup(player);
                                player.displayClientMessage(Lang.translateDirect(train.runtime.isAutoSchedule ? "schedule.auto_removed_from_train" : "schedule.removed_from_train"), true);
                                player.m_21008_(activeHand, train.runtime.returnSchedule());
                                return true;
                            }
                            if (!AllItems.SCHEDULE.isIn(itemInHand)) {
                                return true;
                            }
                            Schedule schedule = ScheduleItem.getSchedule(itemInHand);
                            if (schedule == null) {
                                return false;
                            }
                            if (schedule.entries.isEmpty()) {
                                AllSoundEvents.DENY.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                                player.displayClientMessage(Lang.translateDirect("schedule.no_stops"), true);
                                return true;
                            }
                            train.runtime.setSchedule(schedule, false);
                            AllAdvancements.CONDUCTOR.awardTo(player);
                            AllSoundEvents.CONFIRM.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                            player.displayClientMessage(Lang.translateDirect("schedule.applied_to_train").withStyle(ChatFormatting.GREEN), true);
                            itemInHand.shrink(1);
                            player.m_21008_(activeHand, itemInHand.isEmpty() ? ItemStack.EMPTY : itemInHand);
                            return true;
                        }
                    }
                    player.displayClientMessage(Lang.translateDirect("schedule.non_controlling_seat"), true);
                    AllSoundEvents.DENY.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}