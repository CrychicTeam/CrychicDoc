package com.simibubi.create.foundation.config.ui.entries;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.simibubi.create.foundation.config.ui.ConfigScreenList;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.common.ForgeConfigSpec;

public class SubMenuEntry extends ConfigScreenList.LabeledEntry {

    protected BoxWidget button;

    public SubMenuEntry(SubMenuConfigScreen parent, String label, ForgeConfigSpec spec, UnmodifiableConfig config) {
        super(label);
        this.button = new BoxWidget(0, 0, 35, 16).<ElementWidget>showingElement(AllIcons.I_CONFIG_OPEN.asStencil().at(10.0F, 0.0F)).withCallback(() -> ScreenOpener.open(new SubMenuConfigScreen(parent, label, parent.type, spec, config)));
        this.button.modifyElement(e -> ((DelegatedStencilElement) e).withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.button)));
        this.listeners.add(this.button);
    }

    @Override
    public void tick() {
        super.tick();
        this.button.tick();
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
        super.render(graphics, index, y, x, width, height, mouseX, mouseY, p_230432_9_, partialTicks);
        this.button.m_252865_(x + width - 108);
        this.button.m_253211_(y + 10);
        this.button.setHeight(height - 20);
        this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected int getLabelWidth(int totalWidth) {
        return (int) ((float) totalWidth * 0.4F) + 30;
    }
}