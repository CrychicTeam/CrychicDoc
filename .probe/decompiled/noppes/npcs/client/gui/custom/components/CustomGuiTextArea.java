package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiTextAreaWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiTextUpdate;
import noppes.npcs.shared.client.gui.components.GuiTextArea;

public class CustomGuiTextArea extends GuiTextArea implements IGuiComponent {

    GuiCustom parent;

    CustomGuiTextFieldWrapper component;

    public CustomGuiTextArea(GuiCustom parent, CustomGuiTextAreaWrapper component) {
        super(component.getID(), component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), "");
        this.component = component;
        this.parent = parent;
        this.init();
    }

    @Override
    public void init() {
        this.id = this.component.getID();
        this.x = this.component.getPosX();
        this.y = this.component.getPosY();
        this.width = this.component.getWidth();
        this.height = this.component.getHeight();
        if (this.component.getText() != null && !this.component.getText().isEmpty()) {
            this.setText(this.component.getText());
        }
        this.enabled = this.component.getEnabled() && this.component.getVisible();
        this.visible = this.component.getVisible();
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        if (this.visible) {
            matrixStack.pushPose();
            matrixStack.translate(0.0F, 0.0F, (float) this.id);
            boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            super.render(graphics, mouseX, mouseY);
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

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        String text = this.getText();
        boolean bo = super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        if (!text.equals(this.getText())) {
            this.component.setText(this.getText());
            Packets.sendServer(new SPacketCustomGuiTextUpdate(this.component.getUniqueID(), this.getText()));
        }
        return bo;
    }

    @Override
    public boolean charTyped(char c, int i) {
        String text = this.getText();
        boolean bo = super.charTyped(c, i);
        if (!text.equals(this.getText())) {
            this.component.setText(this.getText());
            Packets.sendServer(new SPacketCustomGuiTextUpdate(this.component.getUniqueID(), this.getText()));
        }
        return bo;
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }
}