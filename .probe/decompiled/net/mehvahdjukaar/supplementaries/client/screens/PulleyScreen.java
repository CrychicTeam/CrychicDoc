package net.mehvahdjukaar.supplementaries.client.screens;

import net.mehvahdjukaar.supplementaries.common.inventories.PulleyContainerMenu;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.ImmediatelyFastCompat;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PulleyScreen extends AbstractContainerScreen<PulleyContainerMenu> {

    private final CyclingSlotBackground slotBG = new CyclingSlotBackground(0);

    public PulleyScreen(PulleyContainerMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.f_97726_ = 176;
        this.f_97727_ = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.startBatching();
        }
        int k = (this.f_96543_ - this.f_97726_) / 2;
        int l = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(ModTextures.PULLEY_BLOCK_GUI_TEXTURE, k, l, 0, 0, this.f_97726_, this.f_97727_);
        this.slotBG.render(this.f_97732_, graphics, partialTicks, this.f_97735_, this.f_97736_);
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.endBatching();
        }
    }

    @Override
    public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.m_280072_(matrixStack, mouseX, mouseY);
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
    public void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.slotBG.tick(ModTextures.PULLEY_SLOT_ICONS);
    }
}