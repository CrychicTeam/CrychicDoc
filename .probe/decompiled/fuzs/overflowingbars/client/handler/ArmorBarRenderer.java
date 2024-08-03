package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ArmorBarRenderer {

    public static void renderArmorBar(GuiGraphics guiGraphics, int posX, int posY, Player player, ProfilerFiller profiler, boolean unmodified) {
        profiler.push("armor");
        ClientConfig.ArmorRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).armor;
        int armorPoints = player.m_21230_();
        renderArmorBar(guiGraphics, posX, posY, 18, armorPoints, true, unmodified, config);
        profiler.pop();
    }

    public static void renderToughnessBar(GuiGraphics guiGraphics, int posX, int posY, Player player, ProfilerFiller profiler, boolean left, boolean unmodified) {
        profiler.push("toughness");
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        int armorPoints = Mth.floor(player.m_21133_(Attributes.ARMOR_TOUGHNESS));
        renderArmorBar(guiGraphics, posX, posY, left ? 9 : 0, armorPoints, left, unmodified, config);
        profiler.pop();
    }

    public static void renderArmorBar(GuiGraphics guiGraphics, int posX, int posY, int vOffset, int armorPoints, boolean left, boolean unmodified, ClientConfig.ArmorRowConfig config) {
        if (armorPoints > 0) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            boolean inverse = !unmodified && config.inverseColoring;
            boolean skip = !unmodified && config.skipEmptyArmorPoints;
            int lastRowArmorPoints = 0;
            if (!unmodified && (config.colorizeFirstRow || armorPoints > 20)) {
                lastRowArmorPoints = (armorPoints - 1) % 20 + 1;
            }
            for (int currentArmorPoint = 0; currentArmorPoint < 10; currentArmorPoint++) {
                int startX = posX + (left ? currentArmorPoint * 8 : -currentArmorPoint * 8 - 9);
                if (currentArmorPoint * 2 + 1 < lastRowArmorPoints) {
                    guiGraphics.blit(BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION, startX, posY, inverse ? 18.0F : 36.0F, (float) vOffset, 9, 9, 256, 256);
                } else if (currentArmorPoint * 2 + 1 == lastRowArmorPoints) {
                    if (armorPoints > 20) {
                        guiGraphics.blit(BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION, startX, posY, inverse ? 54.0F : 27.0F, (float) vOffset, 9, 9, 256, 256);
                    } else {
                        guiGraphics.blit(BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION, startX, posY, inverse ? 9.0F : 45.0F, (float) vOffset, 9, 9, 256, 256);
                    }
                } else if (currentArmorPoint * 2 + 1 < armorPoints) {
                    guiGraphics.blit(BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION, startX, posY, inverse ? 36.0F : 18.0F, (float) vOffset, 9, 9, 256, 256);
                } else if (currentArmorPoint * 2 + 1 == armorPoints) {
                    guiGraphics.blit(BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION, startX, posY, inverse ? 45.0F : 9.0F, (float) vOffset, 9, 9, 256, 256);
                } else if (!skip && currentArmorPoint * 2 + 1 > armorPoints) {
                    guiGraphics.blit(BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION, startX, posY, 0.0F, (float) vOffset, 9, 9, 256, 256);
                }
            }
        }
    }
}