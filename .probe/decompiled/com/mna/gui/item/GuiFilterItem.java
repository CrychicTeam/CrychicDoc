package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.containers.item.ContainerFilterItem;
import com.mna.tools.render.GuiRenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiFilterItem extends AbstractContainerScreen<ContainerFilterItem> {

    public GuiFilterItem(ContainerFilterItem container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.f_97726_ = 176;
        this.f_97727_ = 188;
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(new GuiFilterItem.Checkbox(((ContainerFilterItem) this.f_97732_).getDurabilityMatching(), this.f_97735_ + 72, this.f_97736_ + 80, 44, 80, 0, 248, b -> {
            ((ContainerFilterItem) this.f_97732_).clickMenuButton(this.f_96541_.player, 0);
            this.f_96541_.gameMode.handleInventoryButtonClick(((ContainerFilterItem) this.f_97732_).f_38840_, 0);
        }, Component.translatable("gui.mna.filter.durability")));
        this.m_142416_(new GuiFilterItem.Checkbox(((ContainerFilterItem) this.f_97732_).getTagMatching(), this.f_97735_ + 90, this.f_97736_ + 80, 62, 80, 8, 248, b -> {
            ((ContainerFilterItem) this.f_97732_).clickMenuButton(this.f_96541_.player, 1);
            this.f_96541_.gameMode.handleInventoryButtonClick(((ContainerFilterItem) this.f_97732_).f_38840_, 1);
        }, Component.translatable("gui.mna.filter.nbt")));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouse_x, int mouse_y, float partialTick) {
        super.render(pGuiGraphics, mouse_x, mouse_y, partialTick);
        this.m_280072_(pGuiGraphics, mouse_x, mouse_y);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouse_x, int mouse_y) {
        this.m_280273_(pGuiGraphics);
        int xPos = this.f_97735_;
        int yPos = this.f_97736_;
        GuiRenderUtils.renderStandardPlayerInventory(pGuiGraphics, xPos + this.f_97726_ / 2, yPos + this.f_97727_ - 45);
        pGuiGraphics.blit(GuiTextures.Items.FILTER_ITEM, xPos + 28, yPos, 0, 0, 120, 98);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
    }

    private class Checkbox extends ImageButton {

        private boolean checked = false;

        private final int check_u;

        private final int check_v;

        public Checkbox(boolean initialValue, int x, int y, int u, int v, int check_u, int check_v, Button.OnPress clickHandler, Component tooltip) {
            super(x, y, 14, 14, u, v, 0, GuiTextures.Items.FILTER_ITEM, clickHandler);
            this.check_u = check_u;
            this.check_v = check_v;
            this.checked = initialValue;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int mouse_x, int mouse_y, float partial_tick) {
            super.m_88315_(pGuiGraphics, mouse_x, mouse_y, partial_tick);
            if (this.checked) {
                pGuiGraphics.blit(GuiTextures.Items.FILTER_ITEM, this.m_252754_() + 3, this.m_252907_() + 3, this.check_u, this.check_v, 8, 8);
            }
        }

        @Override
        public void onClick(double mouse_x, double mouse_y) {
            this.checked = !this.checked;
            super.m_5716_(mouse_x, mouse_y);
        }
    }
}