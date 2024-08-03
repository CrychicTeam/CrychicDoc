package noppes.npcs.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiAchievement implements Toast {

    private String title;

    private String subtitle;

    private int type;

    private long firstDrawTime;

    private boolean newDisplay;

    public GuiAchievement(Component titleComponent, Component subtitleComponent, int type) {
        this.title = titleComponent.getString();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getString();
        this.type = type;
    }

    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent toastGui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, f_94893_);
        graphics.blit(f_94893_, 0, 0, 0, 32 * this.type, 160, 32);
        int color1 = -256;
        int color2 = -1;
        if (this.type == 1 || this.type == 3) {
            color1 = -11534256;
            color2 = -16777216;
        }
        graphics.drawString(toastGui.getMinecraft().font, this.title, 18, 7, color1);
        graphics.drawString(toastGui.getMinecraft().font, this.subtitle, 18, 18, color2);
        return delta - this.firstDrawTime < 5000L ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }
}