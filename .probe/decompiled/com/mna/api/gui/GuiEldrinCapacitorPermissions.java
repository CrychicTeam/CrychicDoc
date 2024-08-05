package com.mna.api.gui;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class GuiEldrinCapacitorPermissions extends AbstractContainerScreen<EldrinCapacitorPermissionsContainer> {

    private static final ResourceLocation texture = new ResourceLocation("mna", "textures/gui/gui_conduit.png");

    private static final ItemStack ARCANE = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mna", "greater_mote_arcane")));

    private static final ItemStack ENDER = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mna", "greater_mote_ender")));

    private static final ItemStack EARTH = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mna", "greater_mote_earth")));

    private static final ItemStack WATER = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mna", "greater_mote_water")));

    private static final ItemStack FIRE = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mna", "greater_mote_fire")));

    private static final ItemStack AIR = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mna", "greater_mote_air")));

    private static final Affinity[] AffinitiesInOrder = new Affinity[] { Affinity.ARCANE, Affinity.WIND, Affinity.FIRE, Affinity.WATER, Affinity.EARTH, Affinity.ENDER };

    public GuiEldrinCapacitorPermissions(EldrinCapacitorPermissionsContainer pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, Component.empty());
        this.f_97726_ = 222;
        this.f_97727_ = 143;
    }

    @Override
    protected void init() {
        super.init();
        if (((EldrinCapacitorPermissionsContainer) this.f_97732_).userCanEdit()) {
            this.m_142416_(new ImageButton(this.f_97735_ + 79, this.f_97736_ + 37, 66, 18, 0, 220, 18, texture, 256, 256, b -> this.f_96541_.gameMode.handleInventoryButtonClick(((EldrinCapacitorPermissionsContainer) this.f_97732_).f_38840_, 0), Component.translatable("gui.mna.share-same-team")));
            this.m_142416_(new ImageButton(this.f_97735_ + 79, this.f_97736_ + 61, 66, 18, 0, 220, 18, texture, 256, 256, b -> this.f_96541_.gameMode.handleInventoryButtonClick(((EldrinCapacitorPermissionsContainer) this.f_97732_).f_38840_, 1), Component.translatable("gui.mna.share-same-faction")));
            this.m_142416_(new ImageButton(this.f_97735_ + 79, this.f_97736_ + 85, 66, 18, 0, 220, 18, texture, 256, 256, b -> this.f_96541_.gameMode.handleInventoryButtonClick(((EldrinCapacitorPermissionsContainer) this.f_97732_).f_38840_, 2), Component.translatable("gui.mna.share-same-faction")));
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.m_280273_(pGuiGraphics);
        int barWidgetX = this.f_97735_ + 10;
        int barWidgetStep = 34;
        int barWidgetY = this.f_97736_ - 12;
        for (int i = 0; i < 6; i++) {
            this.blitFillBar(pGuiGraphics, barWidgetX, barWidgetY);
            this.blitFillBarInner(pGuiGraphics, AffinitiesInOrder[i], barWidgetX, barWidgetY);
            barWidgetX += barWidgetStep;
        }
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(texture, i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int textX = this.f_97726_ / 2 + 1;
        Component c = Component.translatable("gui.mna.share-same-team");
        pGuiGraphics.drawString(this.f_96547_, c, textX - this.f_96547_.width(c) / 2, 42, 0, false);
        c = Component.translatable("gui.mna.share-same-faction");
        pGuiGraphics.drawString(this.f_96547_, c, textX - this.f_96547_.width(c) / 2, 66, 0, false);
        c = Component.translatable("gui.mna.share-public");
        pGuiGraphics.drawString(this.f_96547_, c, textX - this.f_96547_.width(c) / 2, 90, 0, false);
        int affinityY = 12;
        pGuiGraphics.renderItem(ARCANE, 12, affinityY);
        pGuiGraphics.renderItem(AIR, 46, affinityY);
        pGuiGraphics.renderItem(FIRE, 80, affinityY);
        pGuiGraphics.renderItem(WATER, 114, affinityY);
        pGuiGraphics.renderItem(EARTH, 148, affinityY);
        pGuiGraphics.renderItem(ENDER, 182, affinityY);
        int affinitySupplyY = 16;
        this.blitSupplyStatus(pGuiGraphics, Affinity.ARCANE, 30, affinitySupplyY);
        this.blitSupplyStatus(pGuiGraphics, Affinity.WIND, 64, affinitySupplyY);
        this.blitSupplyStatus(pGuiGraphics, Affinity.FIRE, 98, affinitySupplyY);
        this.blitSupplyStatus(pGuiGraphics, Affinity.WATER, 132, affinitySupplyY);
        this.blitSupplyStatus(pGuiGraphics, Affinity.EARTH, 166, affinitySupplyY);
        this.blitSupplyStatus(pGuiGraphics, Affinity.ENDER, 200, affinitySupplyY);
        int shareX = 68;
        this.blitShareStatus(pGuiGraphics, ((EldrinCapacitorPermissionsContainer) this.f_97732_).shareTeam(), shareX, 38);
        this.blitShareStatus(pGuiGraphics, ((EldrinCapacitorPermissionsContainer) this.f_97732_).shareFaction(), shareX, 62);
        this.blitShareStatus(pGuiGraphics, ((EldrinCapacitorPermissionsContainer) this.f_97732_).sharePublic(), shareX, 86);
        int textY = 127;
        if (((EldrinCapacitorPermissionsContainer) this.f_97732_).getPlacedByPlayerName() != null) {
            c = Component.literal(((EldrinCapacitorPermissionsContainer) this.f_97732_).getPlacedByPlayerName());
            pGuiGraphics.drawString(this.f_96547_, c, 22, textY, 16777215, false);
        }
        if (((EldrinCapacitorPermissionsContainer) this.f_97732_).getShareTeam() != null) {
            c = Component.literal(((EldrinCapacitorPermissionsContainer) this.f_97732_).getShareTeam());
            pGuiGraphics.drawString(this.f_96547_, c, this.f_97726_ - 22 - this.f_96547_.width(c), textY, 16777215, false);
        }
        c = Component.literal(String.format("%.2f", ((EldrinCapacitorPermissionsContainer) this.f_97732_).getChargeRate()));
        pGuiGraphics.drawString(this.f_96547_, c, 34 - this.f_96547_.width(c) / 2, 45, 16777215, false);
        c = Component.literal(String.format("%.1f", ((EldrinCapacitorPermissionsContainer) this.f_97732_).getChargeRadius()));
        pGuiGraphics.drawString(this.f_96547_, c, 200 - this.f_96547_.width(c) / 2, 45, 16777215, false);
        if (((EldrinCapacitorPermissionsContainer) this.f_97732_).getShareFaction() != null) {
            ManaAndArtificeMod.getGuiRenderHelper().renderFactionIcon(pGuiGraphics, ((EldrinCapacitorPermissionsContainer) this.f_97732_).getShareFaction(), 151, 67);
        }
    }

    private void blitFillBar(GuiGraphics pGuiGraphics, int x, int y) {
        pGuiGraphics.blit(texture, x, y, 0, 208, 32, 12);
    }

    private void blitFillBarInner(GuiGraphics pGuiGraphics, Affinity affinity, int x, int y) {
        int colorOne = FastColor.ABGR32.color(255, affinity.getColor()[0], affinity.getColor()[1], affinity.getColor()[2]);
        int colorTwo = FastColor.ABGR32.color(255, affinity.getSecondaryColor()[0], affinity.getSecondaryColor()[1], affinity.getSecondaryColor()[2]);
        float capacity = ((EldrinCapacitorPermissionsContainer) this.f_97732_).getCapacity(affinity);
        if (capacity > 0.0F) {
            float amount = ((EldrinCapacitorPermissionsContainer) this.f_97732_).getAmount(affinity);
            float pct = Math.min(amount / capacity, 1.0F);
            int fillAmt = pct <= 0.0F ? 0 : (int) (20.0F * pct);
            pGuiGraphics.fillGradient(x + 6, y + 6, x + 6 + fillAmt, y + 9, colorOne, colorTwo);
        }
    }

    private void blitSupplyStatus(GuiGraphics pGuiGraphics, Affinity affinity, int x, int y) {
        if (((EldrinCapacitorPermissionsContainer) this.f_97732_).supplies(affinity)) {
            pGuiGraphics.blit(texture, x, y, 248, 0, 8, 8);
        } else {
            pGuiGraphics.blit(texture, x, y, 248, 8, 8, 8);
        }
    }

    private void blitShareStatus(GuiGraphics pGuiGraphics, boolean shared, int x, int y) {
        if (shared) {
            pGuiGraphics.blit(texture, x, y, 248, 0, 8, 8);
        } else {
            pGuiGraphics.blit(texture, x, y + 9, 248, 8, 8, 8);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }
}