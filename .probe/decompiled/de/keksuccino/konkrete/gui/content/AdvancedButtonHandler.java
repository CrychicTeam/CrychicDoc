package de.keksuccino.konkrete.gui.content;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.konkrete.rendering.RenderUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AdvancedButtonHandler {

    private static AdvancedButton activeDescBtn;

    private static int garbageCheck = 0;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new AdvancedButtonHandler());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreen(ScreenEvent.Render.Post e) {
        if (activeDescBtn != null) {
            if (activeDescBtn.m_198029_() && Minecraft.getInstance() != null && Minecraft.getInstance().screen != null) {
                renderDescription(e.getGuiGraphics(), e.getMouseX(), e.getMouseY());
            }
            if (garbageCheck == 0) {
                activeDescBtn = null;
            }
            garbageCheck = 0;
        }
    }

    public static void setActiveDescriptionButton(AdvancedButton btn) {
        activeDescBtn = btn;
        garbageCheck = 1;
    }

    private static void renderDescriptionBackground(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, new Color(26, 26, 26, 250).getRGB());
    }

    private static void renderDescription(GuiGraphics graphics, int mouseX, int mouseY) {
        PoseStack matrix = graphics.pose();
        if (activeDescBtn != null && activeDescBtn.getDescription() != null) {
            int width = 10;
            int height = 10;
            for (String s : activeDescBtn.getDescription()) {
                int i = Minecraft.getInstance().font.width(s) + 10;
                if (i > width) {
                    width = i;
                }
                height += 10;
            }
            mouseX += 5;
            mouseY += 5;
            if (Minecraft.getInstance().screen.width < mouseX + width) {
                mouseX -= width + 10;
            }
            if (Minecraft.getInstance().screen.height < mouseY + height) {
                mouseY -= height + 10;
            }
            RenderUtils.setZLevelPre(matrix, 600);
            renderDescriptionBackground(graphics, mouseX, mouseY, width, height);
            RenderSystem.enableBlend();
            int i2 = 5;
            for (String s : activeDescBtn.getDescription()) {
                graphics.drawString(Minecraft.getInstance().font, s, mouseX + 5, mouseY + i2, Color.WHITE.getRGB(), true);
                i2 += 10;
            }
            RenderUtils.setZLevelPost(matrix);
            RenderSystem.disableBlend();
        }
    }
}