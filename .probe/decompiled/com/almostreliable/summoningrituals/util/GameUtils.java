package com.almostreliable.summoningrituals.util;

import com.mojang.blaze3d.vertex.PoseStack;
import extensions.net.minecraft.world.entity.Entity.EntityExt;
import extensions.net.minecraft.world.entity.item.ItemEntity.ItemEntityExt;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class GameUtils {

    private GameUtils() {
    }

    public static void sendPlayerMessage(@Nullable Player player, String translationKey, ChatFormatting color, Object... args) {
        if (player != null) {
            player.m_213846_(Component.translatable(String.format("%s.%s.%s", "message", "summoningrituals", translationKey), args).withStyle(color));
        }
    }

    public static void dropItem(Level level, BlockPos pos, ItemStack stack, boolean offset) {
        EntityExt.spawn(ItemEntityExt.of(level, stack), level, (double) pos.m_123341_() + (offset ? 0.5 : 0.0), (double) pos.m_123342_() + (offset ? 0.5 : 0.0), (double) pos.m_123343_() + (offset ? 0.5 : 0.0));
    }

    public static void playSound(@Nullable Level level, BlockPos pos, SoundEvent sound) {
        if (level != null) {
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 0.5F, 1.0F);
        }
    }

    public static void renderCount(GuiGraphics guiGraphics, String text, int x, int y) {
        renderText(guiGraphics, text, GameUtils.ANCHOR.BOTTOM_RIGHT, x + 2, y + 2, 1.0F, 16777215);
    }

    public static void renderText(GuiGraphics guiGraphics, String text, GameUtils.ANCHOR anchor, int x, int y, float scale, int color) {
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate((float) x, (float) y, 200.0F);
        stack.scale(scale, scale, 1.0F);
        int xOffset = 0;
        int yOffset = 0;
        Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        int height = 9;
        switch(anchor) {
            case TOP_LEFT:
            default:
                break;
            case TOP_RIGHT:
                xOffset -= width;
                break;
            case BOTTOM_LEFT:
                yOffset -= height;
                break;
            case BOTTOM_RIGHT:
                xOffset -= width;
                yOffset -= height;
        }
        guiGraphics.drawString(Minecraft.getInstance().font, text, xOffset, yOffset, color, true);
        stack.popPose();
    }

    public static enum ANCHOR {

        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}