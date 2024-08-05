package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.containers.item.ContainerPractitionersPouch;
import com.mna.items.ItemInit;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.tools.render.GuiRenderUtils;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiPractitionersPouch extends AbstractContainerScreen<ContainerPractitionersPouch> {

    private static final int PATCH_LEFT = 155;

    private static final int PATCH_BOTTOM = 136;

    private PractitionersPouchPatches currentPatch = null;

    private Component currentTooltip = null;

    private ImageButton closeButton = null;

    private ArrayList<GuiPractitionersPouch.ItemStackButton> patchSelectButtons;

    public GuiPractitionersPouch(ContainerPractitionersPouch screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 226;
        this.patchSelectButtons = new ArrayList();
    }

    @Override
    protected void init() {
        super.init();
        this.closeButton = (ImageButton) this.m_142416_(new ImageButton(this.f_97735_ + 82, this.f_97736_ + 36, 8, 8, 248, 248, 0, GuiTextures.Items.PRACTITIONERS_POUCH, 256, 256, button -> this.setPatch(null)));
        this.closeButton.f_93623_ = false;
        this.closeButton.f_93624_ = false;
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.DEPTH, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_DEPTH.get()), this.f_97735_ + 159, this.f_97736_ + 80, b -> {
            })));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.DEPTH, 2)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_DEPTH_2.get()), this.f_97735_ + 159, this.f_97736_ + 98, b -> {
            })));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.SPEED, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_SPEED.get()), this.f_97735_ + 177, this.f_97736_ + 80, b -> {
            })));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.SPEED, 2)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_SPEED_2.get()), this.f_97735_ + 177, this.f_97736_ + 98, b -> {
            })));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.SPEED, 3)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_SPEED_3.get()), this.f_97735_ + 177, this.f_97736_ + 116, b -> {
            })));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.GLYPH, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_GLYPH.get()), this.f_97735_ + 195, this.f_97736_ + 80, true, b -> this.setPatch(PractitionersPouchPatches.GLYPH))));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.MOTE, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_MOTE.get()), this.f_97735_ + 195, this.f_97736_ + 98, true, b -> this.setPatch(PractitionersPouchPatches.MOTE))));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.CONVEYANCE, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_CONVEYANCE.get()), this.f_97735_ + 195, this.f_97736_ + 116, true, b -> this.setPatch(PractitionersPouchPatches.CONVEYANCE))));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.RIFT, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_RIFT.get()), this.f_97735_ + 213, this.f_97736_ + 80, b -> {
            })));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.WEAVE, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_WEAVE.get()), this.f_97735_ + 213, this.f_97736_ + 98, true, b -> this.setPatch(PractitionersPouchPatches.WEAVE))));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.COLLECTION, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_COLLECTION.get()), this.f_97735_ + 213, this.f_97736_ + 116, b -> {
            })));
        }
        if (((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.VOID, 1)) {
            this.patchSelectButtons.add((GuiPractitionersPouch.ItemStackButton) this.m_142416_(new GuiPractitionersPouch.ItemStackButton(new ItemStack(ItemInit.PATCH_VOID.get()), this.f_97735_ + 159, this.f_97736_ + 116, true, b -> this.setPatch(PractitionersPouchPatches.VOID))));
        }
    }

    private void setPatch(PractitionersPouchPatches patch) {
        this.currentPatch = patch;
        ((ContainerPractitionersPouch) this.f_97732_).setPatchActive(patch);
        if (patch != null && patch.hasInventory()) {
            this.closeButton.f_93624_ = true;
            this.closeButton.f_93623_ = true;
            this.closeButton.m_252865_(this.f_97735_ + 155 + patch.guiW() - 14);
            this.closeButton.m_253211_(this.f_97736_ + 136 - patch.guiH() + 9);
            this.patchSelectButtons.forEach(b -> {
                b.f_93623_ = false;
                b.f_93624_ = false;
            });
        } else {
            this.closeButton.f_93624_ = false;
            this.closeButton.f_93623_ = false;
            this.patchSelectButtons.forEach(b -> {
                b.f_93623_ = true;
                b.f_93624_ = true;
            });
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        this.currentTooltip = null;
        super.render(pGuiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int xPos = this.f_97735_ + this.f_97726_ / 2;
        int yPos = this.f_97736_ + this.f_97727_ - 45;
        GuiRenderUtils.renderStandardPlayerInventory(pGuiGraphics, xPos, yPos);
        yPos -= 136;
        pGuiGraphics.blit(((ContainerPractitionersPouch) this.f_97732_).patchEnabled(PractitionersPouchPatches.DEPTH, 9999) ? GuiTextures.Items.PRACTITIONERS_POUCH_2 : GuiTextures.Items.PRACTITIONERS_POUCH, xPos - 123, yPos, 96, 0, 150, 96);
        if (this.currentPatch == null) {
            pGuiGraphics.blit(GuiTextures.Items.PRACTITIONERS_POUCH, this.f_97735_ + 155, this.f_97736_ + 136 - 60, 105, 183, 78, 60);
        } else {
            this.blitPatch(pGuiGraphics, this.currentPatch.guiSheet() == 2 ? GuiTextures.Items.PRACTITIONERS_POUCH_2 : GuiTextures.Items.PRACTITIONERS_POUCH, this.currentPatch.guiU(), this.currentPatch.guiV(), this.currentPatch.guiW(), this.currentPatch.guiH());
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        if (this.currentTooltip != null) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.currentTooltip, mouseX - this.f_97735_, mouseY - this.f_97736_);
        }
        this.m_280072_(pGuiGraphics, mouseX - this.f_97735_, mouseY - this.f_97736_);
    }

    private void blitPatch(GuiGraphics pGuiGraphics, ResourceLocation textureSheet, int u, int v, int w, int h) {
        pGuiGraphics.blit(textureSheet, this.f_97735_ + 155 - 4, this.f_97736_ + 136 - h + 5, u, v, w, h);
    }

    class ItemStackButton extends Button {

        private final ItemStack displayStack;

        private final boolean renderPlus;

        public ItemStackButton(ItemStack displayStack, int x, int y, Button.OnPress onPress) {
            this(displayStack, x, y, false, onPress);
        }

        public ItemStackButton(ItemStack displayStack, int x, int y, boolean renderPlus, Button.OnPress onPress) {
            super(x, y, 16, 16, Component.translatable(displayStack.getDescriptionId()), onPress, Button.DEFAULT_NARRATION);
            this.displayStack = displayStack;
            this.renderPlus = renderPlus;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
            pGuiGraphics.renderItem(this.displayStack, this.m_252754_(), this.m_252907_());
            if (this.renderPlus) {
                pGuiGraphics.blit(GuiTextures.Items.PRACTITIONERS_POUCH, this.m_252754_() + this.f_93618_ - 8, this.m_252907_(), 0, 240.0F, 248.0F, 8, 8, 256, 256);
            }
            if (this.m_198029_()) {
                GuiPractitionersPouch.this.currentTooltip = this.m_6035_();
            }
        }
    }
}