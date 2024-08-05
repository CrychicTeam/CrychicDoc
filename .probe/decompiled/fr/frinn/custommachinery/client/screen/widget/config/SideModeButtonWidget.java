package fr.frinn.custommachinery.client.screen.widget.config;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.frinn.custommachinery.common.network.CChangeSideModePacket;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class SideModeButtonWidget extends AbstractWidget {

    private static final ResourceLocation TEXTURE = new ResourceLocation("custommachinery", "textures/gui/config/side_mode.png");

    private final SideConfig config;

    private final RelativeSide side;

    public SideModeButtonWidget(int x, int y, SideConfig config, RelativeSide side) {
        super(x, y, 14, 14, side.getTranslationName());
        this.config = config;
        this.side = side;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int color = this.config.getSideMode(this.side).color();
        float r = (float) FastColor.ARGB32.red(color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        if (this.m_5953_((double) mouseX, (double) mouseY)) {
            graphics.blit(TEXTURE, this.m_252754_(), this.m_252907_(), 0.0F, 14.0F, this.f_93618_, this.f_93619_, this.f_93618_, 28);
        } else {
            graphics.blit(TEXTURE, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, 28);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.m_168802_(narrationElementOutput);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.m_93680_(mouseX, mouseY) && Minecraft.getInstance().player != null) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            if (button == 0) {
                new CChangeSideModePacket(Minecraft.getInstance().player.f_36096_.containerId, this.getComponentId(), (byte) this.side.ordinal(), true).sendToServer();
            } else {
                new CChangeSideModePacket(Minecraft.getInstance().player.f_36096_.containerId, this.getComponentId(), (byte) this.side.ordinal(), false).sendToServer();
            }
            return true;
        } else {
            return false;
        }
    }

    private String getComponentId() {
        return this.config.getComponent().getType().getId().toString() + ":" + this.config.getComponent().getId();
    }
}