package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;

public class CustomGuiLabel extends AbstractWidget implements IGuiComponent {

    private CustomGuiLabelWrapper component;

    private int id;

    private GuiCustom parent;

    public CustomGuiLabel(GuiCustom parent, CustomGuiLabelWrapper component) {
        super(component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), Component.translatable(component.getText()));
        this.component = component;
        this.parent = parent;
        this.init();
    }

    @Override
    public void init() {
        this.id = this.component.getID();
        this.m_252865_(this.component.getPosX());
        this.m_253211_(this.component.getPosY());
        this.m_93674_(this.component.getWidth());
        this.setHeight(this.component.getHeight());
        this.f_93623_ = this.component.getEnabled() && this.component.getVisible();
        this.f_93624_ = this.component.getVisible();
        this.m_93666_(Component.translatable(this.component.getText()));
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        if (this.f_93623_) {
            matrixStack.pushPose();
            matrixStack.translate(0.0F, 0.0F, (float) this.id);
            matrixStack.scale(this.component.getScale(), this.component.getScale(), 0.0F);
            boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            if (this.component.getCentered()) {
                graphics.drawString(Minecraft.getInstance().font, this.m_6035_(), (int) ((float) this.m_252754_() + (float) (this.f_93618_ - Minecraft.getInstance().font.width(this.m_6035_())) / 2.0F), this.m_252907_(), this.component.getColor());
            } else {
                graphics.drawString(Minecraft.getInstance().font, this.m_6035_(), this.m_252754_(), this.m_252907_(), this.component.getColor());
            }
            if (hovered && this.component.hasHoverText()) {
                this.parent.hoverText = this.component.getHoverTextList();
            }
            matrixStack.popPose();
        }
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void setText(String s) {
        this.m_93666_(Component.translatable(s));
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }
}