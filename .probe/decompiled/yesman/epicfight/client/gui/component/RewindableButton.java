package yesman.epicfight.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RewindableButton extends Button {

    protected final Button.OnPress onRewindPress;

    public RewindableButton(int x, int y, int width, int height, Component title, Button.OnPress pressedAction, Button.OnPress rewindPressedAction) {
        super(x, y, width, height, title, pressedAction, Button.DEFAULT_NARRATION);
        this.onRewindPress = rewindPressedAction;
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    public void onClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            super.m_5716_(mouseX, mouseY);
        } else {
            this.onRewindPress.onPress(this);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.f_93623_ && this.f_93624_) {
            if (this.isValidClickButton(button)) {
                boolean flag = this.m_93680_(mouseX, mouseY);
                if (flag) {
                    this.m_7435_(Minecraft.getInstance().getSoundManager());
                    this.onClick(mouseX, mouseY, button);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}