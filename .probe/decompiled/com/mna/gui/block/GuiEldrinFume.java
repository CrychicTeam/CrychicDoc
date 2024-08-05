package com.mna.gui.block;

import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerEldrinFume;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiEldrinFume extends SimpleWizardLabDeskGui<ContainerEldrinFume> {

    public GuiEldrinFume(ContainerEldrinFume container, Inventory inv, Component comp) {
        super(container, inv, Component.literal(""));
        this.f_97726_ = 176;
        this.f_97727_ = 158;
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.WizardLab.ELDRIN_FUME;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, this.f_97726_, this.f_97727_);
        float motePct = ((ContainerEldrinFume) this.f_97732_).getMotePctRemaining();
        pGuiGraphics.blit(this.texture(), this.f_97735_ + 58, this.f_97736_ + 8, 196, 0, (int) (60.0F * motePct), 2);
        float pct = ((ContainerEldrinFume) this.f_97732_).getFuelPctRemaining();
        int progressHeight = 16 - (int) (16.0F * pct);
        pGuiGraphics.blit(this.texture(), this.f_97735_ + 79, this.f_97736_ + 33 + 16 - progressHeight, 238, 18 - progressHeight, 20, progressHeight);
        ItemStack affStack = (ItemStack) GuiTextures.affinityIcons.get(((ContainerEldrinFume) this.f_97732_).getGeneratingAffinity());
        if (!affStack.isEmpty()) {
            pGuiGraphics.renderItem(affStack, this.f_97735_ + 102, this.f_97736_ + 33);
        }
    }

    @Override
    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(63, 36);
    }

    @Override
    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(246, 18);
    }

    @Override
    protected Pair<Integer, Integer> goButtonSize() {
        return new Pair(10, 10);
    }

    @Override
    protected int goButtonHoverOffset() {
        return 0;
    }
}