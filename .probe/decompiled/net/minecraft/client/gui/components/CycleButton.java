package net.minecraft.client.gui.components;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

public class CycleButton<T> extends AbstractButton {

    public static final BooleanSupplier DEFAULT_ALT_LIST_SELECTOR = Screen::m_96639_;

    private static final List<Boolean> BOOLEAN_OPTIONS = ImmutableList.of(Boolean.TRUE, Boolean.FALSE);

    private final Component name;

    private int index;

    private T value;

    private final CycleButton.ValueListSupplier<T> values;

    private final Function<T, Component> valueStringifier;

    private final Function<CycleButton<T>, MutableComponent> narrationProvider;

    private final CycleButton.OnValueChange<T> onValueChange;

    private final boolean displayOnlyValue;

    private final OptionInstance.TooltipSupplier<T> tooltipSupplier;

    CycleButton(int int0, int int1, int int2, int int3, Component component4, Component component5, int int6, T t7, CycleButton.ValueListSupplier<T> cycleButtonValueListSupplierT8, Function<T, Component> functionTComponent9, Function<CycleButton<T>, MutableComponent> functionCycleButtonTMutableComponent10, CycleButton.OnValueChange<T> cycleButtonOnValueChangeT11, OptionInstance.TooltipSupplier<T> optionInstanceTooltipSupplierT12, boolean boolean13) {
        super(int0, int1, int2, int3, component4);
        this.name = component5;
        this.index = int6;
        this.value = t7;
        this.values = cycleButtonValueListSupplierT8;
        this.valueStringifier = functionTComponent9;
        this.narrationProvider = functionCycleButtonTMutableComponent10;
        this.onValueChange = cycleButtonOnValueChangeT11;
        this.displayOnlyValue = boolean13;
        this.tooltipSupplier = optionInstanceTooltipSupplierT12;
        this.updateTooltip();
    }

    private void updateTooltip() {
        this.m_257544_(this.tooltipSupplier.apply(this.value));
    }

    @Override
    public void onPress() {
        if (Screen.hasShiftDown()) {
            this.cycleValue(-1);
        } else {
            this.cycleValue(1);
        }
    }

    private void cycleValue(int int0) {
        List<T> $$1 = this.values.getSelectedList();
        this.index = Mth.positiveModulo(this.index + int0, $$1.size());
        T $$2 = (T) $$1.get(this.index);
        this.updateValue($$2);
        this.onValueChange.onValueChange(this, $$2);
    }

    private T getCycledValue(int int0) {
        List<T> $$1 = this.values.getSelectedList();
        return (T) $$1.get(Mth.positiveModulo(this.index + int0, $$1.size()));
    }

    @Override
    public boolean mouseScrolled(double double0, double double1, double double2) {
        if (double2 > 0.0) {
            this.cycleValue(-1);
        } else if (double2 < 0.0) {
            this.cycleValue(1);
        }
        return true;
    }

    public void setValue(T t0) {
        List<T> $$1 = this.values.getSelectedList();
        int $$2 = $$1.indexOf(t0);
        if ($$2 != -1) {
            this.index = $$2;
        }
        this.updateValue(t0);
    }

    private void updateValue(T t0) {
        Component $$1 = this.createLabelForValue(t0);
        this.m_93666_($$1);
        this.value = t0;
        this.updateTooltip();
    }

    private Component createLabelForValue(T t0) {
        return (Component) (this.displayOnlyValue ? (Component) this.valueStringifier.apply(t0) : this.createFullName(t0));
    }

    private MutableComponent createFullName(T t0) {
        return CommonComponents.optionNameValue(this.name, (Component) this.valueStringifier.apply(t0));
    }

