package club.iananderson.seasonhud.client.minimaps;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.impl.seasons.CurrentSeason;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import pepjebs.mapatlases.MapAtlasesMod;
import pepjebs.mapatlases.client.Anchoring;
import pepjebs.mapatlases.client.MapAtlasesClient;
import pepjebs.mapatlases.config.MapAtlasesClientConfig;

public class MapAtlases implements IGuiOverlay {

    protected final int BG_SIZE = 64;

    private static void drawStringWithLighterShadow(GuiGraphics context, Font font, MutableComponent text, int x, int y) {
        context.drawString(font, text, x + 1, y + 1, 5855577, false);
        context.drawString(font, text, x, y, 14737632, false);
    }

    public static void drawScaledComponent(GuiGraphics context, Font font, int x, int y, MutableComponent text, float textScaling, int maxWidth, int targetWidth) {
        PoseStack pose = context.pose();
        float textWidth = (float) font.width(text);
        float scale = Math.min(1.0F, (float) maxWidth * textScaling / textWidth);
        scale *= textScaling;
        float centerX = (float) x + (float) targetWidth / 2.0F;
        pose.pushPose();
        pose.translate(centerX, (float) (y + 4), 5.0F);
        pose.scale(scale, scale, 1.0F);
        pose.translate(-textWidth / 2.0F, -4.0F, 0.0F);
        drawStringWithLighterShadow(context, font, text, 0, 0);
        pose.popPose();
    }

    public static void drawMapComponentSeason(GuiGraphics poseStack, Font font, int x, int y, int targetWidth, float textScaling) {
        if (CurrentMinimap.minimapLoaded("map_atlases")) {
            MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", ((Component) CurrentSeason.getSeasonHudName().get(0)).copy().withStyle(Common.SEASON_STYLE), ((Component) CurrentSeason.getSeasonHudName().get(1)).copy());
            float globalScale = (float) ((Double) MapAtlasesClientConfig.miniMapScale.get()).doubleValue();
            drawScaledComponent(poseStack, font, x, y, seasonCombined, textScaling / globalScale, targetWidth, (int) ((float) targetWidth / globalScale));
        }
    }

    public static boolean shouldDraw(Minecraft mc) {
        if (!CurrentMinimap.minimapLoaded("map_atlases")) {
            return false;
        } else if (mc.level == null || mc.player == null) {
            return false;
        } else if (mc.options.renderDebug) {
            return false;
        } else if (!(Boolean) MapAtlasesClientConfig.drawMiniMapHUD.get()) {
            return false;
        } else {
            return !MapAtlasesClientConfig.hideWhenInHand.get() || !mc.player.m_21205_().is((Item) MapAtlasesMod.MAP_ATLAS.get()) && !mc.player.m_21206_().is((Item) MapAtlasesMod.MAP_ATLAS.get()) ? !MapAtlasesClient.getCurrentActiveAtlas().isEmpty() : false;
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics seasonStack, float partialTick, int screenWidth, int screenHeight) {
        if (CurrentMinimap.minimapLoaded("map_atlases") && shouldDraw(SeasonHUDClient.mc)) {
            float textScaling = (float) ((Double) MapAtlasesClientConfig.minimapCoordsAndBiomeScale.get()).doubleValue();
            float textHeightOffset = 2.0F;
            float globalScale = (float) ((Double) MapAtlasesClientConfig.miniMapScale.get()).doubleValue();
            int actualBgSize = (int) (64.0F * globalScale);
            LocalPlayer player = SeasonHUDClient.mc.player;
            seasonStack.pose().pushPose();
            seasonStack.pose().scale(globalScale, globalScale, 1.0F);
            Anchoring anchorLocation = (Anchoring) MapAtlasesClientConfig.miniMapAnchoring.get();
            int offset = 5;
            int x = anchorLocation.isLeft ? offset : (int) ((float) screenWidth / globalScale) - (64 + offset);
            int y = anchorLocation.isUp ? offset : (int) ((float) screenHeight / globalScale) - (64 + offset);
            x += (int) ((float) ((Integer) MapAtlasesClientConfig.miniMapHorizontalOffset.get()).intValue() / globalScale);
            y += (int) ((float) ((Integer) MapAtlasesClientConfig.miniMapVerticalOffset.get()).intValue() / globalScale);
            if (anchorLocation.isUp && !anchorLocation.isLeft) {
                boolean hasBeneficial = false;
                boolean hasNegative = false;
                for (MobEffectInstance e : player.m_21220_()) {
                    MobEffect effect = e.getEffect();
                    if (effect.isBeneficial()) {
                        hasBeneficial = true;
                    } else {
                        hasNegative = true;
                    }
                }
                int offsetForEffects = (Integer) MapAtlasesClientConfig.activePotionVerticalOffset.get();
                if (hasNegative && y < 2 * offsetForEffects) {
                    y += 2 * offsetForEffects - y;
                } else if (hasBeneficial && y < offsetForEffects) {
                    y += offsetForEffects - y;
                }
            }
            Font font = SeasonHUDClient.mc.font;
            if (Config.enableMod.get()) {
                if ((Boolean) MapAtlasesClientConfig.drawMinimapCoords.get()) {
                    textHeightOffset += 10.0F * textScaling;
                }
                if ((Boolean) MapAtlasesClientConfig.drawMinimapChunkCoords.get()) {
                    textHeightOffset += 10.0F * textScaling;
                }
                if ((Boolean) MapAtlasesClientConfig.drawMinimapBiome.get()) {
                    textHeightOffset += 10.0F * textScaling;
                }
                drawMapComponentSeason(seasonStack, font, x, (int) ((float) (y + 64) + textHeightOffset / globalScale), actualBgSize, textScaling);
                seasonStack.pose().popPose();
            }
        }
    }
}