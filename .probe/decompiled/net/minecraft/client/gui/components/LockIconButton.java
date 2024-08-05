package net.minecraft.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LockIconButton extends Button {

    private boolean locked;

    public LockIconButton(int int0, int int1, Button.OnPress buttonOnPress2) {
        super(int0, int1, 20, 20, Component.translatable("narrator.button.difficulty_lock"), buttonOnPress2, f_252438_);
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return CommonComponents.joinForNarration(super.createNarrationMessage(), this.isLocked() ? Component.translatable("narrator.button.difficulty_lock.locked") : Component.translatable("narrator.button.difficulty_lock.unlocked"));
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean boolean0) {
        this.locked = boolean0;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        LockIconButton.Icon $$4;
        if (!this.f_93623_) {
            $$4 = this.locked ? LockIconButton.Icon.LOCKED_DISABLED : LockIconButton.Icon.UNLOCKED_DISABLED;
        } else if (this.m_198029_()) {
            $$4 = this.locked ? LockIconButton.Icon.LOCKED_HOVER : LockIconButton.Icon.UNLOCKED_HOVER;
        } else {
            $$4 = this.locked ? LockIconButton.Icon.LOCKED : LockIconButton.Icon.UNLOCKED;
        }
        guiGraphics0.blit(Button.f_93617_, this.m_252754_(), this.m_252907_(), $$4.getX(), $$4.getY(), this.f_93618_, this.f_93619_);
    }

    static enum Icon {

        LOCKED(0, 146),
        LOCKED_HOVER(0, 166),
        LOCKED_DISABLED(0, 186),
        UNLOCKED(20, 146),
        UNLOCKED_HOVER(20, 166),
        UNLOCKED_DISABLED(20, 186);

        private final int x;

        private final int y;

        private Icon(int p_94324_, int p_94325_) {
            this.x = p_94324_;
            this.y = p_94325_;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }
    }
}