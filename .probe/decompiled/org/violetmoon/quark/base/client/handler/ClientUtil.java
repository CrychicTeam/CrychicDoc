package org.violetmoon.quark.base.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.violetmoon.zeta.client.config.screen.ZetaScreen;
import org.violetmoon.zeta.client.event.play.ZScreen;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;

public class ClientUtil {

    public static final ResourceLocation GENERAL_ICONS = new ResourceLocation("quark", "textures/gui/general_icons.png");

    private static int progress;

    private static final int BASIC_GUI_TEXT_COLOR = 4210752;

    @PlayEvent
    public static void onKeystroke(ZScreen.KeyPressed.Pre event) {
        String[] ids = new String[] { "-FCYE87P5L0", "mybsDDymrsc", "6a4BWpBJppI", "thpTOAS1Vgg", "ZNcBZM5SvbY", "_qJEoSa3Ie0", "RWeyOyY_puQ", "VBbeuXW8Nko", "LIDe-yTxda0", "BVVfMFS3mgc", "m5qwcYL8a0o", "UkY8HvgvBJ8", "4K4b9Z9lSwc", "tyInv6RWL0Q", "tIWpr3tHzII", "AFJPFfnzZ7w", "846cjX0ZTrk", "XEOCbFJjRw0", "GEo5bmUKFvI", "b6li05zh3Kg", "_EEo-iE5u_A", "SPYX2y4NzTU", "UDxID0_A9x4", "ZBl48MK17cI", "l6p8FDJqUj4" };
        int[] keys = new int[] { 265, 265, 264, 264, 263, 262, 263, 262, 66, 65 };
        if (event.getScreen() instanceof ZetaScreen) {
            if (keys[progress] == event.getKeyCode()) {
                progress++;
                if (progress >= keys.length) {
                    progress = 0;
                    Util.getPlatform().openUri("https://www.youtube.com/watch?v=" + ids[new Random().nextInt(ids.length)]);
                }
            } else {
                progress = 0;
            }
        }
    }

    @LoadEvent
    public static void handleQuarkConfigChange(ZConfigChanged z) {
        Minecraft mc = Minecraft.getInstance();
        mc.m_18707_(() -> {
            if (mc.hasSingleplayerServer() && mc.player != null && mc.getSingleplayerServer() != null) {
                for (int i = 0; i < 3; i++) {
                    mc.player.sendSystemMessage(Component.translatable("quark.misc.reloaded" + i).withStyle(i == 0 ? ChatFormatting.AQUA : ChatFormatting.WHITE));
                }
            }
        });
    }

    public static int getGuiTextColor(String name) {
        return getGuiTextColor(name, 4210752);
    }

    public static int getGuiTextColor(String name, int base) {
        int ret = base;
        String hex = I18n.get("quark.gui.color." + name);
        if (hex.matches("#[A-F0-9]{6}")) {
            ret = Integer.valueOf(hex.substring(1), 16);
        }
        return ret;
    }

    public static void drawChatBubble(GuiGraphics guiGraphics, int x, int y, Font font, String text, float alpha, boolean extendRight) {
        PoseStack matrix = guiGraphics.pose();
        matrix.pushPose();
        matrix.translate(0.0F, 0.0F, 200.0F);
        int w = font.width(text);
        int left = x - (extendRight ? 0 : w);
        int top = y - 8;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        if (extendRight) {
            guiGraphics.blit(GENERAL_ICONS, left, top, 227.0F, 9.0F, 6, 17, 256, 256);
            for (int i = 0; i < w; i++) {
                guiGraphics.blit(GENERAL_ICONS, left + i + 6, top, 232.0F, 9.0F, 1, 17, 256, 256);
            }
            guiGraphics.blit(GENERAL_ICONS, left + w + 5, top, 236.0F, 9.0F, 5, 17, 256, 256);
        } else {
            guiGraphics.blit(GENERAL_ICONS, left, top, 242.0F, 9.0F, 5, 17, 256, 256);
            for (int i = 0; i < w; i++) {
                guiGraphics.blit(GENERAL_ICONS, left + i + 5, top, 248.0F, 9.0F, 1, 17, 256, 256);
            }
            guiGraphics.blit(GENERAL_ICONS, left + w + 5, top, 250.0F, 9.0F, 6, 17, 256, 256);
        }
        int alphaInt = (int) (256.0F * alpha) << 24;
        guiGraphics.drawString(font, text, left + 5, top + 3, alphaInt, false);
        matrix.popPose();
    }
}