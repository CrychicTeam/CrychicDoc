package de.keksuccino.fancymenu.util.cycle;

import de.keksuccino.fancymenu.util.enums.LocalizedEnum;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonCycles {

    @NotNull
    public static LocalizedEnumValueCycle<CommonCycles.CycleOnOff> cycleOnOff(@NotNull String cycleLocalizationKey) {
        return LocalizedEnumValueCycle.ofArray(cycleLocalizationKey, CommonCycles.CycleOnOff.ON, CommonCycles.CycleOnOff.OFF);
    }

    @NotNull
    public static LocalizedEnumValueCycle<CommonCycles.CycleOnOff> cycleOnOff(@NotNull String cycleLocalizationKey, @NotNull CommonCycles.CycleOnOff selectedValue) {
        return (LocalizedEnumValueCycle<CommonCycles.CycleOnOff>) LocalizedEnumValueCycle.ofArray(cycleLocalizationKey, CommonCycles.CycleOnOff.ON, CommonCycles.CycleOnOff.OFF).setCurrentValue(selectedValue);
    }

    @NotNull
    public static LocalizedEnumValueCycle<CommonCycles.CycleOnOff> cycleOnOff(@NotNull String cycleLocalizationKey, boolean selectedValue) {
        return (LocalizedEnumValueCycle<CommonCycles.CycleOnOff>) LocalizedEnumValueCycle.ofArray(cycleLocalizationKey, CommonCycles.CycleOnOff.ON, CommonCycles.CycleOnOff.OFF).setCurrentValue(CommonCycles.CycleOnOff.getByBoolean(selectedValue));
    }

    @NotNull
    public static LocalizedEnumValueCycle<CommonCycles.CycleEnabledDisabled> cycleEnabledDisabled(@NotNull String cycleLocalizationKey) {
        return LocalizedEnumValueCycle.ofArray(cycleLocalizationKey, CommonCycles.CycleEnabledDisabled.ENABLED, CommonCycles.CycleEnabledDisabled.DISABLED);
    }

    @NotNull
    public static LocalizedEnumValueCycle<CommonCycles.CycleEnabledDisabled> cycleEnabledDisabled(@NotNull String cycleLocalizationKey, @NotNull CommonCycles.CycleEnabledDisabled selectedValue) {
        return (LocalizedEnumValueCycle<CommonCycles.CycleEnabledDisabled>) LocalizedEnumValueCycle.ofArray(cycleLocalizationKey, CommonCycles.CycleEnabledDisabled.ENABLED, CommonCycles.CycleEnabledDisabled.DISABLED).setCurrentValue(selectedValue);
    }

    @NotNull
    public static LocalizedEnumValueCycle<CommonCycles.CycleEnabledDisabled> cycleEnabledDisabled(@NotNull String cycleLocalizationKey, boolean selectedValue) {
        return (LocalizedEnumValueCycle<CommonCycles.CycleEnabledDisabled>) LocalizedEnumValueCycle.ofArray(cycleLocalizationKey, CommonCycles.CycleEnabledDisabled.ENABLED, CommonCycles.CycleEnabledDisabled.DISABLED).setCurrentValue(CommonCycles.CycleEnabledDisabled.getByBoolean(selectedValue));
    }

    @NotNull
    public static <T> LocalizedGenericValueCycle<T> cycleOrangeValue(@NotNull String cycleLocalizationKey, @NotNull List<T> values) {
        return (LocalizedGenericValueCycle<T>) LocalizedGenericValueCycle.of(cycleLocalizationKey, values.toArray()).setValueComponentStyleSupplier(consumes -> (Style) LocalizedEnum.WARNING_TEXT_STYLE.get());
    }

    @NotNull
    public static <T> LocalizedGenericValueCycle<T> cycleOrangeValue(@NotNull String cycleLocalizationKey, @NotNull List<T> values, @NotNull T selectedValue) {
        return (LocalizedGenericValueCycle<T>) cycleOrangeValue(cycleLocalizationKey, values).setCurrentValue(selectedValue);
    }

    @NotNull
    public static <T> LocalizedGenericValueCycle<T> cycle(@NotNull String cycleLocalizationKey, @NotNull List<T> values) {
        return LocalizedGenericValueCycle.of(cycleLocalizationKey, (T[]) values.toArray());
    }

    @NotNull
    public static <T> LocalizedGenericValueCycle<T> cycle(@NotNull String cycleLocalizationKey, @NotNull List<T> values, @NotNull T selectedValue) {
        return (LocalizedGenericValueCycle<T>) cycle(cycleLocalizationKey, values).setCurrentValue(selectedValue);
    }

    public static enum CycleEnabledDisabled implements LocalizedEnum<CommonCycles.CycleEnabledDisabled> {

        ENABLED("enabled", true, LocalizedEnum.SUCCESS_TEXT_STYLE), DISABLED("disabled", false, LocalizedEnum.ERROR_TEXT_STYLE);

        final String name;

        final Supplier<Style> style;

        final boolean valueBoolean;

        private CycleEnabledDisabled(String name, boolean valueBoolean, Supplier<Style> style) {
            this.name = name;
            this.style = style;
            this.valueBoolean = valueBoolean;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.general.cycle.enabled_disabled";
        }

        public boolean getAsBoolean() {
            return this.valueBoolean;
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public CommonCycles.CycleEnabledDisabled[] getValues() {
            return values();
        }

        @Nullable
        public CommonCycles.CycleEnabledDisabled getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @NotNull
        @Override
        public Style getValueComponentStyle() {
            return (Style) this.style.get();
        }

        public static CommonCycles.CycleEnabledDisabled getByBoolean(boolean b) {
            return b ? ENABLED : DISABLED;
        }

        @Nullable
        public static CommonCycles.CycleEnabledDisabled getByName(@NotNull String name) {
            for (CommonCycles.CycleEnabledDisabled e : values()) {
                if (e.getName().equals(name)) {
                    return e;
                }
            }
            return null;
        }
    }

    public static enum CycleOnOff implements LocalizedEnum<CommonCycles.CycleOnOff> {

        ON("on", true, LocalizedEnum.SUCCESS_TEXT_STYLE), OFF("off", false, LocalizedEnum.ERROR_TEXT_STYLE);

        final String name;

        final Supplier<Style> style;

        final boolean valueBoolean;

        private CycleOnOff(String name, boolean valueBoolean, Supplier<Style> style) {
            this.name = name;
            this.style = style;
            this.valueBoolean = valueBoolean;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.general.cycle.on_off";
        }

        public boolean getAsBoolean() {
            return this.valueBoolean;
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public CommonCycles.CycleOnOff[] getValues() {
            return values();
        }

        @Nullable
        public CommonCycles.CycleOnOff getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @NotNull
        @Override
        public Style getValueComponentStyle() {
            return (Style) this.style.get();
        }

        public static CommonCycles.CycleOnOff getByBoolean(boolean b) {
            return b ? ON : OFF;
        }

        @Nullable
        public static CommonCycles.CycleOnOff getByName(@NotNull String name) {
            for (CommonCycles.CycleOnOff e : values()) {
                if (e.getName().equals(name)) {
                    return e;
                }
            }
            return null;
        }
    }
}