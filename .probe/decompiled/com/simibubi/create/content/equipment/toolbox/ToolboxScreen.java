package com.simibubi.create.content.equipment.toolbox;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class ToolboxScreen extends AbstractSimiContainerScreen<ToolboxMenu> {

    protected static final AllGuiTextures BG = AllGuiTextures.TOOLBOX;

    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;

    protected Slot hoveredToolboxSlot;

    private IconButton confirmButton;

    private IconButton disposeButton;

    private DyeColor color;

    private List<Rect2i> extraAreas = Collections.emptyList();

    public ToolboxScreen(ToolboxMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.init();
    }

    @Override
    protected void init() {
        this.setWindowSize(30 + BG.width, BG.height + PLAYER.height - 24);
        this.setWindowOffset(-11, 0);
        super.init();
        this.m_169413_();
        this.color = ((ToolboxMenu) this.f_97732_).contentHolder.getColor();
        this.confirmButton = new IconButton(this.f_97735_ + 30 + BG.width - 33, this.f_97736_ + BG.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.f_96541_.player.closeContainer());
        this.m_142416_(this.confirmButton);
        this.disposeButton = new IconButton(this.f_97735_ + 30 + 81, this.f_97736_ + 69, AllIcons.I_TOOLBOX);
        this.disposeButton.withCallback(() -> AllPackets.getChannel().sendToServer(new ToolboxDisposeAllPacket(((ToolboxMenu) this.f_97732_).contentHolder.m_58899_())));
        this.disposeButton.setToolTip(Lang.translateDirect("toolbox.depositBox"));
        this.m_142416_(this.disposeButton);
        this.extraAreas = ImmutableList.of(new Rect2i(this.f_97735_ + 30 + BG.width, this.f_97736_ + BG.height - 15 - 34 - 6, 72, 68));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        ((ToolboxMenu) this.f_97732_).renderPass = true;
        super.render(graphics, mouseX, mouseY, partialTicks);
        ((ToolboxMenu) this.f_97732_).renderPass = false;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = this.f_97735_ + this.f_97726_ - BG.width;
        int y = this.f_97736_;
        BG.render(graphics, x, y);
        graphics.drawString(this.f_96547_, this.f_96539_, x + 15, y + 4, 5841956, false);
        int invX = this.f_97735_;
        int invY = this.f_97736_ + this.f_97727_ - PLAYER.height;
        this.renderPlayerInventory(graphics, invX, invY);
        this.renderToolbox(graphics, x + BG.width + 50, y + BG.height + 12, partialTicks);
        PoseStack ms = graphics.pose();
        this.hoveredToolboxSlot = null;
        for (int compartment = 0; compartment < 8; compartment++) {
            int baseIndex = compartment * 4;
            Slot slot = (Slot) ((ToolboxMenu) this.f_97732_).f_38839_.get(baseIndex);
            ItemStack itemstack = slot.getItem();
            int i = slot.x + this.f_97735_;
            int j = slot.y + this.f_97736_;
            if (itemstack.isEmpty()) {
                itemstack = ((ToolboxMenu) this.f_97732_).getFilter(compartment);
            }
            if (!itemstack.isEmpty()) {
                int count = ((ToolboxMenu) this.f_97732_).totalCountInCompartment(compartment);
                String s = String.valueOf(count);
                ms.pushPose();
                ms.translate(0.0F, 0.0F, 100.0F);
                RenderSystem.enableDepthTest();
                graphics.renderItem(this.f_96541_.player, itemstack, i, j, 0);
                graphics.renderItemDecorations(this.f_96547_, itemstack, i, j, s);
                ms.popPose();
            }
            if (this.m_6774_(slot.x, slot.y, 16, 16, (double) mouseX, (double) mouseY)) {
                this.hoveredToolboxSlot = slot;
                RenderSystem.disableDepthTest();
                RenderSystem.colorMask(true, true, true, false);
                int slotColor = this.getSlotColor(baseIndex);
                graphics.fillGradient(i, j, i + 16, j + 16, slotColor, slotColor);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();
            }
        }
    }

    private void renderToolbox(GuiGraphics graphics, int x, int y, float partialTicks) {
        PoseStack ms = graphics.pose();
        ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).pushPose()).translate((double) x, (double) y, 100.0)).scale(50.0F)).rotateX(-22.0)).rotateY(-202.0);
        GuiGameElement.of(AllBlocks.TOOLBOXES.get(this.color).getDefaultState()).render(graphics);
        ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).pushPose()).translate(0.0, -0.375, 0.75)).rotateX((double) (-105.0F * ((ToolboxMenu) this.f_97732_).contentHolder.lid.getValue(partialTicks)))).translate(0.0, 0.375, -0.75);
        GuiGameElement.of((PartialModel) AllPartialModels.TOOLBOX_LIDS.get(this.color)).render(graphics);
        ms.popPose();
        for (int offset : Iterate.zeroAndOne) {
            ms.pushPose();
            ms.translate(0.0F, (float) (-offset * 1) / 8.0F, ((ToolboxMenu) this.f_97732_).contentHolder.drawers.getValue(partialTicks) * -0.175F * (float) (2 - offset));
            GuiGameElement.of(AllPartialModels.TOOLBOX_DRAWER).render(graphics);
            ms.popPose();
        }
        ms.popPose();
    }

    @Override
    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.hoveredToolboxSlot != null) {
            this.f_97734_ = this.hoveredToolboxSlot;
        }
        super.renderForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return this.extraAreas;
    }
}