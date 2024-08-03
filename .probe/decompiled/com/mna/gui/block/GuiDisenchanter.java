package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerDisenchanter;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GuiDisenchanter extends SimpleWizardLabDeskGui<ContainerDisenchanter> {

    public GuiDisenchanter(ContainerDisenchanter container, Inventory inv, Component comp) {
        super(container, inv, Component.literal(""));
        this.f_97726_ = 176;
        this.f_97727_ = 153;
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.WizardLab.DISENCHANTER;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(pGuiGraphics, partialTicks, mouseX, mouseY);
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        float xpPct = MathUtils.clamp01(player.isCreative() ? 1.0F : (float) player.totalExperience / (float) ((ContainerDisenchanter) this.f_97732_).getXPCost());
        int VCoord = xpPct < 1.0F ? 5 : 0;
        pGuiGraphics.blit(this.texture(), this.f_97735_ + 101, this.f_97736_ + 49, 220, VCoord, (int) (36.0F * xpPct), 5);
        if (((ContainerDisenchanter) this.f_97732_).isActive()) {
            float pct = ((ContainerDisenchanter) this.f_97732_).getProgress();
            pGuiGraphics.blit(this.texture(), this.f_97735_ + 83, this.f_97736_ + 25, 223, 10, 8, (int) (22.0F * pct));
        }
        pGuiGraphics.renderItem((ItemStack) GuiTextures.affinityIcons.get(Affinity.FIRE), this.f_97735_ + 39, this.f_97736_ + 7);
        pGuiGraphics.renderItem((ItemStack) GuiTextures.affinityIcons.get(Affinity.WIND), this.f_97735_ + 39, this.f_97736_ + 28);
        HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirements = ((ContainerDisenchanter) this.f_97732_).powerRequirementStatus();
        this.renderPowerConsumeStatusIcon(pGuiGraphics, mouseX, mouseY, this.f_97735_ + 58, this.f_97736_ + 11, Affinity.FIRE, (WizardLabTile.PowerStatus) powerRequirements.getOrDefault(Affinity.FIRE, WizardLabTile.PowerStatus.NOT_REQUESTING));
        this.renderPowerConsumeStatusIcon(pGuiGraphics, mouseX, mouseY, this.f_97735_ + 58, this.f_97736_ + 32, Affinity.WIND, (WizardLabTile.PowerStatus) powerRequirements.getOrDefault(Affinity.WIND, WizardLabTile.PowerStatus.NOT_REQUESTING));
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
        ItemStack stack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
        pGuiGraphics.pose().translate(0.0, 0.5, 0.0);
        pGuiGraphics.renderItem(stack, 51, 7);
        pGuiGraphics.pose().popPose();
    }

    @Override
    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(47, 50);
    }

    @Override
    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(231, 10);
    }
}