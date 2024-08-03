package com.mna.gui.block;

import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerSeerStone;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

public class GuiSeerStone extends AbstractContainerScreen<ContainerSeerStone> {

    public GuiSeerStone(ContainerSeerStone sentry, Inventory inv, Component comp) {
        super(sentry, inv, Component.literal(""));
        this.f_97726_ = 234;
        this.f_97727_ = 209;
    }

    public ResourceLocation texture() {
        return GuiTextures.Blocks.SEER_STONE;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        Component tooltip = null;
        if (mouseY >= 29 && mouseY <= 43) {
            if (mouseX >= 20 && mouseX <= 34 || mouseX >= 200 && mouseX <= 214) {
                tooltip = Component.translatable("block.mna.seer_stone.whitelist");
            }
        } else if (mouseY >= 72 && mouseY <= 76) {
            if (mouseX >= 20 && mouseX <= 34 || mouseX >= 200 && mouseX <= 214) {
                tooltip = Component.translatable("block.mna.seer_stone.blacklist");
            }
        } else if (mouseY >= 94 && mouseY <= 112 && (mouseX >= 82 && mouseX <= 98 || mouseX >= 136 && mouseX <= 152)) {
            tooltip = Component.translatable("block.mna.seer_stone.area");
        }
        if (tooltip != null) {
            List<FormattedCharSequence> split = this.f_96541_.font.split(tooltip, this.f_97726_ / 2);
            pGuiGraphics.renderTooltip(this.f_96547_, split, mouseX, mouseY);
        } else {
            this.m_280072_(pGuiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem._setShaderTexture(0, this.texture());
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, this.f_97726_, this.f_97727_);
        int crystalHalfWidth = 20;
        int crystalHeight = 74;
        int crystalX = this.f_97735_ + this.f_97726_ / 2;
        int crystalY = this.f_97736_ - crystalHeight;
        pGuiGraphics.blit(this.texture(), crystalX - crystalHalfWidth, crystalY, 236, crystalHeight, crystalHalfWidth, crystalHeight);
        pGuiGraphics.blit(this.texture(), crystalX, crystalY, 236, 0, crystalHalfWidth, crystalHeight);
    }
}