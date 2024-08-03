package net.mehvahdjukaar.supplementaries.client.screens;

import net.mehvahdjukaar.supplementaries.common.inventories.SackContainerMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.VariableSizeContainerMenu;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SackScreen extends AbstractContainerScreen<VariableSizeContainerMenu> {

    public SackScreen(VariableSizeContainerMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.f_97726_ = 176;
        this.f_97727_ = 166;
    }

    @Override
    protected void renderBg(GuiGraphics matrixStack, float partialTicks, int x, int y) {
    }

    private void renderBack(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.f_96543_ - this.f_97726_) / 2;
        int y = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(ModTextures.SACK_GUI_TEXTURE, x, y, 0, 0, this.f_97726_, this.f_97727_);
    }

    private void renderSlots(GuiGraphics graphics) {
        int k = -1 + (this.f_96543_ - this.f_97726_) / 2;
        int l = -1 + (this.f_96544_ - this.f_97727_) / 2;
        int size = (Integer) CommonConfigs.Functional.SACK_SLOTS.get();
        int[] dims = SackContainerMenu.getRatio(size);
        if (dims[0] > 9) {
            dims[0] = 9;
            dims[1] = (int) Math.ceil((double) ((float) size / 9.0F));
        }
        int yp = 44 - 9 * dims[1];
        for (int h = 0; h < dims[1]; h++) {
            int dimx = Math.min(dims[0], size);
            int xp = 89 - dimx * 18 / 2;
            for (int j = 0; j < dimx; j++) {
                graphics.blit(ModTextures.SLOT_TEXTURE, k + xp + j * 18, l + yp + 18 * h, 0.0F, 0.0F, 18, 18, 18, 18);
            }
            size -= dims[0];
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        this.renderBack(graphics, partialTicks, mouseX, mouseY);
        this.renderSlots(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.m_280072_(graphics, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.f_96541_.player.closeContainer();
            return true;
        } else {
            return super.keyPressed(key, b, c);
        }
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
    }
}