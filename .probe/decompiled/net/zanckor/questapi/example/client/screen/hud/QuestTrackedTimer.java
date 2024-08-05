package net.zanckor.questapi.example.client.screen.hud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.util.Timer;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE, value = { Dist.CLIENT })
public class QuestTrackedTimer {

    @SubscribeEvent
    public static void tickEvent(TickEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && !ClientHandler.trackedQuestList.isEmpty() && e.phase.equals(TickEvent.Phase.START)) {
            ClientHandler.trackedQuestList.forEach(quest -> {
                if (mc.player.f_19797_ % 20 == 0 && quest.hasTimeLimit() && Timer.existsTimer(mc.player.m_20148_(), "TIMER_QUEST" + quest.getId()) && quest.getTimeLimitInSeconds() > 0) {
                    quest.setTimeLimitInSeconds((int) Timer.remainingTime(mc.player.m_20148_(), "TIMER_QUEST" + quest.getId()) / 1000);
                }
            });
        }
    }
}