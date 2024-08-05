package com.mna.gui.block;

import com.google.common.collect.Lists;
import com.mna.ManaAndArtifice;
import com.mna.api.sound.SFX;
import com.mna.blocks.tileentities.wizard_lab.MagiciansWorkbenchTile;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerMagiciansWorkbench;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiMagiciansWorkbench extends AbstractContainerScreen<ContainerMagiciansWorkbench> {

    List<Component> currentTooltip;

    public GuiMagiciansWorkbench(ContainerMagiciansWorkbench p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.f_97726_ = 256;
        this.f_97727_ = 202;
        this.currentTooltip = new ArrayList();
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < 8; i++) {
            int index = i;
            this.m_142416_(new GuiMagiciansWorkbench.IndexButton(this.f_97735_ + 10 + i % 2 * 26, this.f_97736_ + 10 + 26 * Math.floorDiv(i, 2), 22, 22, 23 * i, 234, 0, GuiTextures.WizardLab.MAGICIANS_WORKBENCH, 256, 256, i, button -> {
                if (button instanceof GuiMagiciansWorkbench.IndexButton idxBtn) {
                    if (idxBtn.getClickButton() == 0) {
                        ((ContainerMagiciansWorkbench) this.f_97732_).moveRecipeToCraftingGrid(index);
                    } else {
                        this.f_96541_.gameMode.handleInventoryButtonClick(((ContainerMagiciansWorkbench) this.f_97732_).f_38840_, index);
                    }
                }
            }));
        }
        this.m_142416_(new ImageButton(this.f_97735_ + 185, this.f_97736_ + 73, 7, 7, 184, 249, 0, GuiTextures.WizardLab.MAGICIANS_WORKBENCH, 256, 256, button -> ((ContainerMagiciansWorkbench) this.f_97732_).tryClearGrid(true)));
        this.m_142416_(new ImageButton(this.f_97735_ + 64, this.f_97736_ + 73, 7, 7, 184, 249, 0, GuiTextures.WizardLab.MAGICIANS_WORKBENCH, 256, 256, button -> ((ContainerMagiciansWorkbench) this.f_97732_).tryClearGrid(false)));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        this.currentTooltip.clear();
        super.render(pGuiGraphics, mouseX, mouseY, partialTick);
        if (this.currentTooltip.size() > 0) {
            pGuiGraphics.renderTooltip(this.f_96547_, Lists.transform(this.currentTooltip, Component::m_7532_), mouseX, mouseY);
        } else {
            this.m_280072_(pGuiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        pGuiGraphics.blit(GuiTextures.WizardLab.MAGICIANS_WORKBENCH, i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int p_230451_2_, int p_230451_3_) {
    }

    public class IndexButton extends ImageButton {

        int index;

        int clickButton;

        final ResourceLocation texture;

        public IndexButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int hoverOffset, ResourceLocation textureFile, int texWidth, int texHeight, int index, Button.OnPress clickHandler) {
            super(x, y, width, height, xTexStart, yTexStart, hoverOffset, textureFile, texWidth, texHeight, clickHandler, Component.literal(""));
            this.index = index;
            this.texture = textureFile;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(pGuiGraphics, mouseX, mouseY, partialTick);
            if (((ContainerMagiciansWorkbench) GuiMagiciansWorkbench.this.f_97732_).getWorkbench().getRememberedRecipeItems().size() > this.index) {
                MagiciansWorkbenchTile.RememberedRecipe recipe = (MagiciansWorkbenchTile.RememberedRecipe) ((ContainerMagiciansWorkbench) GuiMagiciansWorkbench.this.f_97732_).getWorkbench().getRememberedRecipeItems().get(this.index);
                ItemStack stack = recipe.getOutput(ManaAndArtifice.instance.proxy.getClientWorld());
                pGuiGraphics.renderItem(stack, this.m_252754_() + 3, this.m_252907_() + 3);
                if (mouseX >= this.m_252754_() && mouseX <= this.m_252754_() + this.m_5711_() && mouseY >= this.m_252907_() && mouseY <= this.m_252907_() + this.m_93694_()) {
                    MutableComponent component = Component.translatable(stack.getDescriptionId());
                    if (((ContainerMagiciansWorkbench) GuiMagiciansWorkbench.this.f_97732_).hasComponents(this.index) && !((ContainerMagiciansWorkbench) GuiMagiciansWorkbench.this.f_97732_).isRecipeAlreadyInGrid(this.index) && ((ContainerMagiciansWorkbench) GuiMagiciansWorkbench.this.f_97732_).gridIsFreeFor(this.index)) {
                        component.withStyle(ChatFormatting.GREEN);
                    } else {
                        component.withStyle(ChatFormatting.RED);
                    }
                    GuiMagiciansWorkbench.this.currentTooltip.add(component);
                    GuiMagiciansWorkbench.this.currentTooltip.add(Component.translatable("block.mna.magicians_workbench.lock_prompt").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
                }
                if (recipe.isLocked()) {
                    pGuiGraphics.blit(this.texture, this.m_252754_() + this.f_93618_ - 4, this.m_252907_() - 1, 192, 250, 5, 6);
                }
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
        }

        @Override
        protected boolean isValidClickButton(int button) {
            if (button != 0 && button != 1) {
                return false;
            } else {
                this.clickButton = button;
                return true;
            }
        }

        public int getClickButton() {
            return this.clickButton;
        }
    }
}