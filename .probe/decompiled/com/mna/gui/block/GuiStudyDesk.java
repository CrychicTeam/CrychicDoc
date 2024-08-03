package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerStudyDesk;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GuiStudyDesk extends SimpleWizardLabDeskGui<ContainerStudyDesk> {

    public GuiStudyDesk(ContainerStudyDesk container, Inventory inv, Component comp) {
        super(container, inv, Component.literal(""));
        this.f_97726_ = 176;
        this.f_97727_ = 154;
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.WizardLab.STUDY_DESK;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
        ItemStack stack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
        pGuiGraphics.pose().translate(0.5, 0.5, 0.0);
        pGuiGraphics.renderItem(stack, 35, 2);
        pGuiGraphics.pose().popPose();
        boolean wasActive = this.goButton.f_93623_;
        boolean wasVisible = this.goButton.f_93624_;
        this.goButton.f_93623_ = true;
        this.goButton.f_93624_ = true;
        if (this.goButton.m_5953_((double) x, (double) y) && !wasActive && !((ContainerStudyDesk) this.f_97732_).m_38853_(0).getItem().isEmpty()) {
            this.tooltip.add(((ContainerStudyDesk) this.f_97732_).getCantStartReason(this.f_96541_.player).tooltip());
        }
        this.goButton.f_93623_ = wasActive;
        this.goButton.f_93624_ = wasVisible;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(pGuiGraphics, partialTicks, mouseX, mouseY);
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        float xpPct = MathUtils.clamp01(player.isCreative() ? 1.0F : (float) player.totalExperience / (float) ((ContainerStudyDesk) this.f_97732_).getXPCost());
        int VCoord = xpPct < 1.0F ? 49 : 0;
        pGuiGraphics.blit(this.texture(), this.f_97735_ + 70, this.f_97736_ + 39, 220, VCoord, (int) (36.0F * xpPct), 5);
    }

    @Override
    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(90, 50);
    }

    @Override
    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(231, 5);
    }
}