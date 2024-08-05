package journeymap.client.ui.fullscreen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;

public class MapChat extends ChatScreen {

    protected boolean hidden = false;

    protected int cursorCounter;

    public MapChat(String defaultText, boolean hidden) {
        super(defaultText);
        this.hidden = hidden;
    }

    @Override
    public void removed() {
        super.removed();
        this.hidden = true;
    }

    public void close() {
        this.removed();
    }

    @Override
    public void tick() {
        if (!this.hidden) {
            super.tick();
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (this.hidden) {
            return false;
        } else if (keyCode == 256) {
            this.close();
            return true;
        } else if (keyCode != 257 && keyCode != 335) {
            return super.m_5534_(typedChar, keyCode);
        } else {
            String s = this.f_95573_.getValue().trim();
            if (!s.isEmpty()) {
                this.m_241797_(s, true);
            }
            this.f_95573_.setValue("");
            Minecraft.getInstance().gui.getChat().resetChatScroll();
            return true;
        }
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        if (key != 257 && key != 335) {
            return super.keyPressed(key, value, modifier);
        } else {
            String s = this.f_95573_.getValue().trim();
            if (!s.isEmpty()) {
                this.m_241797_(s, true);
            }
            this.f_95573_.setValue("");
            Minecraft.getInstance().gui.getChat().resetChatScroll();
            this.close();
            return true;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.hidden ? false : super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0F, (float) this.f_96544_ - 47.5F, 0.0F);
        if (this.f_96541_ != null && this.f_96541_.gui != null) {
            this.f_96541_.gui.getChat().render(graphics, mouseX, mouseY, this.hidden ? this.f_96541_.gui.getGuiTicks() : this.cursorCounter++);
        }
        poseStack.popPose();
        if (!this.hidden) {
            super.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setText(String defaultText) {
        this.f_95573_.setValue(defaultText);
    }
}