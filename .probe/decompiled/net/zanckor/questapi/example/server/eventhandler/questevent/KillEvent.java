package net.zanckor.questapi.example.server.eventhandler.questevent;

import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class KillEvent {

    @SubscribeEvent
    public static void killQuest(LivingDeathEvent e) throws IOException {
        if (e.getSource().getEntity() instanceof ServerPlayer player && !player.m_9236_().isClientSide) {
            ServerHandler.questHandler(EnumGoalType.KILL, player, e.getEntity());
            return;
        }
    }
}