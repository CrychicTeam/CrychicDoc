package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiButton;

public class CustomGuiButton extends Button implements IGuiComponent {

    protected GuiCustom parent;

    private CustomGuiTexturedRect background;

    public CustomGuiButtonWrapper component;

    protected boolean hovered;

    private int colour = 16777215;

    protected Button.OnPress onPress;

    public int id;

    public CustomGuiButton(GuiCustom parent, CustomGuiButtonWrapper component) {
        super(component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), Component.translatable(component.getLabel()), btn -> {
        }, null);
        this.onPress = button -> {
            if (!component.disablePackets) {
                Packets.sendServer(new SPacketCustomGuiButton(component.getUniqueID()));
            } else {
                component.onPress(parent.guiWrapper);
            }
        };
        this.parent = parent;
        this.component = component;
        this.init();
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    public void init() {
        this.id = this.component.getID();
        this.m_252865_(this.component.getPosX());
        this.m_253211_(this.component.getPosY());
        this.m_93674_(this.component.getWidth());
        this.setHeight(this.component.getHeight());
        this.background = new CustomGuiTexturedRect(this.parent, this.component.getTextureRect());
        this.m_93666_(Component.translatable(this.component.getLabel()));
        this.f_93623_ = this.component.getEnabled() && this.component.getVisible();
        this.f_93624_ = this.component.getVisible();
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        return false;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        if (this.f_93624_) {
            matrixStack.pushPose();
            matrixStack.translate((float) this.m_252754_(), (float) this.m_252907_(), 0.0F);
            Minecraft mc = Minecraft.getInstance();
            this.hovered = this.isHovered(mouseX, mouseY);
            int i = !this.f_93623_ ? 0 : (this.hovered ? 2 : 1);
            this.background.textureY = this.component.getTextureY() + i * this.component.getTextureHoverOffset();
            this.background.onRender(graphics, mouseX - this.m_252754_(), mouseY - this.m_252907_(), partialTicks);
            matrixStack.translate(0.0F, 0.0F, 10.0F);
            this.renderLabel(graphics);
            if (!this.component.getDisplayItem().isEmpty()) {
                int xx = (int) (((float) this.f_93618_ - 16.0F) / 2.0F);
                int yy = (int) (((float) this.f_93619_ - 16.0F) / 2.0F) + 1;
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, -90.0F);
                PoseStack posestack = RenderSystem.getModelViewStack();
                posestack.pushPose();
                posestack.translate((float) this.m_252754_(), (float) this.m_252907_(), -90.0F);
                RenderSystem.applyModelViewMatrix();
                graphics.renderItem(this.component.getDisplayItem().getMCItemStack(), xx, yy);
                graphics.renderItemDecorations(mc.font, this.component.getDisplayItem().getMCItemStack(), xx, yy);
                posestack.popPose();
                graphics.pose().popPose();
                RenderSystem.applyModelViewMatrix();
            }
            if (this.hovered && this.component.hasHoverText()) {
                this.parent.hoverText = this.component.getHoverTextList();
            }
            matrixStack.popPose();
        }
    }

    public void renderLabel(GuiGraphics graphics) {
        if (!this.component.getLabel().isEmpty()) {
            int j = 14737632;
            if (this.colour != 0) {
                j = this.colour;
            } else if (!this.f_93623_) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            Minecraft mc = Minecraft.getInstance();
            graphics.pose().translate(0.0F, 0.0F, 1.0F);
            graphics.drawCenteredString(mc.font, this.m_6035_(), this.f_93618_ / 2, (this.f_93619_ - 8) / 2, j);
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    protected int hoverState(boolean mouseOver) {
        int i = 0;
        if (mouseOver) {
            i = 1;
        }
        return i;
    }
}