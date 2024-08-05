package com.simibubi.create.foundation.blockEntity.behaviour;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.SidedFilteringBehaviour;
import com.simibubi.create.foundation.utility.RaycastHelper;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ValueSettingsInputHandler {

    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        if (canInteract(player)) {
            if (!AllBlocks.CLIPBOARD.isIn(player.m_21205_())) {
                if (world.getBlockEntity(pos) instanceof SmartBlockEntity sbe) {
                    if (event.getSide() == LogicalSide.CLIENT) {
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateClient.VALUE_SETTINGS_HANDLER.cancelIfWarmupAlreadyStarted(event));
                    }
                    if (!event.isCanceled()) {
                        Iterator var12 = sbe.getAllBehaviours().iterator();
                        while (true) {
                            BlockEntityBehaviour behaviour;
                            BlockHitResult ray;
                            while (true) {
                                while (true) {
                                    if (!var12.hasNext()) {
                                        return;
                                    }
                                    behaviour = (BlockEntityBehaviour) var12.next();
                                    if (behaviour instanceof ValueSettingsBehaviour valueSettingsBehaviour) {
                                        ray = RaycastHelper.rayTraceRange(world, player, 10.0);
                                        if (ray == null) {
                                            return;
                                        }
                                        if (!(behaviour instanceof SidedFilteringBehaviour)) {
                                            break;
                                        }
                                        behaviour = ((SidedFilteringBehaviour) behaviour).get(ray.getDirection());
                                        if (behaviour != null) {
                                            break;
                                        }
                                    }
                                }
                                if (valueSettingsBehaviour.isActive() && (!valueSettingsBehaviour.onlyVisibleWithWrench() || AllTags.AllItemTags.WRENCH.matches(player.m_21120_(hand)))) {
                                    if (!(valueSettingsBehaviour.getSlotPositioning() instanceof ValueBoxTransform.Sided sidedSlot)) {
                                        break;
                                    }
                                    if (sidedSlot.isSideActive(sbe.m_58900_(), ray.getDirection())) {
                                        sidedSlot.fromSide(ray.getDirection());
                                        break;
                                    }
                                }
                            }
                            boolean fakePlayer = player instanceof FakePlayer;
                            if (valueSettingsBehaviour.testHit(ray.m_82450_()) || fakePlayer) {
                                event.setCanceled(true);
                                event.setCancellationResult(InteractionResult.SUCCESS);
                                if (valueSettingsBehaviour.acceptsValueSettings() && !fakePlayer) {
                                    if (event.getSide() == LogicalSide.CLIENT) {
                                        BehaviourType<?> type = behaviour.getType();
                                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateClient.VALUE_SETTINGS_HANDLER.startInteractionWith(pos, type, hand, ray.getDirection()));
                                    }
                                    return;
                                } else {
                                    valueSettingsBehaviour.onShortInteract(player, hand, ray.getDirection());
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean canInteract(Player player) {
        return player != null && !player.isSpectator() && !player.m_6144_();
    }
}