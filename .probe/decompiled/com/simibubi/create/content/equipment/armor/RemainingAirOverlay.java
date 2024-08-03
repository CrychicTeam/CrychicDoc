package com.simibubi.create.content.equipment.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class RemainingAirOverlay implements IGuiOverlay {

    public static final RemainingAirOverlay INSTANCE = new RemainingAirOverlay();

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            LocalPlayer player = mc.player;
            if (player != null) {
                if (!player.m_7500_()) {
                    if (player.getPersistentData().contains("VisualBacktankAir")) {
                        if (player.m_204029_(FluidTags.WATER) || player.m_20077_()) {
                            int timeLeft = player.getPersistentData().getInt("VisualBacktankAir");
                            PoseStack poseStack = graphics.pose();
                            poseStack.pushPose();
                            ItemStack backtank = getDisplayedBacktank(player);
                            poseStack.translate((float) (width / 2 + 90), (float) (height - 53 + (backtank.getItem().isFireResistant() ? 9 : 0)), 0.0F);
                            Component text = Components.literal(StringUtil.formatTickDuration(Math.max(0, timeLeft - 1) * 20));
                            GuiGameElement.of(backtank).<RenderElement>at(0.0F, 0.0F).render(graphics);
                            int color = -1;
                            if (timeLeft < 60 && timeLeft % 2 == 0) {
                                color = Color.mixColors(-65536, color, Math.max((float) timeLeft / 60.0F, 0.25F));
                            }
                            graphics.drawString(mc.font, text, 16, 5, color);
                            poseStack.popPose();
                        }
                    }
                }
            }
        }
    }

    public static ItemStack getDisplayedBacktank(LocalPlayer player) {
        List<ItemStack> backtanks = BacktankUtil.getAllWithAir(player);
        return !backtanks.isEmpty() ? (ItemStack) backtanks.get(0) : AllItems.COPPER_BACKTANK.asStack();
    }
}