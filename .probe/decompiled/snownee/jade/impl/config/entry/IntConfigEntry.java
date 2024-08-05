package snownee.jade.impl.config.entry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import snownee.jade.gui.config.OptionsList;
import snownee.jade.gui.config.value.InputOptionValue;
import snownee.jade.gui.config.value.OptionValue;
import snownee.jade.impl.config.PluginConfig;

public class IntConfigEntry extends ConfigEntry<Integer> {

    private boolean slider;

    private int min;

    private int max;

    public IntConfigEntry(ResourceLocation id, int defaultValue, int min, int max, boolean slider) {
        super(id, defaultValue);
        this.slider = slider;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValidValue(Object value) {
        return value instanceof Number && ((Number) value).intValue() >= this.min && ((Number) value).intValue() <= this.max;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(((Number) value).intValue());
    }

    @Override
    public OptionValue<?> createUI(OptionsList options, String optionName) {
        return this.slider ? options.slider(optionName, (float) this.getValue().intValue(), f -> PluginConfig.INSTANCE.set(this.id, (int) f.floatValue()), (float) this.min, (float) this.max, f -> (float) Math.round(f)) : options.input(optionName, this.getValue(), i -> PluginConfig.INSTANCE.set(this.id, Mth.clamp(i, this.min, this.max)), InputOptionValue.INTEGER.and($ -> this.isValidValue(Integer.valueOf($))));
    }
}