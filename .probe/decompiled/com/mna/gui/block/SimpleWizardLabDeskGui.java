package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.SimpleWizardLabContainer;
import com.mna.network.ClientMessageDispatcher;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public abstract class SimpleWizardLabDeskGui<T extends SimpleWizardLabContainer<?, ?>> extends AbstractContainerScreen<T> {

    protected ImageButton goButton;

    protected final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    protected ArrayList<Component> tooltip;

    public SimpleWizardLabDeskGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 170;
        this.tooltip = new ArrayList();
    }

    protected Pair<Integer, Integer> goButtonPos() {
        return new Pair(8, 82);
    }

    protected Pair<Integer, Integer> goButtonUV() {
        return new Pair(0, 228);
    }

    protected Pair<Integer, Integer> goButtonSize() {
        return new Pair(25, 14);
    }

    protected int goButtonHoverOffset() {
        return 14;
    }

    protected ResourceLocation goButtonTexture() {
        return this.texture();
    }

    protected void addTooltipLine(Component line) {
        this.tooltip.add(line);
    }

    @Override
    protected void init() {
        super.init();
        this.goButton = new ImageButton(this.f_97735_ + (Integer) this.goButtonPos().getFirst(), this.f_97736_ + (Integer) this.goButtonPos().getSecond(), (Integer) this.goButtonSize().getFirst(), (Integer) this.goButtonSize().getSecond(), (Integer) this.goButtonUV().getFirst(), (Integer) this.goButtonUV().getSecond(), this.goButtonHoverOffset(), this.goButtonTexture(), b -> {
            ((SimpleWizardLabContainer) this.f_97732_).clickMenuButton(this.f_96541_.player, 0);
            this.f_96541_.gameMode.handleInventoryButtonClick(((SimpleWizardLabContainer) this.f_97732_).f_38840_, 0);
        });
        this.m_142416_(this.goButton);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.goButton.f_93623_ = ((SimpleWizardLabContainer) this.f_97732_).canActivate();
        this.goButton.f_93624_ = this.goButton.f_93623_;
        this.tooltip.clear();
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
        if (this.tooltip.size() > 0) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.tooltip, Optional.empty(), mouseX, mouseY);
        }
        if (ManaAndArtifice.instance.proxy.getGameTicks() % 20L == 0L) {
            ClientMessageDispatcher.sendRequestWellspringNetworkSyncMessage(false);
        }
    }

    protected boolean cursorIsWithin(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= this.f_97735_ + x && mouseX <= this.f_97735_ + x + w && mouseY >= this.f_97736_ + y && mouseY <= this.f_97736_ + y + h;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouesX, int mouseY) {
        if (((SimpleWizardLabContainer) this.f_97732_).getXPCost() > 0) {
            Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
            if (player.totalExperience < ((SimpleWizardLabContainer) this.f_97732_).getXPCost() && !player.isCreative()) {
                pGuiGraphics.drawString(this.f_96547_, Component.translatable("gui.mna.wizard_lab.xp_insufficient", ((SimpleWizardLabContainer) this.f_97732_).getXPCost(), player.totalExperience), 0, -9, this.WHITE, false);
            } else {
                pGuiGraphics.drawString(this.f_96547_, Component.translatable("gui.mna.wizard_lab.xp_cost", ((SimpleWizardLabContainer) this.f_97732_).getXPCost(), player.totalExperience), 0, -9, this.WHITE, false);
            }
        }
    }

    protected void renderPowerConsumeStatusIcon(GuiGraphics pGuiGraphics, int mouseX, int mouseY, int x, int y, Affinity affinity, WizardLabTile.PowerStatus status) {
        switch(status) {
            case NO_POWER:
                pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, x, y, 120.0F, 88.0F, 8, 8, 128, 128);
                if (this.cursorIsWithin(mouseX, mouseY, x - this.f_97735_, y - this.f_97736_, 8, 8)) {
                    this.tooltip.add(Component.translatable("gui.mna.wizard_lab.no_power", affinity).withStyle(ChatFormatting.GOLD));
                }
                break;
            case NOT_REQUESTING:
                pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, x, y, 120.0F, 96.0F, 8, 8, 128, 128);
                if (this.cursorIsWithin(mouseX, mouseY, x - this.f_97735_, y - this.f_97736_, 8, 8)) {
                    this.tooltip.add(Component.translatable("gui.mna.wizard_lab.not_requesting").withStyle(ChatFormatting.GOLD));
                }
                break;
            case SUPPLIED:
                pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, x, y, 120.0F, 104.0F, 8, 8, 128, 128);
                if (this.cursorIsWithin(mouseX, mouseY, x - this.f_97735_, y - this.f_97736_, 8, 8)) {
                    this.tooltip.add(Component.translatable("gui.mna.wizard_lab.supplied", affinity).withStyle(ChatFormatting.GOLD));
                }
                break;
            case NO_CONDUIT:
                pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, x, y, 120.0F, 112.0F, 8, 8, 128, 128);
                if (this.cursorIsWithin(mouseX, mouseY, x - this.f_97735_, y - this.f_97736_, 8, 8)) {
                    this.tooltip.add(Component.translatable("gui.mna.wizard_lab.conduit_missing", affinity).withStyle(ChatFormatting.GOLD));
                }
                break;
            case PARTIAL:
                pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, x, y, 120.0F, 120.0F, 8, 8, 128, 128);
                if (this.cursorIsWithin(mouseX, mouseY, x - this.f_97735_, y - this.f_97736_, 8, 8)) {
                    this.tooltip.add(Component.translatable("gui.mna.wizard_lab.supplied_partial", affinity).withStyle(ChatFormatting.GOLD));
                }
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    protected abstract ResourceLocation texture();
}