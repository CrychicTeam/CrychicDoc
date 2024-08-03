package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerAffinityTinker;
import com.mna.gui.widgets.ImageItemStackButton;
import com.mna.items.ItemInit;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GuiAffinityTinker extends SimpleWizardLabDeskGui<ContainerAffinityTinker> {

    public static HashMap<Affinity, ItemStack> affinityIcons = new HashMap<Affinity, ItemStack>() {

        private static final long serialVersionUID = 1L;

        {
            this.put(Affinity.ARCANE, new ItemStack(ItemInit.GREATER_MOTE_ARCANE.get()).setHoverName(Component.translatable("affinity.mna.arcane")));
            this.put(Affinity.WIND, new ItemStack(ItemInit.GREATER_MOTE_AIR.get()).setHoverName(Component.translatable("affinity.mna.air")));
            this.put(Affinity.EARTH, new ItemStack(ItemInit.GREATER_MOTE_EARTH.get()).setHoverName(Component.translatable("affinity.mna.earth")));
            this.put(Affinity.WATER, new ItemStack(ItemInit.GREATER_MOTE_WATER.get()).setHoverName(Component.translatable("affinity.mna.water")));
            this.put(Affinity.FIRE, new ItemStack(ItemInit.GREATER_MOTE_FIRE.get()).setHoverName(Component.translatable("affinity.mna.fire")));
            this.put(Affinity.ENDER, new ItemStack(ItemInit.GREATER_MOTE_ENDER.get()).setHoverName(Component.translatable("affinity.mna.ender")));
            this.put(Affinity.ICE, new ItemStack(ItemInit.GREATER_MOTE_ICE.get()).setHoverName(Component.translatable("affinity.mna.ice")));
            this.put(Affinity.LIGHTNING, new ItemStack(ItemInit.GREATER_MOTE_LIGHTNING.get()).setHoverName(Component.translatable("affinity.mna.lightning")));
        }
    };

    public GuiAffinityTinker(ContainerAffinityTinker container, Inventory inv, Component comp) {
        super(container, inv, Component.literal(""));
        this.f_97726_ = 175;
        this.f_97727_ = 232;
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(39, 37, Affinity.WIND, 6);
        this.addButton(39, 67, Affinity.ENDER, 3);
        this.addButton(39, 97, Affinity.EARTH, 2);
        this.addButton(111, 37, Affinity.FIRE, 4);
        this.addButton(111, 67, Affinity.ARCANE, 1);
        this.addButton(111, 97, Affinity.WATER, 5);
        this.addButton(131, 17, Affinity.LIGHTNING, 8);
        this.addButton(131, 117, Affinity.ICE, 7);
    }

    private void addButton(int x, int y, Affinity affinity, int index) {
        ImageItemStackButton btn_arcane = new ImageItemStackButton(this.f_97735_ + x, this.f_97736_ + y, 26, 26, 230, 10, 0, this.texture(), 256, 256, b -> {
            if (!((ContainerAffinityTinker) this.f_97732_).isActive()) {
                ((ContainerAffinityTinker) this.f_97732_).m_6366_(this.f_96541_.player, index);
                this.f_96541_.gameMode.handleInventoryButtonClick(((ContainerAffinityTinker) this.f_97732_).f_38840_, index);
                this.activateButton((ImageItemStackButton) b);
            }
        }, this::setTooltip, (ItemStack) affinityIcons.get(affinity)).setAffinityOffset(5, 5).setRenderBackground(((ContainerAffinityTinker) this.f_97732_).getSelectedAffinity() == affinity);
        this.m_142416_(btn_arcane);
    }

    public void setTooltip(List<Component> lines) {
    }

    public void activateButton(ImageItemStackButton b) {
        this.m_6702_().forEach(c -> {
            if (c == b) {
                b.setRenderBackground(true);
            } else if (c instanceof ImageItemStackButton) {
                ((ImageItemStackButton) c).setRenderBackground(false);
            }
        });
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.WizardLab.AFFINITY_TINKER;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        Affinity[] values = new Affinity[] { Affinity.WIND, Affinity.FIRE, Affinity.ENDER, Affinity.ARCANE, Affinity.EARTH, Affinity.WATER };
        if (((ContainerAffinityTinker) this.f_97732_).isActive()) {
            float pct = ((ContainerAffinityTinker) this.f_97732_).getProgress();
            pGuiGraphics.blit(this.texture(), 84, 90, 222, 10, 8, (int) (36.0F * pct));
        }
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        float xpPct = MathUtils.clamp01(player.isCreative() ? 1.0F : (float) player.totalExperience / (float) ((ContainerAffinityTinker) this.f_97732_).getXPCost());
        int VCoord = xpPct < 1.0F ? 5 : 0;
        pGuiGraphics.blit(this.texture(), 70, 40, 220, VCoord, (int) (36.0F * xpPct), 5);
        ItemStack stack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
        pGuiGraphics.pose().translate(0.5, 0.25, 0.0);
        pGuiGraphics.renderItem(stack, 35, 3);
        pGuiGraphics.pose().popPose();
        HashMap<Affinity, WizardLabTile.PowerStatus> powerReqs = ((ContainerAffinityTinker) this.f_97732_).powerRequirementStatus();
        WizardLabTile.PowerStatus powerStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
        Affinity requestingAffinity = Affinity.UNKNOWN;
        for (Affinity aff : values) {
            if (powerReqs.containsKey(aff)) {
                powerStatus = (WizardLabTile.PowerStatus) powerReqs.getOrDefault(aff, WizardLabTile.PowerStatus.NOT_REQUESTING);
                requestingAffinity = aff;
                break;
            }
        }
        this.renderPowerConsumeStatusIcon(pGuiGraphics, mouseX - this.f_97735_, mouseY - this.f_97736_, 104, 132, requestingAffinity, powerStatus);
    }

    @Override
    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(231, 36);
    }

    @Override
    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(49, 129);
    }
}