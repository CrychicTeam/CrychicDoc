package club.iananderson.seasonhud.client.minimaps;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.impl.seasons.CurrentSeason;
import club.iananderson.seasonhud.platform.Services;
import journeymap.client.JourneymapClient;
import journeymap.client.io.ThemeLoader;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.minimap.DisplayVars;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeLabelSource;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class JourneyMap implements IGuiOverlay {

    private static String getSeason() {
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", ((Component) CurrentSeason.getSeasonHudName().get(0)).copy().withStyle(Common.SEASON_STYLE), ((Component) CurrentSeason.getSeasonHudName().get(1)).copy());
        return seasonCombined.getString();
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics seasonStack, float partialTick, int scaledWidth, int scaledHeight) {
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", ((Component) CurrentSeason.getSeasonHudName().get(0)).copy().withStyle(Common.SEASON_STYLE), ((Component) CurrentSeason.getSeasonHudName().get(1)).copy());
        if (Services.PLATFORM.isModLoaded("journeymap") && !Config.enableMod.get()) {
            ThemeLabelSource.create("seasonhud", "menu.seasonhud.infodisplay.season", 1000L, 1L, JourneyMap::getSeason);
        }
        if (CurrentMinimap.minimapLoaded("journeymap")) {
            DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
            JourneymapClient jm = JourneymapClient.getInstance();
            Font fontRenderer = SeasonHUDClient.mc.font;
            float guiScale = (float) SeasonHUDClient.mc.getWindow().getGuiScale();
            double screenWidth = (double) SeasonHUDClient.mc.getWindow().getWidth();
            double screenHeight = (double) SeasonHUDClient.mc.getWindow().getHeight();
            int minimapHeight = vars.minimapHeight;
            int minimapWidth = vars.minimapWidth;
            float fontScale = jm.getActiveMiniMapProperties().fontScale.get();
            int halfWidth = minimapWidth / 2;
            Theme.LabelSpec currentTheme = ThemeLoader.getCurrentTheme().minimap.square.labelBottom;
            int labelColor = currentTheme.background.getColor();
            int textColor = currentTheme.foreground.getColor();
            float labelAlpha = jm.getActiveMiniMapProperties().infoSlotAlpha.get();
            float textAlpha = currentTheme.foreground.alpha;
            boolean fontShadow = currentTheme.shadow;
            int labelHeight = (int) ((float) (DrawUtil.getLabelHeight(fontRenderer, fontShadow) + currentTheme.margin) * fontScale);
            int topLabelHeight = vars.getInfoLabelAreaHeight(fontRenderer, currentTheme, (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info1Label.get()), (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info2Label.get()));
            int bottomLabelHeight = vars.getInfoLabelAreaHeight(fontRenderer, currentTheme, (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info3Label.get()), (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info4Label.get()));
            int margin = ThemeLoader.getCurrentTheme().minimap.square.margin;
            double textureX = (double) vars.textureX;
            double textureY = (double) vars.textureY;
            int startX = (int) (textureX + (double) halfWidth);
            int startY = (int) (textureY + (double) (Config.journeyMapAboveMap.get() ? -margin - labelHeight : minimapHeight + margin));
            if (CurrentMinimap.shouldDrawMinimapHud()) {
                int labelY = startY + (Config.journeyMapAboveMap.get() ? -topLabelHeight : bottomLabelHeight);
                if (Config.journeyMapMacOS.get()) {
                    screenWidth /= 2.0;
                    screenHeight /= 2.0;
                }
                seasonStack.pose().pushPose();
                seasonStack.pose().scale(1.0F / fontScale, 1.0F / fontScale, 0.0F);
                DrawUtil.sizeDisplay(seasonStack.pose(), screenWidth, screenHeight);
                seasonStack.pose().popPose();
                DrawUtil.drawBatchLabel(seasonStack.pose(), seasonCombined, seasonStack.bufferSource(), (double) startX, (double) labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, labelColor, labelAlpha, textColor, textAlpha, (double) fontScale, fontShadow);
                seasonStack.bufferSource().endBatch();
                DrawUtil.sizeDisplay(seasonStack.pose(), (double) scaledWidth, (double) scaledHeight);
            }
        }
    }
}