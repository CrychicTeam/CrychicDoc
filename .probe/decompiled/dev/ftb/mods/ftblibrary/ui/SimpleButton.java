package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class SimpleButton extends Button {

    private final SimpleButton.Callback consumer;

    private final List<Component> tooltip;

    public SimpleButton(Panel panel, Component text, Icon icon, SimpleButton.Callback c) {
        super(panel, text, icon);
        this.consumer = c;
        this.tooltip = List.of();
    }

    public SimpleButton(Panel panel, List<Component> text, Icon icon, SimpleButton.Callback c) {
        super(panel, (Component) (text.isEmpty() ? Component.empty() : (Component) text.get(0)), icon);
        this.consumer = c;
        this.tooltip = text;
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.tooltip.isEmpty()) {
            super.addMouseOverText(list);
        } else {
            this.tooltip.forEach(list::add);
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
    }

    @Override
    public void onClicked(MouseButton button) {
        this.playClickSound();
        this.consumer.onClicked(this, button);
    }

    public interface Callback {

        void onClicked(SimpleButton var1, MouseButton var2);
    }
}