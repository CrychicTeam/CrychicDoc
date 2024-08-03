package me.jellysquid.mods.sodium.client.gui.options.control;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.Validate;

public class CyclingControl<T extends Enum<T>> implements Control<T> {

    private final Option<T> option;

    private final T[] allowedValues;

    private final Component[] names;

    public CyclingControl(Option<T> option, Class<T> enumType) {
        this(option, enumType, (T[]) enumType.getEnumConstants());
    }

    public CyclingControl(Option<T> option, Class<T> enumType, Component[] names) {
        T[] universe = (T[]) enumType.getEnumConstants();
        Validate.isTrue(universe.length == names.length, "Mismatch between universe length and names array length", new Object[0]);
        Validate.notEmpty(universe, "The enum universe must contain at least one item", new Object[0]);
        this.option = option;
        this.allowedValues = universe;
        this.names = names;
    }

    public CyclingControl(Option<T> option, Class<T> enumType, T[] allowedValues) {
        T[] universe = (T[]) enumType.getEnumConstants();
        this.option = option;
        this.allowedValues = allowedValues;
        this.names = new Component[universe.length];
        for (int i = 0; i < this.names.length; i++) {
            T value = universe[i];
            Component name;
            if (value instanceof TextProvider) {
                name = ((TextProvider) value).getLocalizedName();
            } else {
                name = Component.literal(value.name());
            }
            this.names[i] = name;
        }
    }

    public Component[] getNames() {
        return this.names;
    }

    @Override
    public Option<T> getOption() {
        return this.option;
    }

    @Override
    public ControlElement<T> createElement(Dim2i dim) {
        return new CyclingControl.CyclingControlElement<>(this.option, dim, this.allowedValues, this.names);
    }

    @Override
    public int getMaxWidth() {
        return 70;
    }

    private static class CyclingControlElement<T extends Enum<T>> extends ControlElement<T> {

        private final T[] allowedValues;

        private final Component[] names;

        private int currentIndex;

        public CyclingControlElement(Option<T> option, Dim2i dim, T[] allowedValues, Component[] names) {
            super(option, dim);
            this.allowedValues = allowedValues;
            this.names = names;
            this.currentIndex = 0;
            for (int i = 0; i < allowedValues.length; i++) {
                if (allowedValues[i] == option.getValue()) {
                    this.currentIndex = i;
                    break;
                }
            }
        }

        @Override
        public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
            super.render(drawContext, mouseX, mouseY, delta);
            Enum<T> value = this.option.getValue();
            Component name = this.names[value.ordinal()];
            int strWidth = this.getStringWidth(name);
            this.drawString(drawContext, name, this.dim.getLimitX() - strWidth - 6, this.dim.getCenterY() - 4, -1);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.option.isAvailable() && button == 0 && this.dim.containsCursor(mouseX, mouseY)) {
                this.cycleControl(Screen.hasShiftDown());
                this.playClickSound();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!this.m_93696_()) {
                return false;
            } else if (CommonInputs.selected(keyCode)) {
                this.cycleControl(Screen.hasShiftDown());
                return true;
            } else {
                return false;
            }
        }

        public void cycleControl(boolean reverse) {
            if (reverse) {
                this.currentIndex = (this.currentIndex + this.allowedValues.length - 1) % this.allowedValues.length;
            } else {
                this.currentIndex = (this.currentIndex + 1) % this.allowedValues.length;
            }
            this.option.setValue(this.allowedValues[this.currentIndex]);
        }
    }
}