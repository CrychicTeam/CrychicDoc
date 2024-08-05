package club.iananderson.seasonhud.event;

import club.iananderson.seasonhud.client.KeyBindings;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.gui.screens.SeasonHUDScreen;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.MapAtlases;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class ClientEvents {

    @EventBusSubscriber(modid = "seasonhud", value = { Dist.CLIENT })
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key Event) {
            if (KeyBindings.seasonhudOptionsKeyMapping.consumeClick()) {
                SeasonHUDScreen.open();
            }
        }
    }

    @EventBusSubscriber(modid = "seasonhud", value = { Dist.CLIENT }, bus = Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            SeasonHUDOverlay HUD = new SeasonHUDOverlay();
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "seasonhud", HUD);
        }

        @SubscribeEvent
        public static void registerJourneyMapOverlay(RegisterGuiOverlaysEvent event) {
            JourneyMap HUD = new JourneyMap();
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "journeymap", HUD);
        }

        @SubscribeEvent
        public static void registerMapAtlasesOverlay(RegisterGuiOverlaysEvent event) {
            MapAtlases HUD = new MapAtlases();
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "mapatlases", HUD);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.seasonhudOptionsKeyMapping);
        }
    }
}