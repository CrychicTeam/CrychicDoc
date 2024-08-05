package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class StateSwitchingButton extends AbstractWidget {

    protected ResourceLocation resourceLocation;

    protected boolean isStateTriggered;

    protected int xTexStart;

    protected int yTexStart;

    protected int xDiffTex;

    protected int yDiffTex;

    public StateSwitchingButton(int int0, int int1, int int2, int int3, boolean boolean4) {
        super(int0, int1, int2, int3, CommonComponents.EMPTY);
        this.isStateTriggered = boolean4;
    }

    public void initTextureValues(int int0, int int1, int int2, int int3, ResourceLocation resourceLocation4) {
        this.xTexStart = int0;
        this.yTexStart = int1;
        this.xDiffTex = int2;
        this.yDiffTex = int3;
        this.resourceLocation = resourceLocation4;
    }

    public void setStateTriggered(boolean boolean0) {
        this.isStateTriggered = boolean0;
    }

    public boolean isStateTriggered() {
        return this.isStateTriggered;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        this.m_168802_(narrationElementOutput0);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        RenderSystem.disableDepthTest();
        int $$4 = this.xTexStart;
        int $$5 = this.yTexStart;
        if (this.isStateTriggered) {
            $$4 += this.xDiffTex;
        }
        if (this.m_198029_()) {
            $$5 += this.yDiffTex;
        }
        guiGraphics0.blit(this.resourceLocation, this.m_252754_(), this.m_252907_(), $$4, $$5, this.f_93618_, this.f_93619_);
        RenderSystem.enableDepthTest();
    }
}