package dev.ftb.mods.ftblibrary.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;

public class CheckBoxList extends Button {

    private final boolean radioButtonBehaviour;

    private final List<CheckBoxList.CheckBoxEntry> entries;

    public CheckBoxList(BaseScreen gui, boolean radioButtonBehaviour) {
        super(gui);
        this.setSize(10, 2);
        this.radioButtonBehaviour = radioButtonBehaviour;
        this.entries = new ArrayList();
    }

    public int getValueCount() {
        return 2;
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
    }

    public void drawCheckboxBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawCheckboxBackground(graphics, x, y, w, h, this.radioButtonBehaviour);
    }

    public void getCheckboxIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h, int index, int value) {
        theme.drawCheckbox(graphics, x, y, w, h, WidgetType.mouseOver(this.isMouseOver()), value != 0, this.radioButtonBehaviour);
    }

    public void addBox(CheckBoxList.CheckBoxEntry checkBox) {
        this.entries.add(checkBox);
        this.setWidth(Math.max(this.width, this.getGui().getTheme().getStringWidth(checkBox.name)));
        this.setHeight(this.height + 11);
    }

    public CheckBoxList.CheckBoxEntry addBox(String name) {
        CheckBoxList.CheckBoxEntry entry = new CheckBoxList.CheckBoxEntry(name, this);
        this.addBox(entry);
        return entry;
    }

    @Override
    public void onClicked(MouseButton button) {
        int y = this.getMouseY() - this.getY();
        if (y % 11 != 10) {
            int i = y / 11;
            if (i >= 0 && i < this.entries.size()) {
                ((CheckBoxList.CheckBoxEntry) this.entries.get(i)).onClicked(button, i);
            }
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        this.drawBackground(graphics, theme, x, y, w, h);
        for (int i = 0; i < this.entries.size(); i++) {
            CheckBoxList.CheckBoxEntry entry = (CheckBoxList.CheckBoxEntry) this.entries.get(i);
            int ey = y + i * 11 + 1;
            this.drawCheckboxBackground(graphics, theme, x, ey, 10, 10);
            this.getCheckboxIcon(graphics, theme, x + 1, ey + 1, 8, 8, i, entry.index);
            theme.drawString(graphics, entry.name, x + 12, ey + 1);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static class CheckBoxEntry {

        private final String name;

        private final CheckBoxList checkBoxList;

        private int index = 0;

        public CheckBoxEntry(String name, CheckBoxList checkBoxList) {
            this.name = name;
            this.checkBoxList = checkBoxList;
        }

        public void onClicked(MouseButton button, int index) {
            this.select((this.index + 1) % this.checkBoxList.getValueCount());
            this.checkBoxList.playClickSound();
        }

        public void addMouseOverText(List<String> list) {
        }

        public CheckBoxList.CheckBoxEntry select(int index) {
            if (this.checkBoxList.radioButtonBehaviour) {
                if (index <= 0) {
                    return this;
                }
                for (CheckBoxList.CheckBoxEntry entry : this.checkBoxList.entries) {
                    boolean old1 = entry.index > 0;
                    entry.index = 0;
                    if (old1) {
                        entry.onValueChanged();
                    }
                }
            }
            int old = this.index;
            this.index = index;
            if (old != this.index) {
                this.onValueChanged();
            }
            return this;
        }

        public int getIndex() {
            return this.index;
        }

        public void onValueChanged() {
        }
    }
}