    public T getValue() {
        return this.value;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return (MutableComponent) this.narrationProvider.apply(this);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.f_93623_) {
            T $$1 = this.getCycledValue(1);
            Component $$2 = this.createLabelForValue($$1);
            if (this.m_93696_()) {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.focused", $$2));
            } else {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.hovered", $$2));
            }
        }
    }

    public MutableComponent createDefaultNarrationMessage() {
        return m_168799_((Component) (this.displayOnlyValue ? this.createFullName(this.value) : this.m_6035_()));
    }

    public static <T> CycleButton.Builder<T> builder(Function<T, Component> functionTComponent0) {
        return new CycleButton.Builder<>(functionTComponent0);
    }

    public static CycleButton.Builder<Boolean> booleanBuilder(Component component0, Component component1) {
        return new CycleButton.Builder<Boolean>(p_168902_ -> p_168902_ ? component0 : component1).withValues(BOOLEAN_OPTIONS);
    }

    public static CycleButton.Builder<Boolean> onOffBuilder() {
        return new CycleButton.Builder<Boolean>(p_168891_ -> p_168891_ ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF).withValues(BOOLEAN_OPTIONS);
    }

    public static CycleButton.Builder<Boolean> onOffBuilder(boolean boolean0) {
        return onOffBuilder().withInitialValue(boolean0);
    }

    public static class Builder<T> {

        private int initialIndex;

        @Nullable
        private T initialValue;

        private final Function<T, Component> valueStringifier;

        private OptionInstance.TooltipSupplier<T> tooltipSupplier = p_168964_ -> null;

        private Function<CycleButton<T>, MutableComponent> narrationProvider = CycleButton::m_168904_;

        private CycleButton.ValueListSupplier<T> values = CycleButton.ValueListSupplier.create(ImmutableList.of());

        private boolean displayOnlyValue;

        public Builder(Function<T, Component> functionTComponent0) {
            this.valueStringifier = functionTComponent0;
        }

        public CycleButton.Builder<T> withValues(Collection<T> collectionT0) {
            return this.withValues(CycleButton.ValueListSupplier.create(collectionT0));
        }

        @SafeVarargs
        public final CycleButton.Builder<T> withValues(T... t0) {
            return this.withValues(ImmutableList.copyOf(t0));
        }

        public CycleButton.Builder<T> withValues(List<T> listT0, List<T> listT1) {
            return this.withValues(CycleButton.ValueListSupplier.create(CycleButton.DEFAULT_ALT_LIST_SELECTOR, listT0, listT1));
        }

        public CycleButton.Builder<T> withValues(BooleanSupplier booleanSupplier0, List<T> listT1, List<T> listT2) {
            return this.withValues(CycleButton.ValueListSupplier.create(booleanSupplier0, listT1, listT2));
        }

        public CycleButton.Builder<T> withValues(CycleButton.ValueListSupplier<T> cycleButtonValueListSupplierT0) {
            this.values = cycleButtonValueListSupplierT0;
            return this;
        }

        public CycleButton.Builder<T> withTooltip(OptionInstance.TooltipSupplier<T> optionInstanceTooltipSupplierT0) {
            this.tooltipSupplier = optionInstanceTooltipSupplierT0;
            return this;
        }

        public CycleButton.Builder<T> withInitialValue(T t0) {
            this.initialValue = t0;
            int $$1 = this.values.getDefaultList().indexOf(t0);
            if ($$1 != -1) {
                this.initialIndex = $$1;
            }
            return this;
        }

        public CycleButton.Builder<T> withCustomNarration(Function<CycleButton<T>, MutableComponent> functionCycleButtonTMutableComponent0) {
            this.narrationProvider = functionCycleButtonTMutableComponent0;
            return this;
        }

        public CycleButton.Builder<T> displayOnlyValue() {
            this.displayOnlyValue = true;
            return this;
        }

        public CycleButton<T> create(int int0, int int1, int int2, int int3, Component component4) {
            return this.create(int0, int1, int2, int3, component4, (p_168946_, p_168947_) -> {
            });
        }

        public CycleButton<T> create(int int0, int int1, int int2, int int3, Component component4, CycleButton.OnValueChange<T> cycleButtonOnValueChangeT5) {
            List<T> $$6 = this.values.getDefaultList();
            if ($$6.isEmpty()) {
                throw new IllegalStateException("No values for cycle button");
            } else {
                T $$7 = (T) (this.initialValue != null ? this.initialValue : $$6.get(this.initialIndex));
                Component $$8 = (Component) this.valueStringifier.apply($$7);
                Component $$9 = (Component) (this.displayOnlyValue ? $$8 : CommonComponents.optionNameValue(component4, $$8));
                return new CycleButton<>(int0, int1, int2, int3, $$9, component4, this.initialIndex, $$7, this.values, this.valueStringifier, this.narrationProvider, cycleButtonOnValueChangeT5, this.tooltipSupplier, this.displayOnlyValue);
            }
        }
    }

    public interface OnValueChange<T> {

        void onValueChange(CycleButton<T> var1, T var2);
    }

    public interface ValueListSupplier<T> {

        List<T> getSelectedList();

        List<T> getDefaultList();

        static <T> CycleButton.ValueListSupplier<T> create(Collection<T> collectionT0) {
            final List<T> $$1 = ImmutableList.copyOf(collectionT0);
            return new CycleButton.ValueListSupplier<T>() {

                @Override
                public List<T> getSelectedList() {
                    return $$1;
                }

                @Override
                public List<T> getDefaultList() {
                    return $$1;
                }
            };
        }

        static <T> CycleButton.ValueListSupplier<T> create(final BooleanSupplier booleanSupplier0, List<T> listT1, List<T> listT2) {
            final List<T> $$3 = ImmutableList.copyOf(listT1);
            final List<T> $$4 = ImmutableList.copyOf(listT2);
            return new CycleButton.ValueListSupplier<T>() {

                @Override
                public List<T> getSelectedList() {
                    return booleanSupplier0.getAsBoolean() ? $$4 : $$3;
                }

                @Override
                public List<T> getDefaultList() {
                    return $$3;
                }
            };
        }
    }
}