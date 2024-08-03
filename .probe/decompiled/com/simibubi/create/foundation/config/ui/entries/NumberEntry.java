package com.simibubi.create.foundation.config.ui.entries;

import com.simibubi.create.foundation.config.ui.ConfigTextField;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.element.TextStencilElement;
import com.simibubi.create.foundation.utility.Components;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class NumberEntry<T extends Number> extends ValueEntry<T> {

    protected int minOffset = 0;

    protected int maxOffset = 0;

    protected TextStencilElement minText = null;

    protected TextStencilElement maxText = null;

    protected EditBox textField = new ConfigTextField(Minecraft.getInstance().font, 0, 0, 200, 20);

    @Nullable
    public static NumberEntry<? extends Number> create(Object type, String label, ForgeConfigSpec.ConfigValue<?> value, ForgeConfigSpec.ValueSpec spec) {
        if (type instanceof Integer) {
            return new NumberEntry.IntegerEntry(label, (ForgeConfigSpec.ConfigValue<Integer>) value, spec);
        } else if (type instanceof Float) {
            return new NumberEntry.FloatEntry(label, (ForgeConfigSpec.ConfigValue<Float>) value, spec);
        } else {
            return type instanceof Double ? new NumberEntry.DoubleEntry(label, (ForgeConfigSpec.ConfigValue<Double>) value, spec) : null;
        }
    }

    public NumberEntry(String label, ForgeConfigSpec.ConfigValue<T> value, ForgeConfigSpec.ValueSpec spec) {
        super(label, value, spec);
        if (this instanceof NumberEntry.IntegerEntry && this.annotations.containsKey("IntDisplay")) {
            String intDisplay = (String) this.annotations.get("IntDisplay");
            int intValue = (Integer) this.getValue();
            this.textField.setValue(switch(intDisplay) {
                case "#" ->
                    "#" + Integer.toHexString(intValue).toUpperCase(Locale.ROOT);
                case "0x" ->
                    "0x" + Integer.toHexString(intValue).toUpperCase(Locale.ROOT);
                case "0b" ->
                    "0b" + Integer.toBinaryString(intValue);
                default ->
                    String.valueOf(intValue);
            });
        } else {
            this.textField.setValue(String.valueOf(this.getValue()));
        }
        this.textField.setTextColor(Theme.i(Theme.Key.TEXT));
        Object range = spec.getRange();
        try {
            Field minField = range.getClass().getDeclaredField("min");
            Field maxField = range.getClass().getDeclaredField("max");
            minField.setAccessible(true);
            maxField.setAccessible(true);
            T min = (T) minField.get(range);
            T max = (T) maxField.get(range);
            Font font = Minecraft.getInstance().font;
            if (min.doubleValue() > this.getTypeMin().doubleValue()) {
                MutableComponent t = Components.literal(this.formatBound(min) + " < ");
                this.minText = new TextStencilElement(font, t).centered(true, false);
                this.minText.withElementRenderer((ms, width, height, alpha) -> UIRenderHelper.angledGradient(ms, 0.0F, 0, height / 2, height, width, Theme.p(Theme.Key.TEXT_DARKER)));
                this.minOffset = font.width(t);
            }
            if (max.doubleValue() < this.getTypeMax().doubleValue()) {
                MutableComponent t = Components.literal(" < " + this.formatBound(max));
                this.maxText = new TextStencilElement(font, t).centered(true, false);
                this.maxText.withElementRenderer((ms, width, height, alpha) -> UIRenderHelper.angledGradient(ms, 0.0F, 0, height / 2, height, width, Theme.p(Theme.Key.TEXT_DARKER)));
                this.maxOffset = font.width(t);
            }
        } catch (IllegalAccessException | ClassCastException | NullPointerException | NoSuchFieldException var11) {
        }
        this.textField.setResponder(s -> {
            try {
                T number = (T) this.getParser().apply(s);
                if (!spec.test(number)) {
                    throw new IllegalArgumentException();
                }
                this.textField.setTextColor(Theme.i(Theme.Key.TEXT));
                this.setValue(number);
            } catch (IllegalArgumentException var4x) {
                this.textField.setTextColor(Theme.i(Theme.Key.BUTTON_FAIL));
            }
        });
        this.textField.moveCursorToStart();
        this.listeners.add(this.textField);
        this.onReset();
    }

    protected String formatBound(T bound) {
        String sci = String.format("%.2E", bound.doubleValue());
        String str = String.valueOf(bound);
        return sci.length() < str.length() ? sci : str;
    }

    protected abstract T getTypeMin();

    protected abstract T getTypeMax();

    protected abstract Function<String, T> getParser();

    @Override
    protected void setEditable(boolean b) {
        super.setEditable(b);
        this.textField.setEditable(b);
    }

    public void onValueChange(T newValue) {
        super.onValueChange(newValue);
        try {
            T current = (T) this.getParser().apply(this.textField.getValue());
            if (!current.equals(newValue)) {
                this.textField.setValue(String.valueOf(newValue));
            }
        } catch (IllegalArgumentException var3) {
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.textField.tick();
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
        super.render(graphics, index, y, x, width, height, mouseX, mouseY, p_230432_9_, partialTicks);
        this.textField.m_252865_(x + width - 82 - 28);
        this.textField.m_253211_(y + 8);
        this.textField.m_93674_(Math.min(width - this.getLabelWidth(width) - 28 - this.minOffset - this.maxOffset, 40));
        this.textField.setHeight(20);
        this.textField.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (this.minText != null) {
            this.minText.<RenderElement>at((float) (this.textField.m_252754_() - this.minOffset), (float) this.textField.m_252907_(), 0.0F).<RenderElement>withBounds(this.minOffset, this.textField.m_93694_()).render(graphics);
        }
        if (this.maxText != null) {
            this.maxText.<RenderElement>at((float) (this.textField.m_252754_() + this.textField.m_5711_()), (float) this.textField.m_252907_(), 0.0F).<RenderElement>withBounds(this.maxOffset, this.textField.m_93694_()).render(graphics);
        }
    }

    public static class DoubleEntry extends NumberEntry<Double> {

        public DoubleEntry(String label, ForgeConfigSpec.ConfigValue<Double> value, ForgeConfigSpec.ValueSpec spec) {
            super(label, value, spec);
        }

        protected Double getTypeMin() {
            return -Float.MAX_VALUE;
        }

        protected Double getTypeMax() {
            return Float.MAX_VALUE;
        }

        @Override
        protected Function<String, Double> getParser() {
            return Double::parseDouble;
        }
    }

    public static class FloatEntry extends NumberEntry<Float> {

        public FloatEntry(String label, ForgeConfigSpec.ConfigValue<Float> value, ForgeConfigSpec.ValueSpec spec) {
            super(label, value, spec);
        }

        protected Float getTypeMin() {
            return -Float.MAX_VALUE;
        }

        protected Float getTypeMax() {
            return Float.MAX_VALUE;
        }

        @Override
        protected Function<String, Float> getParser() {
            return Float::parseFloat;
        }
    }

    public static class IntegerEntry extends NumberEntry<Integer> {

        public IntegerEntry(String label, ForgeConfigSpec.ConfigValue<Integer> value, ForgeConfigSpec.ValueSpec spec) {
            super(label, value, spec);
        }

        protected Integer getTypeMin() {
            return Integer.MIN_VALUE;
        }

        protected Integer getTypeMax() {
            return Integer.MAX_VALUE;
        }

        @Override
        protected Function<String, Integer> getParser() {
            return string -> {
                if (string.startsWith("#")) {
                    return Integer.parseUnsignedInt(string.substring(1), 16);
                } else if (string.startsWith("0x")) {
                    return Integer.parseUnsignedInt(string.substring(2), 16);
                } else {
                    return string.startsWith("0b") ? Integer.parseUnsignedInt(string.substring(2), 2) : Integer.parseInt(string);
                }
            };
        }
    }
}