package net.zanckor.questapi.example.server.eventhandler.questevent;

import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class CollectEvent {

    @SubscribeEvent
    public static void CollectPickUpQuest(PlayerEvent.ItemPickupEvent e) throws IOException {
        if (e.getEntity() instanceof ServerPlayer && !e.getEntity().m_9236_().isClientSide) {
            runGoalHandler((ServerPlayer) e.getEntity());
        }
    }

    @SubscribeEvent
    public static void CollectCraftQuest(PlayerEvent.ItemCraftedEvent e) throws IOException {
        if (e.getEntity() instanceof ServerPlayer && !e.getEntity().m_9236_().isClientSide) {
            runGoalHandler((ServerPlayer) e.getEntity());
        }
    }

    @SubscribeEvent
    public static void CollectCraftQuest(PlayerEvent.ItemSmeltedEvent e) throws IOException {
        if (e.getEntity() instanceof ServerPlayer && !e.getEntity().m_9236_().isClientSide) {
            runGoalHandler((ServerPlayer) e.getEntity());
        }
    }

    public static void runGoalHandler(ServerPlayer player) throws IOException {
        ServerHandler.questHandler(EnumGoalType.COLLECT, player, null);
        ServerHandler.questHandler(EnumGoalType.COLLECT_WITH_NBT, player, null);
    }
}