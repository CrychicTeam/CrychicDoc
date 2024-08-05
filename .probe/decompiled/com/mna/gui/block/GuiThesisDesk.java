package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerThesisDesk;
import com.mna.gui.widgets.SpellPartList;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GuiThesisDesk extends SimpleWizardLabDeskGui<ContainerThesisDesk> {

    private Component thesisTooltip;

    public GuiThesisDesk(ContainerThesisDesk container, Inventory inv, Component comp) {
        super(container, inv, Component.literal(""));
        this.f_97726_ = 256;
        this.f_97727_ = 233;
    }

    @Override
    protected void init() {
        super.init();
        SpellPartList spellSelector = new SpellPartList(true, true, this.f_97735_, this.f_97736_, 9, 29, 64, 111, ((ContainerThesisDesk) this.f_97732_)::setSpellComponent, ((ContainerThesisDesk) this.f_97732_)::setSpellComponent, ((ContainerThesisDesk) this.f_97732_)::setSpellComponent, tt -> this.thesisTooltip = tt);
        spellSelector._selected = ((ContainerThesisDesk) this.f_97732_).getSpellComponent();
        this.m_142416_(spellSelector);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.thesisTooltip = null;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.thesisTooltip != null) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.thesisTooltip, mouseX, mouseY);
        }
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.WizardLab.THESIS_DESK;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
        ItemStack stack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
        pGuiGraphics.pose().translate(0.5, 0.5, 0.0);
        pGuiGraphics.renderItem(stack, 103, 23);
        pGuiGraphics.pose().popPose();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(pGuiGraphics, partialTicks, mouseX, mouseY);
        pGuiGraphics.blit(GuiTextures.WizardLab.THESIS_DESK_EXTENSION, this.f_97735_ + 88, this.f_97736_ + 27, 0, 0, 104, 116);
        if (((ContainerThesisDesk) this.f_97732_).isActive()) {
            float pct = ((ContainerThesisDesk) this.f_97732_).getProgress();
            this.blitProgressBarLine(pGuiGraphics, 27, 0, 23, pct / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 50, 23, 23, (pct - 0.2F) / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 73, 46, 20, (pct - 0.4F) / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 93, 66, 22, (pct - 0.6F) / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 115, 88, 28, (pct - 0.8F) / 0.2F);
        }
        pGuiGraphics.blit(GuiTextures.WizardLab.THESIS_DESK_EXTENSION, this.f_97735_ + 106, this.f_97736_ + 6, 238, 38, 18, 18);
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        float xpPct = MathUtils.clamp01(player.isCreative() ? 1.0F : (float) player.totalExperience / (float) ((ContainerThesisDesk) this.f_97732_).getXPCost());
        int VCoord = xpPct < 1.0F ? 5 : 0;
        pGuiGraphics.blit(GuiTextures.WizardLab.THESIS_DESK_EXTENSION, this.f_97735_ + 206, this.f_97736_ + 81, 220, VCoord, (int) (36.0F * xpPct), 5);
        WizardLabTile.PowerStatus powerStatus = (WizardLabTile.PowerStatus) ((ContainerThesisDesk) this.f_97732_).powerRequirementStatus().getOrDefault(((ContainerThesisDesk) this.f_97732_).getAffinity(), WizardLabTile.PowerStatus.NOT_REQUESTING);
        this.renderPowerConsumeStatusIcon(pGuiGraphics, mouseX, mouseY, this.f_97735_ + 29, this.f_97736_ + 11, ((ContainerThesisDesk) this.f_97732_).getAffinity(), powerStatus);
        ItemStack affStack = (ItemStack) GuiTextures.affinityIcons.get(((ContainerThesisDesk) this.f_97732_).getAffinity());
        if (!affStack.isEmpty()) {
            pGuiGraphics.renderItem(affStack, this.f_97735_ + 11, this.f_97736_ + 7);
        }
        ISpellComponent selected = ((ContainerThesisDesk) this.f_97732_).getSpellComponent();
        if (selected != null) {
            int texSize = 32;
            pGuiGraphics.blit(selected.getGuiIcon(), this.f_97735_ + 208, this.f_97736_ + 9, 0.0F, 0.0F, 32, 32, 32, 32);
        }
    }

    private void blitProgressBarLine(GuiGraphics pGuiGraphics, int y, int v, int h, float pct) {
        int progressWidth = (int) (104.0F * MathUtils.clamp01(pct));
        pGuiGraphics.blit(GuiTextures.WizardLab.THESIS_DESK_EXTENSION, this.f_97735_ + 88, this.f_97736_ + y, 105, v, progressWidth, h);
    }

    @Override
    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(199, 130);
    }

    @Override
    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(231, 10);
    }

    @Override
    protected ResourceLocation goButtonTexture() {
        return GuiTextures.WizardLab.THESIS_DESK_EXTENSION;
    }
}