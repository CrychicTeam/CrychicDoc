package com.simibubi.create.content.trains.track;

import com.mojang.blaze3d.platform.Window;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class TrackPlacementOverlay implements IGuiOverlay {

    public static final TrackPlacementOverlay INSTANCE = new TrackPlacementOverlay();

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            if (TrackPlacement.hoveringPos != null) {
                if (TrackPlacement.cached != null && TrackPlacement.cached.curve != null && TrackPlacement.cached.valid) {
                    if (TrackPlacement.extraTipWarmup >= 4) {
                        if (ObfuscationReflectionHelper.getPrivateValue(Gui.class, gui, "f_92993_") instanceof Integer toolHighlightTimer && toolHighlightTimer > 0) {
                            return;
                        }
                        boolean active = mc.options.keySprint.isDown();
                        MutableComponent text = Lang.translateDirect("track.hold_for_smooth_curve", Components.keybind("key.sprint").withStyle(active ? ChatFormatting.WHITE : ChatFormatting.GRAY));
                        Window window = mc.getWindow();
                        int x = (window.getGuiScaledWidth() - gui.m_93082_().width(text)) / 2;
                        int y = window.getGuiScaledHeight() - 61;
                        Color color = new Color(4905802).setAlpha(Mth.clamp((float) (TrackPlacement.extraTipWarmup - 4) / 3.0F, 0.1F, 1.0F));
                        graphics.drawString(gui.m_93082_(), text, x, y, color.getRGB(), false);
                    }
                }
            }
        }
    }
}