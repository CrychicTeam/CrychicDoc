package fr.frinn.custommachinery.client.screen.widget.config;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.frinn.custommachinery.common.network.CChangeSideModePacket;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class AutoIOModeButtonWidget extends AbstractWidget {

    private static final ResourceLocation TEXTURE = new ResourceLocation("custommachinery", "textures/gui/config/auto_io_mode.png");

    private static final Component INPUT = Component.translatable("custommachinery.gui.config.auto_input");

    private static final Component OUTPUT = Component.translatable("custommachinery.gui.config.auto_output");

    private static final Component ENABLED = Component.translatable("custommachinery.gui.config.enabled").withStyle(ChatFormatting.GREEN);

    private static final Component DISABLED = Component.translatable("custommachinery.gui.config.disabled").withStyle(ChatFormatting.RED);

    private final SideConfig config;

    private final boolean input;

    public AutoIOModeButtonWidget(int x, int y, SideConfig config, boolean input) {
        super(x, y, 28, 14, input ? INPUT : OUTPUT);
        this.config = config;
        this.input = input;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int color = FastColor.ARGB32.color(255, 255, 255, 255);
        if (this.input && this.config.isAutoInput()) {
            color = FastColor.ARGB32.color(255, 255, 85, 85);
        } else if (!this.input && this.config.isAutoOutput()) {
            color = FastColor.ARGB32.color(255, 85, 85, 255);
        }
        float r = (float) FastColor.ARGB32.red(color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        if (this.m_274382_()) {
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

    public List<Component> getTooltips() {
        List<Component> tooltips = new ArrayList();
        tooltips.add(this.input ? INPUT : OUTPUT);
        if ((!this.input || !this.config.isAutoInput()) && (this.input || !this.config.isAutoOutput())) {
            tooltips.add(DISABLED);
        } else {
            tooltips.add(ENABLED);
        }
        return tooltips;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.m_93680_(mouseX, mouseY) && (button == 0 || button == 1) && Minecraft.getInstance().player != null) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            new CChangeSideModePacket(Minecraft.getInstance().player.f_36096_.containerId, this.getComponentId(), (byte) (this.input ? 6 : 7), true).sendToServer();
            return true;
        } else {
            return false;
        }
    }

    private String getComponentId() {
        return this.config.getComponent().getType().getId().toString() + ":" + this.config.getComponent().getId();
    }
}