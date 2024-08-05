package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiBagBase;
import com.mna.gui.containers.item.ContainerSpellBook;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiSpellBook extends GuiBagBase<ContainerSpellBook> {

    public GuiSpellBook(ContainerSpellBook book, Inventory inv, Component comp) {
        super(inv, book);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        float textScaleFactor = 0.5F;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(textScaleFactor, textScaleFactor, textScaleFactor);
        for (int i = 0; i < 8; i++) {
            if (((ContainerSpellBook) this.f_97732_).m_38853_(i).hasItem()) {
                pGuiGraphics.drawString(this.f_96547_, ((ContainerSpellBook) this.f_97732_).m_38853_(i).getItem().getHoverName().getString(), (int) (36.0F / textScaleFactor), (int) ((float) (8 + 18 * i) / textScaleFactor), 4210752, false);
            }
        }
        pGuiGraphics.pose().popPose();
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.Items.SPELL_BOOK;
    }

    @Override
    public String name() {
        return "Spell Book";
    }

    @Override
    public void onClose() {
        ((ContainerSpellBook) this.f_97732_).onClose();
        super.m_7379_();
    }
}