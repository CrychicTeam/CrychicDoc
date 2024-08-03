package net.blay09.mods.waystones.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SortWaystoneButton extends Button {

    private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");

    private final int sortDir;

    private final int visibleRegionStart;

    private final int visibleRegionHeight;

    public SortWaystoneButton(int x, int y, int sortDir, int visibleRegionStart, int visibleRegionHeight, Button.OnPress pressable) {
        super(x, y, 11, 7, Component.empty(), pressable, Button.DEFAULT_NARRATION);
        this.sortDir = sortDir;
        this.visibleRegionStart = visibleRegionStart;
        this.visibleRegionHeight = visibleRegionHeight;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (mouseY >= this.visibleRegionStart && mouseY < this.visibleRegionStart + this.visibleRegionHeight) {
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            int renderY = this.m_252907_() - (this.sortDir == 1 ? 20 : 5);
            RenderSystem.enableBlend();
            if (this.f_93623_ && this.f_93622_) {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else if (this.f_93623_) {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.75F);
            } else {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.25F);
            }
            if (this.f_93622_ && this.f_93623_) {
                guiGraphics.blit(SERVER_SELECTION_BUTTONS, this.m_252754_() - 5, renderY, this.sortDir == 1 ? 64 : 96, 32, 32, 32);
            } else {
                guiGraphics.blit(SERVER_SELECTION_BUTTONS, this.m_252754_() - 5, renderY, this.sortDir == 1 ? 64 : 96, 0, 32, 32);
            }
            RenderSystem.disableBlend();
        }
    }
}