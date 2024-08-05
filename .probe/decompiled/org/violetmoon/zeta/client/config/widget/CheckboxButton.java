package org.violetmoon.zeta.client.config.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.ValueDefinition;

public class CheckboxButton extends Button {

    private final ResourceLocation iconsTexture;

    private final ValueDefinition<Boolean> value;

    private final ChangeSet changes;

    public CheckboxButton(ResourceLocation iconsTexture, int x, int y, ChangeSet changes, ValueDefinition<Boolean> value) {
        super(new Button.Builder(Component.literal(""), CheckboxButton::toggle).pos(x, y).size(20, 20));
        this.iconsTexture = iconsTexture;
        this.value = value;
        this.changes = changes;
    }

    public CheckboxButton(ZetaClient zc, int x, int y, ChangeSet changes, ValueDefinition<Boolean> value) {
        this(zc.generalIcons, x, y, changes, value);
    }

    private static void toggle(Button press) {
        if (press instanceof CheckboxButton checkbox) {
            checkbox.changes.toggle(checkbox.value);
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.m_87963_(guiGraphics, mouseX, mouseY, partial);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        boolean enabled = this.changes.get(this.value) && this.f_93623_;
        int u = enabled ? 0 : 16;
        int v = 0;
        guiGraphics.blit(this.iconsTexture, this.m_252754_() + 2, this.m_252907_() + 1, u, v, 15, 15);
    }
}