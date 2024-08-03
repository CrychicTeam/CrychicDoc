package net.zanckor.questapi;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.api.registry.ScreenRegistry;
import net.zanckor.questapi.api.screen.AbstractQuestTracked;
import net.zanckor.questapi.mod.common.config.client.ScreenConfig;

@EventBusSubscriber(modid = "questapi", bus = Bus.MOD, value = { Dist.CLIENT })
public class ClientForgeQuestAPI {

    public static KeyMapping questMenu;

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent e) {
        CommonMain.Constants.LOG.info("Registering overlay screens");
        e.registerAboveAll("quest_tracker", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            Player player = Minecraft.getInstance().player;
            if (player != null && !player.m_21224_()) {
                AbstractQuestTracked abstractQuestTracked = ScreenRegistry.getQuestTrackedScreen(ScreenConfig.QUEST_TRACKED_SCREEN.get());
                abstractQuestTracked.renderQuestTracked(guiGraphics, screenWidth, screenHeight);
            }
        });
    }

    @SubscribeEvent
    public static void keyInit(RegisterKeyMappingsEvent e) {
        questMenu = registerKey("quest_menu", 75);
        e.register(questMenu);
    }

    public static KeyMapping registerKey(String name, int keycode) {
        CommonMain.Constants.LOG.info("Registering keys");
        return new KeyMapping("key.questapi." + name, keycode, "key.categories.QuestApi");
    }
}