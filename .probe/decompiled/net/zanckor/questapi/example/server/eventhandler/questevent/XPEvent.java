package net.zanckor.questapi.example.server.eventhandler.questevent;

import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class XPEvent {

    @SubscribeEvent
    public static void xpQuest(PlayerXpEvent e) throws IOException {
        if (e.getEntity() instanceof ServerPlayer player && !player.m_9236_().isClientSide) {
            ServerHandler.questHandler(EnumGoalType.XP, player, e.getEntity());
            return;
        }
    }
}