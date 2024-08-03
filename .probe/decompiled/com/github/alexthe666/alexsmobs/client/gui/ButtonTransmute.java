package com.github.alexthe666.alexsmobs.client.gui;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ButtonTransmute extends Button {

    private final Screen parent;

    public ButtonTransmute(Screen parent, int x, int y, Button.OnPress onPress) {
        super(x, y, 117, 19, CommonComponents.EMPTY, onPress, f_252438_);
        this.parent = parent;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        int color = 8453920;
        int cost = AMConfig.transmutingExperienceCost;
        if (!this.canBeTransmuted(cost)) {
            color = 16736352;
        } else if (this.f_93623_ && this.f_93622_) {
            guiGraphics.blit(GUITransmutationTable.TEXTURE, this.m_252754_(), this.m_252907_(), 0, 201, 117, 19);
            color = 13107152;
        }
        guiGraphics.pose().pushPose();
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("alexsmobs.container.transmutation_table.cost").append(" " + cost), this.m_252754_() + 21, this.m_252907_() + (this.f_93619_ - 8) / 2, color, false);
        guiGraphics.pose().popPose();
    }

    public boolean canBeTransmuted(int cost) {
        return Minecraft.getInstance().player.f_36078_ >= cost || Minecraft.getInstance().player.m_150110_().instabuild;
    }

    @Override
    public void playDownSound(SoundManager sounds) {
        if (this.canBeTransmuted(AMConfig.transmutingExperienceCost)) {
            super.m_7435_(sounds);
        }
    }

    @Override
    public void onPress() {
        if (this.canBeTransmuted(AMConfig.transmutingExperienceCost)) {
            super.onPress();
        }
        this.f_93622_ = false;
        this.m_93692_(false);
    }
}