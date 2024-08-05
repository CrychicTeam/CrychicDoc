package com.simibubi.create.foundation.blockEntity.behaviour;

import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;
import java.util.function.Function;
import net.minecraft.network.chat.MutableComponent;

public class ValueSettingsFormatter {

    private Function<ValueSettingsBehaviour.ValueSettings, MutableComponent> formatter;

    public ValueSettingsFormatter(Function<ValueSettingsBehaviour.ValueSettings, MutableComponent> formatter) {
        this.formatter = formatter;
    }

    public MutableComponent format(ValueSettingsBehaviour.ValueSettings valueSettings) {
        return (MutableComponent) this.formatter.apply(valueSettings);
    }

    public static class ScrollOptionSettingsFormatter extends ValueSettingsFormatter {

        private INamedIconOptions[] options;

        public ScrollOptionSettingsFormatter(INamedIconOptions[] options) {
            super(v -> Lang.translateDirect(options[v.value()].getTranslationKey()));
            this.options = options;
        }

        public AllIcons getIcon(ValueSettingsBehaviour.ValueSettings valueSettings) {
            return this.options[valueSettings.value()].getIcon();
        }
    }
}