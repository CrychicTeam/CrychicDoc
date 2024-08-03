package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonListWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiButtonList;

public class CustomGuiButtonList extends CustomGuiButton {

    private CustomGuiTexturedRect left;

    private CustomGuiTexturedRect right;

    private CustomGuiTexturedRectWrapper leftWrapper;

    private CustomGuiTexturedRectWrapper rightWrapper;

    private boolean isRight = false;

    public CustomGuiButtonList(GuiCustom parent, CustomGuiButtonListWrapper component) {
        super(parent, component);
        this.onPress = button -> {
            CustomGuiButtonList list = (CustomGuiButtonList) button;
            component.setSelected(component.getSelected() + (list.isRight ? 1 : -1));
            list.m_93666_(Component.translatable(component.getLabel()));
            this.sendPacket();
            if (!component.disablePackets) {
                Packets.sendServer(new SPacketCustomGuiButtonList(component.getUniqueID(), list.isRight));
            } else {
                component.onPress(parent.guiWrapper);
            }
        };
    }

    private void sendPacket() {
        Packets.sendServer(new SPacketCustomGuiButtonList(this.component.getUniqueID(), this.isRight));
    }

    public CustomGuiButtonList(GuiCustom parent, CustomGuiButtonListWrapper component, Button.OnPress onPress) {
        super(parent, component);
        this.component = component;
        this.onPress = onPress;
        this.init();
    }

    @Override
    public void init() {
        super.init();
        this.leftWrapper = ((CustomGuiButtonListWrapper) this.component).getLeftTexture();
        this.rightWrapper = ((CustomGuiButtonListWrapper) this.component).getRightTexture();
        this.left = new CustomGuiTexturedRect(this.parent, this.leftWrapper);
        this.right = new CustomGuiTexturedRect(this.parent, this.rightWrapper);
    }

    protected int getYImage(boolean boolean0) {
        int i = 1;
        if (!this.f_93623_) {
            i = 0;
        } else if (boolean0) {
            i = 2;
        }
        return i;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        super.onRender(graphics, mouseX, mouseY, partialTicks);
        matrixStack.pushPose();
        matrixStack.translate((float) this.m_252754_(), (float) this.m_252907_(), 10.0F);
        this.isRight = mouseX >= this.m_252754_() + this.f_93618_ / 2;
        this.left.textureY = this.leftWrapper.getTextureY() + this.getYImage(this.hovered && !this.isRight) * this.leftWrapper.getHeight();
        this.left.onRender(graphics, mouseX - this.m_252754_(), mouseY - this.m_252907_(), partialTicks);
        this.right.textureY = this.rightWrapper.getTextureY() + this.getYImage(this.hovered && this.isRight) * this.rightWrapper.getHeight();
        this.right.onRender(graphics, mouseX - this.m_252754_(), mouseY - this.m_252907_(), partialTicks);
        this.renderLabel(graphics);
        matrixStack.popPose();
    }
}