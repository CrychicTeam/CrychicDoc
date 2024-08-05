package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerSpellSpecialization;
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

public class GuiSpellSpecialization extends SimpleWizardLabDeskGui<ContainerSpellSpecialization> {

    private Component specializationTooltip;

    public GuiSpellSpecialization(ContainerSpellSpecialization container, Inventory inv, Component comp) {
        super(container, inv, Component.literal(""));
        this.f_97726_ = 256;
        this.f_97727_ = 233;
    }

    @Override
    protected void init() {
        super.init();
        SpellPartList spellSelector = new SpellPartList(false, true, this.f_97735_, this.f_97736_, 9, 29, 64, 111, s -> ((ContainerSpellSpecialization) this.f_97732_).setSpellComponent(s), c -> ((ContainerSpellSpecialization) this.f_97732_).setSpellComponent(c), m -> {
        }, tt -> this.specializationTooltip = tt);
        spellSelector._selected = ((ContainerSpellSpecialization) this.f_97732_).getSpellComponent();
        this.m_142416_(spellSelector);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.specializationTooltip = null;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.specializationTooltip != null) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.specializationTooltip, mouseX, mouseY);
        }
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.WizardLab.SPELL_SPECIALIZATION;
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
        ISpellComponent selected = ((ContainerSpellSpecialization) this.f_97732_).getSpellComponent();
        if (selected != null) {
            ManaAndArtifice.instance.proxy.getClientPlayer().getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                float mastery = r.getMastery(selected);
                for (int i = 0; i < 10 && !((float) (i + 1) * 0.05F > mastery); i++) {
                    int x;
                    int y;
                    if (i < 3) {
                        x = 208 + i % 3 * 12;
                        y = 94;
                    } else if (i < 7) {
                        x = 202 + (i - 3) % 4 * 12;
                        y = 104;
                    } else {
                        x = 208 + (i - 7) % 3 * 12;
                        y = 114;
                    }
                    pGuiGraphics.blit(GuiTextures.WizardLab.SPELL_SPECIALIZATION_EXTENSION, this.f_97735_ + x, this.f_97736_ + y, 0, 248, 8, 8);
                }
            });
        }
        pGuiGraphics.blit(GuiTextures.WizardLab.SPELL_SPECIALIZATION_EXTENSION, this.f_97735_ + 88, this.f_97736_ + 27, 0, 0, 104, 116);
        if (((ContainerSpellSpecialization) this.f_97732_).isActive()) {
            float pct = ((ContainerSpellSpecialization) this.f_97732_).getProgress();
            this.blitProgressBarLine(pGuiGraphics, 27, 0, 27, pct / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 54, 27, 21, (pct - 0.2F) / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 75, 48, 20, (pct - 0.4F) / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 95, 68, 22, (pct - 0.6F) / 0.2F);
            this.blitProgressBarLine(pGuiGraphics, 117, 90, 26, (pct - 0.8F) / 0.2F);
        }
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        float xpPct = MathUtils.clamp01(player.isCreative() ? 1.0F : (float) player.totalExperience / (float) ((ContainerSpellSpecialization) this.f_97732_).getXPCost());
        int VCoord = xpPct < 1.0F ? 223 : 218;
        pGuiGraphics.blit(GuiTextures.WizardLab.SPELL_SPECIALIZATION_EXTENSION, this.f_97735_ + 206, this.f_97736_ + 81, 0, VCoord, (int) (36.0F * xpPct), 5);
        WizardLabTile.PowerStatus powerStatus = (WizardLabTile.PowerStatus) ((ContainerSpellSpecialization) this.f_97732_).powerRequirementStatus().getOrDefault(((ContainerSpellSpecialization) this.f_97732_).getAffinity(), WizardLabTile.PowerStatus.NOT_REQUESTING);
        this.renderPowerConsumeStatusIcon(pGuiGraphics, mouseX, mouseY, this.f_97735_ + 29, this.f_97736_ + 11, ((ContainerSpellSpecialization) this.f_97732_).getAffinity(), powerStatus);
        ItemStack affStack = (ItemStack) GuiTextures.affinityIcons.get(((ContainerSpellSpecialization) this.f_97732_).getAffinity());
        if (!affStack.isEmpty()) {
            pGuiGraphics.renderItem(affStack, this.f_97735_ + 11, this.f_97736_ + 7);
        }
        if (selected != null) {
            int texSize = 32;
            pGuiGraphics.blit(selected.getGuiIcon(), this.f_97735_ + 208, this.f_97736_ + 9, 0.0F, 0.0F, 32, 32, 32, 32);
        }
    }

    private void blitProgressBarLine(GuiGraphics pGuiGraphics, int y, int v, int h, float pct) {
        int progressWidth = (int) (104.0F * MathUtils.clamp01(pct));
        pGuiGraphics.blit(GuiTextures.WizardLab.SPELL_SPECIALIZATION_EXTENSION, this.f_97735_ + 88, this.f_97736_ + y, 105, v, progressWidth, h);
    }

    @Override
    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(211, 130);
    }

    @Override
    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(11, 228);
    }

    @Override
    protected ResourceLocation goButtonTexture() {
        return GuiTextures.WizardLab.SPELL_SPECIALIZATION_EXTENSION;
    }
}