package com.simibubi.create.foundation.config.ui.entries;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.common.ForgeConfigSpec;

public class BooleanEntry extends ValueEntry<Boolean> {

    RenderElement enabled = AllIcons.I_CONFIRM.asStencil().<DelegatedStencilElement>withElementRenderer((ms, width, height, alpha) -> UIRenderHelper.angledGradient(ms, 0.0F, 0, height / 2, height, width, Theme.p(Theme.Key.BUTTON_SUCCESS))).at(10.0F, 0.0F);

    RenderElement disabled = AllIcons.I_DISABLE.asStencil().<DelegatedStencilElement>withElementRenderer((ms, width, height, alpha) -> UIRenderHelper.angledGradient(ms, 0.0F, 0, height / 2, height, width, Theme.p(Theme.Key.BUTTON_FAIL))).at(10.0F, 0.0F);

    BoxWidget button = new BoxWidget().<ElementWidget>showingElement(this.enabled).withCallback(() -> this.setValue(Boolean.valueOf(!this.getValue())));

    public BooleanEntry(String label, ForgeConfigSpec.ConfigValue<Boolean> value, ForgeConfigSpec.ValueSpec spec) {
        super(label, value, spec);
        this.listeners.add(this.button);
        this.onReset();
    }

    @Override
    protected void setEditable(boolean b) {
        super.setEditable(b);
        this.button.f_93623_ = b;
    }

    @Override
    public void tick() {
        super.tick();
        this.button.tick();
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
        super.render(graphics, index, y, x, width, height, mouseX, mouseY, p_230432_9_, partialTicks);
        this.button.m_252865_(x + width - 80 - 28);
        this.button.m_253211_(y + 10);
        this.button.m_93674_(35);
        this.button.setHeight(height - 20);
        this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    public void onValueChange(Boolean newValue) {
        super.onValueChange(newValue);
        this.button.showingElement(newValue ? this.enabled : this.disabled);
        this.bumpCog(newValue ? 15.0F : -16.0F);
    }
}