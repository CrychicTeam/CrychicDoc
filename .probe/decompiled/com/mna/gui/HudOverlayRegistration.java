package com.mna.gui;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.config.ClientConfigValues;
import com.mna.api.items.IShowHud;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.items.artifice.ItemSpectralElytra;
import com.mna.items.manaweaving.ItemManaweaverWand;
import com.mna.tools.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "mna", bus = Bus.MOD)
public class HudOverlayRegistration {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegistrGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("mna_overlays", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            Minecraft mc = Minecraft.getInstance();
            if (mc.player.m_21023_(EffectInit.MIST_FORM.get())) {
                HUDOverlayRenderer.instance.renderTextureOverlay(GuiTextures.Overlay.MIST_FORM, 0.8F);
            }
        });
        event.registerAboveAll("mna_main_hud", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            if (ClientConfigValues.ShowHudMode == ClientConfigValues.HudMode.AlwaysShow || shouldConditionalShow()) {
                HUDOverlayRenderer.instance.renderHUD(mStack, screenWidth, screenHeight, partialTicks);
            }
        });
        event.registerAboveAll("mna_affinity", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            if (ClientConfigValues.HudAffinity) {
                gui.setupOverlayRenderState(true, false);
                if (ClientConfigValues.ShowHudMode == ClientConfigValues.HudMode.AlwaysShow || shouldConditionalShow()) {
                    HUDOverlayRenderer.instance.renderAffinity(mStack, null, null, screenWidth, screenHeight);
                }
            }
        });
        event.registerAboveAll("mna_pinned_recipe", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            HUDOverlayRenderer.instance.renderPinnedRecipe(mStack, screenWidth, screenHeight);
            HUDOverlayRenderer.instance.renderPinnedDiagnostics(mStack, screenWidth, screenHeight);
        });
        event.registerAboveAll("mna_cantrip_timer", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            HUDOverlayRenderer.instance.renderCantripTimer(mStack, screenWidth, screenHeight, partialTicks);
        });
        event.registerAboveAll("mna_manaweave_timer", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player.m_21211_().getItem() instanceof ItemManaweaverWand && ItemManaweaverWand.getStoredPattern(mc.level, player.m_21211_()) != null) {
                IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                float ticksNeeded = (float) ItemManaweaverWand.getAutoweaveTicks(player);
                float pct = MathUtils.clamp01((float) player.m_21252_() / ticksNeeded);
                if (pct < 1.0F) {
                    int[] color = new int[] { 50, 0, 128 };
                    if (progression != null && progression.getAlliedFaction() != null) {
                        color = progression.getAlliedFaction().getManaweaveRGB();
                    }
                    HUDOverlayRenderer.instance.renderCenteredTimer(mStack, pct, 255, screenWidth, screenHeight, 50, color[0], color[1], color[2]);
                } else {
                    HUDOverlayRenderer.instance.renderCenteredTimer(mStack, pct, 255, screenWidth, screenHeight, 50, 0, 255, 0);
                }
            }
        });
        event.registerAboveAll("mna_spellbook_chord_hud", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            HUDOverlayRenderer.instance.renderSpellBookChordsHud(player, mStack);
        });
    }

    private static boolean shouldConditionalShow() {
        if (ClientConfigValues.ShowHudMode != ClientConfigValues.HudMode.ConditionalShow) {
            return false;
        } else {
            Player entityPlayer = ManaAndArtifice.instance.proxy.getClientPlayer();
            return entityPlayer == null ? false : entityPlayer.m_21205_().getItem() instanceof IShowHud || entityPlayer.m_21206_().getItem() instanceof IShowHud || entityPlayer.m_21255_() && entityPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemSpectralElytra;
        }
    }
}