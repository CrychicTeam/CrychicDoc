package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ImageComponent;
import dev.ftb.mods.ftbquests.client.gui.quests.ViewQuestPanel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

public class ImageComponentWidget extends Widget {

    private final ImageComponent component;

    private final MutableComponent mutableComponent;

    private final ViewQuestPanel viewQuestPanel;

    private final int index;

    public ImageComponentWidget(ViewQuestPanel viewQuestPanel, Panel panel, ImageComponent component, int index) {
        super(panel);
        this.viewQuestPanel = viewQuestPanel;
        this.component = component;
        this.index = index;
        this.mutableComponent = MutableComponent.create(this.component);
        this.setSize(this.component.width, this.component.height);
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.mutableComponent.getStyle().getHoverEvent() != null && this.mutableComponent.getStyle().getHoverEvent().getAction() == HoverEvent.Action.SHOW_TEXT) {
            list.add(this.mutableComponent.getStyle().getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT));
        }
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        this.component.image.draw(graphics, x, y, w, h);
    }

    public ImageComponent getComponent() {
        return this.component;
    }

    @Override
    public boolean mouseDoubleClicked(MouseButton button) {
        if (this.isMouseOver() && this.viewQuestPanel.canEdit()) {
            this.viewQuestPanel.editDescLine(this, this.index, false, this.component);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (this.isMouseOver() && this.viewQuestPanel.canEdit() && button.isRight()) {
            this.viewQuestPanel.editDescLine(this, this.index, true, this.component);
            return true;
        } else {
            return false;
        }
    }
}