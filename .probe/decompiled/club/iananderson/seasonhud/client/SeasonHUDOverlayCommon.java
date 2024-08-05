package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.impl.seasons.Calendar;
import club.iananderson.seasonhud.impl.seasons.CurrentSeason;
import club.iananderson.seasonhud.platform.Services;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SeasonHUDOverlayCommon {

    public static void render(GuiGraphics seasonStack) {
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", ((Component) CurrentSeason.getSeasonHudName().get(0)).copy().withStyle(Common.SEASON_STYLE), ((Component) CurrentSeason.getSeasonHudName().get(1)).copy());
        float guiSize = (float) SeasonHUDClient.mc.getWindow().getGuiScale();
        int screenWidth = SeasonHUDClient.mc.getWindow().getGuiScaledWidth();
        int screenHeight = SeasonHUDClient.mc.getWindow().getGuiScaledHeight();
        int xOffset = (int) ((float) Config.hudX.get().intValue() / guiSize);
        int yOffset = (int) ((float) Config.hudY.get().intValue() / guiSize);
        int x = 1;
        int y = 1;
        int offsetDim = 2;
        int stringWidth = SeasonHUDClient.mc.font.width(seasonCombined);
        if (Config.enableMod.get() && (CurrentMinimap.noMinimapLoaded() || Services.MINIMAP.currentMinimapHidden() && Config.showDefaultWhenMinimapHidden.get() || !Config.enableMinimapIntegration.get())) {
            Location hudLoc = Config.hudLocation.get();
            switch(hudLoc) {
                case TOP_LEFT:
                    x = offsetDim;
                    y = 0;
                    break;
                case TOP_CENTER:
                    x = screenWidth / 2 - stringWidth / 2;
                    y = 0;
                    break;
                case TOP_RIGHT:
                    x = screenWidth - stringWidth - offsetDim;
                    y = 0;
                    break;
                case BOTTOM_LEFT:
                    x = offsetDim;
                    y = screenHeight - 2 * offsetDim;
                    break;
                case BOTTOM_RIGHT:
                    x = screenWidth - stringWidth - offsetDim;
                    y = screenHeight - 2 * offsetDim;
            }
            if (Common.vanillaShouldDrawHud() && !SeasonHUDClient.mc.player.m_150108_() && Calendar.calendarFound()) {
                seasonStack.pose().pushPose();
                seasonStack.pose().scale(1.0F, 1.0F, 1.0F);
                int iconX = x + xOffset;
                int iconY = y + yOffset + offsetDim;
                seasonStack.drawString(SeasonHUDClient.mc.font, seasonCombined, iconX, iconY, 16777215);
                seasonStack.pose().popPose();
            }
        }
    }
}