package com.simibubi.create.content.trains.schedule;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ScheduleItemEntityInteraction {

    @SubscribeEvent
    public static void interactWithConductor(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity entity = event.getTarget();
        Player player = event.getEntity();
        if (player != null && entity != null) {
            if (!player.isSpectator()) {
                Entity rootVehicle = entity.getRootVehicle();
                if (rootVehicle instanceof CarriageContraptionEntity) {
                    if (entity instanceof LivingEntity living) {
                        if (!player.getCooldowns().isOnCooldown((Item) AllItems.SCHEDULE.get())) {
                            ItemStack itemStack = event.getItemStack();
                            if (itemStack.getItem() instanceof ScheduleItem si) {
                                InteractionResult result = si.handScheduleTo(itemStack, player, living, event.getHand());
                                if (result.consumesAction()) {
                                    player.getCooldowns().addCooldown((Item) AllItems.SCHEDULE.get(), 5);
                                    event.setCancellationResult(result);
                                    event.setCanceled(true);
                                    return;
                                }
                            }
                            if (event.getHand() != InteractionHand.OFF_HAND) {
                                CarriageContraptionEntity cce = (CarriageContraptionEntity) rootVehicle;
                                Contraption contraption = cce.getContraption();
                                if (contraption instanceof CarriageContraption cc) {
                                    Train train = cce.getCarriage().train;
                                    if (train != null) {
                                        if (train.runtime.getSchedule() != null) {
                                            Integer seatIndex = (Integer) contraption.getSeatMapping().get(entity.getUUID());
                                            if (seatIndex != null) {
                                                BlockPos seatPos = (BlockPos) contraption.getSeats().get(seatIndex);
                                                Couple<Boolean> directions = (Couple<Boolean>) cc.conductorSeats.get(seatPos);
                                                if (directions != null) {
                                                    boolean onServer = !event.getLevel().isClientSide;
                                                    if (train.runtime.paused && !train.runtime.completed) {
                                                        if (onServer) {
                                                            train.runtime.paused = false;
                                                            AllSoundEvents.CONFIRM.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                                                            player.displayClientMessage(Lang.translateDirect("schedule.continued"), true);
                                                        }
                                                        player.getCooldowns().addCooldown((Item) AllItems.SCHEDULE.get(), 5);
                                                        event.setCancellationResult(InteractionResult.SUCCESS);
                                                        event.setCanceled(true);
                                                    } else {
                                                        ItemStack itemInHand = player.m_21120_(event.getHand());
                                                        if (!itemInHand.isEmpty()) {
                                                            if (onServer) {
                                                                AllSoundEvents.DENY.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                                                                player.displayClientMessage(Lang.translateDirect("schedule.remove_with_empty_hand"), true);
                                                            }
                                                            event.setCancellationResult(InteractionResult.SUCCESS);
                                                            event.setCanceled(true);
                                                        } else {
                                                            if (onServer) {
                                                                AllSoundEvents.playItemPickup(player);
                                                                player.displayClientMessage(Lang.translateDirect(train.runtime.isAutoSchedule ? "schedule.auto_removed_from_train" : "schedule.removed_from_train"), true);
                                                                player.getInventory().placeItemBackInInventory(train.runtime.returnSchedule());
                                                            }
                                                            player.getCooldowns().addCooldown((Item) AllItems.SCHEDULE.get(), 5);
                                                            event.setCancellationResult(InteractionResult.SUCCESS);
                                                            event.setCanceled(true);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}