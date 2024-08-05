package club.iananderson.seasonhud.impl.minimaps;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.Calendar;
import club.iananderson.seasonhud.platform.Services;

public class CurrentMinimap {

    public static boolean minimapLoaded(String minimap) {
        return Services.PLATFORM.isModLoaded(minimap);
    }

    public static boolean noMinimapLoaded() {
        return !minimapLoaded("xaerominimap") && !minimapLoaded("xaerominimapfair") && !minimapLoaded("journeymap") && !minimapLoaded("ftbchunks") && !minimapLoaded("map_atlases");
    }

    public static boolean shouldDrawMinimapHud() {
        return SeasonHUDClient.mc.level != null && SeasonHUDClient.mc.player != null ? Config.enableMod.get() && Config.enableMinimapIntegration.get() && Calendar.calendarFound() && !Services.MINIMAP.hideHudInCurrentDimension() && !Services.MINIMAP.currentMinimapHidden() && Common.vanillaShouldDrawHud() && !SeasonHUDClient.mc.player.m_150108_() : false;
    }
}