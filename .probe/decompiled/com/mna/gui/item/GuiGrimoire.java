package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.item.ContainerGrimoire;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiGrimoire extends GuiJEIDisable<ContainerGrimoire> {

    public GuiGrimoire(ContainerGrimoire book, Inventory inv, Component comp) {
        super(book, inv, Component.literal(""));
        this.f_97726_ = 256;
        this.f_97727_ = 256;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        float textScaleFactor = 0.5F;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(textScaleFactor, textScaleFactor, textScaleFactor);
        for (int i = 0; i < 16; i++) {
            if (((ContainerGrimoire) this.f_97732_).m_38853_(i).hasItem()) {
                float x = (float) (i < 8 ? 36 : 151) / textScaleFactor;
                float y = (float) (i < 8 ? 8 + 18 * i : 8 + 18 * (i - 8)) / textScaleFactor;
                pGuiGraphics.drawString(this.f_96547_, ((ContainerGrimoire) this.f_97732_).m_38853_(i).getItem().getHoverName().getString(), (int) x, (int) y, 4210752, false);
            }
        }
        pGuiGraphics.pose().popPose();
    }

    public ResourceLocation texture() {
        return GuiTextures.Items.GRIMOIRE;
    }

    public int rows() {
        return 8;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, this.f_97726_, this.f_97727_);
    }
}