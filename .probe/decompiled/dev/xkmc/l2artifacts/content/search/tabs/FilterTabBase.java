package dev.xkmc.l2artifacts.content.search.tabs;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public abstract class FilterTabBase<T extends FilterTabBase<T>> extends Button {

    public final int index;

    public final ItemStack stack;

    public final FilterTabToken<T> token;

    public final FilterTabManager manager;

    public FilterTabBase(int index, FilterTabToken<T> token, FilterTabManager manager, ItemStack stack, Component title) {
        super(0, 0, 32, 28, title, b -> ((FilterTabBase) b).onTabClicked(), Supplier::get);
        this.index = index;
        this.stack = stack;
        this.token = token;
        this.manager = manager;
    }

    public abstract void onTabClicked();

    public void onTooltip(GuiGraphics g, int x, int y) {
        g.renderTooltip(Minecraft.getInstance().font, this.m_6035_(), x, y);
    }

    @Override
    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            this.token.type.draw(g, this.m_252754_(), this.m_252907_(), this.manager.selected == this.token, this.index);
            this.token.type.drawIcon(g, this.m_252754_(), this.m_252907_(), this.index, this.stack);
        }
        if (this == this.manager.list.get(this.manager.list.size() - 1)) {
            this.manager.onToolTipRender(g, mouseX, mouseY);
        }
    }
}