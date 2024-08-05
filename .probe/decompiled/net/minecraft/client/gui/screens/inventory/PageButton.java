package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.sounds.SoundEvents;

public class PageButton extends Button {

    private final boolean isForward;

    private final boolean playTurnSound;

    public PageButton(int int0, int int1, boolean boolean2, Button.OnPress buttonOnPress3, boolean boolean4) {
        super(int0, int1, 23, 13, CommonComponents.EMPTY, buttonOnPress3, f_252438_);
        this.isForward = boolean2;
        this.playTurnSound = boolean4;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = 0;
        int $$5 = 192;
        if (this.m_198029_()) {
            $$4 += 23;
        }
        if (!this.isForward) {
            $$5 += 13;
        }
        guiGraphics0.blit(BookViewScreen.BOOK_LOCATION, this.m_252754_(), this.m_252907_(), $$4, $$5, 23, 13);
    }

    @Override
    public void playDownSound(SoundManager soundManager0) {
        if (this.playTurnSound) {
            soundManager0.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
}