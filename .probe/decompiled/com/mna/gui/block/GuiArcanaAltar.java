package com.mna.gui.block;

import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerArcaneAltar;
import com.mna.gui.widgets.ReagentList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiArcanaAltar extends SimpleWizardLabDeskGui<ContainerArcaneAltar> {

    ReagentList rList;

    ReagentList mwList;

    public GuiArcanaAltar(ContainerArcaneAltar container, Inventory inv, Component comp) {
        super(container, inv, Component.literal(""));
        this.f_97726_ = 256;
        this.f_97727_ = 226;
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.WizardLab.ALTAR_OF_ARCANA;
    }

    @Override
    protected void init() {
        super.init();
        this.rList = new ReagentList(this.f_97735_, this.f_97736_, 172, 14, 71, 75, this::addTooltipLine);
        this.m_142416_(this.rList);
        this.mwList = new ReagentList(this.f_97735_, this.f_97736_, 172, 103, 71, 31, this::addTooltipLine);
        this.mwList.setFullTooltip(true);
        this.m_142416_(this.mwList);
        this.rList.reInit(((ContainerArcaneAltar) this.f_97732_).getRemainingReagents());
        this.mwList.reInit(((ContainerArcaneAltar) this.f_97732_).getRemainingManaweaves());
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        if (((ContainerArcaneAltar) this.f_97732_).shouldRecalculateMaterials()) {
            this.rList.reInit(((ContainerArcaneAltar) this.f_97732_).getRemainingReagents());
            this.mwList.reInit(((ContainerArcaneAltar) this.f_97732_).getRemainingManaweaves());
            ((ContainerArcaneAltar) this.f_97732_).materialsRecalculated();
        }
        super.renderBg(pGuiGraphics, partialTicks, mouseX, mouseY);
        if (((ContainerArcaneAltar) this.f_97732_).isActive()) {
            float pct = ((ContainerArcaneAltar) this.f_97732_).getProgress();
            pGuiGraphics.blit(GuiTextures.WizardLab.ALTAR_OF_ARCANA_EXT, this.f_97735_ + 13, this.f_97736_ + 13, 0.0F, 0.0F, 120, (int) (120.0F * pct), 128, 128);
        }
    }

    @Override
    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(66, 66);
    }

    @Override
    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(0, 242);
    }

    @Override
    protected Pair<Integer, Integer> goButtonSize() {
        return new Pair(14, 14);
    }

    @Override
    protected int goButtonHoverOffset() {
        return 0;
    }
}