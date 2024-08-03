package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiBagBase;
import com.mna.gui.containers.item.ContainerMarkBook;
import com.mna.items.ItemInit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiMarkBook extends GuiBagBase<ContainerMarkBook> {

    public GuiMarkBook(ContainerMarkBook book, Inventory inv, Component comp) {
        super(inv, book);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        float textScaleFactor = 0.5F;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(textScaleFactor, textScaleFactor, textScaleFactor);
        for (int i = 0; i < 8; i++) {
            if (((ContainerMarkBook) this.f_97732_).m_38853_(i).hasItem()) {
                ItemStack stack = ((ContainerMarkBook) this.f_97732_).m_38853_(i).getItem();
                BlockPos markPos = ItemInit.RUNE_MARKING.get().getLocation(stack);
                Direction markFace = ItemInit.RUNE_MARKING.get().getFace(stack);
                Component blockName = ItemInit.RUNE_MARKING.get().getBlockName(stack);
                if (blockName == null) {
                    blockName = Component.translatable("item.mna.rune_marking.noPosition");
                }
                pGuiGraphics.drawString(this.f_96547_, Component.literal(stack.getHoverName().getString()).append(" (").append(blockName).append(")"), (int) (36.0F / textScaleFactor), (int) ((float) (8 + 18 * i) / textScaleFactor), 4210752, false);
                if (markPos != null) {
                    pGuiGraphics.drawString(this.f_96547_, markPos.m_123344_() + (markFace != null ? " (" + markFace.getName() + ")" : ""), (int) (36.0F / textScaleFactor), (int) ((float) (15 + 18 * i) / textScaleFactor), 4210752, false);
                }
            }
        }
        for (int ix = 8; ix < 16; ix++) {
            if (((ContainerMarkBook) this.f_97732_).m_38853_(ix).hasItem()) {
                ItemStack stackx = ((ContainerMarkBook) this.f_97732_).m_38853_(ix).getItem();
                BlockPos markPosx = ItemInit.RUNE_MARKING.get().getLocation(stackx);
                Direction markFacex = ItemInit.RUNE_MARKING.get().getFace(stackx);
                Component blockNamex = ItemInit.RUNE_MARKING.get().getBlockName(stackx);
                if (blockNamex == null) {
                    blockNamex = Component.translatable("item.mna.rune_marking.noPosition");
                }
                pGuiGraphics.drawString(this.f_96547_, Component.literal(stackx.getHoverName().getString()).append(" (").append(blockNamex).append(")"), (int) (151.0F / textScaleFactor), (int) ((float) (8 + 18 * (ix - 8)) / textScaleFactor), 4210752, false);
                if (markPosx != null) {
                    pGuiGraphics.drawString(this.f_96547_, markPosx.m_123344_() + (markFacex != null ? " (" + markFacex.getName() + ")" : ""), (int) (151.0F / textScaleFactor), (int) ((float) (15 + 18 * (ix - 8)) / textScaleFactor), 4210752, false);
                }
            }
        }
        pGuiGraphics.pose().popPose();
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.Items.BOOK_MARKS;
    }

    @Override
    public String name() {
        return "Book of Marks";
    }
}