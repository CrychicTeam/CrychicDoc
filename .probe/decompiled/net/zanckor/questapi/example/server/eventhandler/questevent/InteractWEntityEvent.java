package net.zanckor.questapi.example.server.eventhandler.questevent;

import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;
import net.zanckor.questapi.util.Timer;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class InteractWEntityEvent {

    @SubscribeEvent
    public static void interactWithNPC(PlayerInteractEvent.EntityInteract e) throws IOException {
        if (e.getHand() != InteractionHand.OFF_HAND && !e.getSide().isClient() && e.getTarget() instanceof LivingEntity) {
            if (Timer.canUseWithCooldown(e.getEntity().m_20148_(), "INTERACT_EVENT_COOLDOWN", 1.0F)) {
                ServerHandler.questHandler(EnumGoalType.INTERACT_ENTITY, (ServerPlayer) e.getEntity(), (LivingEntity) e.getTarget());
                Timer.updateCooldown(e.getEntity().m_20148_(), "INTERACT_EVENT_COOLDOWN", 1.0F);
            }
        }
    }
}