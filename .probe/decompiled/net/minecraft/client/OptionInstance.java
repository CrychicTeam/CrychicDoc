package net.minecraft.client;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;
import org.slf4j.Logger;

public final class OptionInstance<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final OptionInstance.Enum<Boolean> BOOLEAN_VALUES = new OptionInstance.Enum<>(ImmutableList.of(Boolean.TRUE, Boolean.FALSE), Codec.BOOL);

    public static final OptionInstance.CaptionBasedToString<Boolean> BOOLEAN_TO_STRING = (p_231544_, p_231545_) -> p_231545_ ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF;

    private final OptionInstance.TooltipSupplier<T> tooltip;

    final Function<T, Component> toString;

    private final OptionInstance.ValueSet<T> values;

    private final Codec<T> codec;

    private final T initialValue;

    private final Consumer<T> onValueUpdate;

    final Component caption;

    T value;

    public static OptionInstance<Boolean> createBoolean(String string0, boolean boolean1, Consumer<Boolean> consumerBoolean2) {
        return createBoolean(string0, noTooltip(), boolean1, consumerBoolean2);
    }

    public static OptionInstance<Boolean> createBoolean(String string0, boolean boolean1) {
        return createBoolean(string0, noTooltip(), boolean1, p_231548_ -> {
        });
    }

    public static OptionInstance<Boolean> createBoolean(String string0, OptionInstance.TooltipSupplier<Boolean> optionInstanceTooltipSupplierBoolean1, boolean boolean2) {
        return createBoolean(string0, optionInstanceTooltipSupplierBoolean1, boolean2, p_231513_ -> {
        });
    }

    public static OptionInstance<Boolean> createBoolean(String string0, OptionInstance.TooltipSupplier<Boolean> optionInstanceTooltipSupplierBoolean1, boolean boolean2, Consumer<Boolean> consumerBoolean3) {
        return createBoolean(string0, optionInstanceTooltipSupplierBoolean1, BOOLEAN_TO_STRING, boolean2, consumerBoolean3);
    }

    public static OptionInstance<Boolean> createBoolean(String string0, OptionInstance.TooltipSupplier<Boolean> optionInstanceTooltipSupplierBoolean1, OptionInstance.CaptionBasedToString<Boolean> optionInstanceCaptionBasedToStringBoolean2, boolean boolean3, Consumer<Boolean> consumerBoolean4) {
        return new OptionInstance<>(string0, optionInstanceTooltipSupplierBoolean1, optionInstanceCaptionBasedToStringBoolean2, BOOLEAN_VALUES, boolean3, consumerBoolean4);
    }

    public OptionInstance(String string0, OptionInstance.TooltipSupplier<T> optionInstanceTooltipSupplierT1, OptionInstance.CaptionBasedToString<T> optionInstanceCaptionBasedToStringT2, OptionInstance.ValueSet<T> optionInstanceValueSetT3, T t4, Consumer<T> consumerT5) {
        this(string0, optionInstanceTooltipSupplierT1, optionInstanceCaptionBasedToStringT2, optionInstanceValueSetT3, optionInstanceValueSetT3.codec(), t4, consumerT5);
    }

    public OptionInstance(String string0, OptionInstance.TooltipSupplier<T> optionInstanceTooltipSupplierT1, OptionInstance.CaptionBasedToString<T> optionInstanceCaptionBasedToStringT2, OptionInstance.ValueSet<T> optionInstanceValueSetT3, Codec<T> codecT4, T t5, Consumer<T> consumerT6) {
        this.caption = Component.translatable(string0);
        this.tooltip = optionInstanceTooltipSupplierT1;
        this.toString = p_231506_ -> optionInstanceCaptionBasedToStringT2.toString(this.caption, (T) p_231506_);
        this.values = optionInstanceValueSetT3;
        this.codec = codecT4;
        this.initialValue = t5;
        this.onValueUpdate = consumerT6;
        this.value = this.initialValue;
    }

    public static <T> OptionInstance.TooltipSupplier<T> noTooltip() {
        return p_258114_ -> null;
    }

    public static <T> OptionInstance.TooltipSupplier<T> cachedConstantTooltip(Component component0) {
        return p_258116_ -> Tooltip.create(component0);
    }

    public static <T extends OptionEnum> OptionInstance.CaptionBasedToString<T> forOptionEnum() {
        return (p_231538_, p_231539_) -> p_231539_.getCaption();
    }

    public AbstractWidget createButton(Options options0, int int1, int int2, int int3) {
        return this.createButton(options0, int1, int2, int3, p_261336_ -> {
        });
    }

    public AbstractWidget createButton(Options options0, int int1, int int2, int int3, Consumer<T> consumerT4) {
        return (AbstractWidget) this.values.createButton(this.tooltip, options0, int1, int2, int3, consumerT4).apply(this);
    }

    public T get() {
        return this.value;
    }

    public Codec<T> codec() {
        return this.codec;
    }

    public String toString() {
        return this.caption.getString();
    }

    public void set(T t0) {
        T $$1 = (T) this.values.validateValue(t0).orElseGet(() -> {
            LOGGER.error("Illegal option value " + t0 + " for " + this.caption);
            return this.initialValue;
        });
        if (!Minecraft.getInstance().isRunning()) {
            this.value = $$1;
        } else {
            if (!Objects.equals(this.value, $$1)) {
                this.value = $$1;
                this.onValueUpdate.accept(this.value);
            }
        }
    }

    public OptionInstance.ValueSet<T> values() {
        return this.values;
    }

    public static record AltEnum<T>(List<T> f_231557_, List<T> f_231558_, BooleanSupplier f_231559_, OptionInstance.CycleableValueSet.ValueSetter<T> f_231560_, Codec<T> f_231561_) implements OptionInstance.CycleableValueSet<T> {

        private final List<T> values;

        private final List<T> altValues;

        private final BooleanSupplier altCondition;

        private final OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter;

        private final Codec<T> codec;

        public AltEnum(List<T> f_231557_, List<T> f_231558_, BooleanSupplier f_231559_, OptionInstance.CycleableValueSet.ValueSetter<T> f_231560_, Codec<T> f_231561_) {
            this.values = f_231557_;
            this.altValues = f_231558_;
            this.altCondition = f_231559_;
            this.valueSetter = f_231560_;
            this.codec = f_231561_;
        }

        @Override
        public CycleButton.ValueListSupplier<T> valueListSupplier() {
            return CycleButton.ValueListSupplier.create(this.altCondition, this.values, this.altValues);
        }

        @Override
        public Optional<T> validateValue(T p_231570_) {
            return (this.altCondition.getAsBoolean() ? this.altValues : this.values).contains(p_231570_) ? Optional.of(p_231570_) : Optional.empty();
        }
    }

    public interface CaptionBasedToString<T> {

        Component toString(Component var1, T var2);
    }

    public static record ClampingLazyMaxIntRange(int f_231583_, IntSupplier f_231584_, int f_276069_) implements OptionInstance.IntRangeBase, OptionInstance.SliderableOrCyclableValueSet<Integer> {

        private final int minInclusive;

        private final IntSupplier maxSupplier;

        private final int encodableMaxInclusive;

        public ClampingLazyMaxIntRange(int f_231583_, IntSupplier f_231584_, int f_276069_) {
            this.minInclusive = f_231583_;
            this.maxSupplier = f_231584_;
            this.encodableMaxInclusive = f_276069_;
        }

        public Optional<Integer> validateValue(Integer p_231590_) {
            return Optional.of(Mth.clamp(p_231590_, this.minInclusive(), this.maxInclusive()));
        }

        @Override
        public int maxInclusive() {
            return this.maxSupplier.getAsInt();
        }

        @Override
        public Codec<Integer> codec() {
            return ExtraCodecs.validate(Codec.INT, p_276098_ -> {
                int $$1 = this.encodableMaxInclusive + 1;
                return p_276098_.compareTo(this.minInclusive) >= 0 && p_276098_.compareTo($$1) <= 0 ? DataResult.success(p_276098_) : DataResult.error(() -> "Value " + p_276098_ + " outside of range [" + this.minInclusive + ":" + $$1 + "]", p_276098_);
            });
        }

        @Override
        public boolean createCycleButton() {
            return true;
        }

        @Override
        public CycleButton.ValueListSupplier<Integer> valueListSupplier() {
            return CycleButton.ValueListSupplier.create(IntStream.range(this.minInclusive, this.maxInclusive() + 1).boxed().toList());
        }
    }

    interface CycleableValueSet<T> extends OptionInstance.ValueSet<T> {

        CycleButton.ValueListSupplier<T> valueListSupplier();

        default OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter() {
            return OptionInstance::m_231514_;
        }

        @Override
        default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> optionInstanceTooltipSupplierT0, Options options1, int int2, int int3, int int4, Consumer<T> consumerT5) {
            return p_261343_ -> CycleButton.<T>builder(p_261343_.toString).withValues(this.valueListSupplier()).withTooltip(optionInstanceTooltipSupplierT0).withInitialValue(p_261343_.value).create(int2, int3, int4, 20, p_261343_.caption, (p_261347_, p_261348_) -> {
                this.valueSetter().set(p_261343_, (T) p_261348_);
                options1.save();
                consumerT5.accept(p_261348_);
            });
        }

        public interface ValueSetter<T> {

            void set(OptionInstance<T> var1, T var2);
        }
    }

    public static record Enum<T>(List<T> f_231625_, Codec<T> f_231626_) implements OptionInstance.CycleableValueSet<T> {

        private final List<T> values;

        private final Codec<T> codec;

        public Enum(List<T> f_231625_, Codec<T> f_231626_) {
            this.values = f_231625_;
            this.codec = f_231626_;
        }

        @Override
        public Optional<T> validateValue(T p_231632_) {
            return this.values.contains(p_231632_) ? Optional.of(p_231632_) : Optional.empty();
        }

        @Override
        public CycleButton.ValueListSupplier<T> valueListSupplier() {
            return CycleButton.ValueListSupplier.create(this.values);
        }
    }

    public static record IntRange(int f_231639_, int f_231640_) implements OptionInstance.IntRangeBase {

        private final int minInclusive;

        private final int maxInclusive;

        public IntRange(int f_231639_, int f_231640_) {
            this.minInclusive = f_231639_;
            this.maxInclusive = f_231640_;
        }

        public Optional<Integer> validateValue(Integer p_231645_) {
            return p_231645_.compareTo(this.minInclusive()) >= 0 && p_231645_.compareTo(this.maxInclusive()) <= 0 ? Optional.of(p_231645_) : Optional.empty();
        }

        @Override
        public Codec<Integer> codec() {
            return Codec.intRange(this.minInclusive, this.maxInclusive + 1);
        }
    }

    interface IntRangeBase extends OptionInstance.SliderableValueSet<Integer> {

        int minInclusive();

        int maxInclusive();

        default double toSliderValue(Integer integer0) {
            return (double) Mth.map((float) integer0.intValue(), (float) this.minInclusive(), (float) this.maxInclusive(), 0.0F, 1.0F);
        }

        default Integer fromSliderValue(double double0) {
            return Mth.floor(Mth.map(double0, 0.0, 1.0, (double) this.minInclusive(), (double) this.maxInclusive()));
        }

        default <R> OptionInstance.SliderableValueSet<R> xmap(final IntFunction<? extends R> intFunctionExtendsR0, final ToIntFunction<? super R> toIntFunctionSuperR1) {
            return new OptionInstance.SliderableValueSet<R>() {

                @Override
                public Optional<R> validateValue(R p_231674_) {
                    return IntRangeBase.this.m_214064_(toIntFunctionSuperR1.applyAsInt(p_231674_)).map(intFunctionExtendsR0::apply);
                }

                @Override
                public double toSliderValue(R p_231678_) {
                    return IntRangeBase.this.toSliderValue(toIntFunctionSuperR1.applyAsInt(p_231678_));
                }

                @Override
                public R fromSliderValue(double p_231676_) {
                    return (R) intFunctionExtendsR0.apply(IntRangeBase.this.fromSliderValue(p_231676_));
                }

                @Override
                public Codec<R> codec() {
                    return IntRangeBase.this.m_213664_().xmap(intFunctionExtendsR0::apply, toIntFunctionSuperR1::applyAsInt);
                }
            };
        }
    }

    public static record LazyEnum<T>(Supplier<List<T>> f_231680_, Function<T, Optional<T>> f_231681_, Codec<T> f_231682_) implements OptionInstance.CycleableValueSet<T> {

        private final Supplier<List<T>> values;

        private final Function<T, Optional<T>> validateValue;

        private final Codec<T> codec;

        public LazyEnum(Supplier<List<T>> f_231680_, Function<T, Optional<T>> f_231681_, Codec<T> f_231682_) {
            this.values = f_231680_;
            this.validateValue = f_231681_;
            this.codec = f_231682_;
        }

        @Override
        public Optional<T> validateValue(T p_231689_) {
            return (Optional<T>) this.validateValue.apply(p_231689_);
        }

        @Override
        public CycleButton.ValueListSupplier<T> valueListSupplier() {
            return CycleButton.ValueListSupplier.create((Collection<T>) this.values.get());
        }
    }

    static final class OptionInstanceSliderButton<N> extends AbstractOptionSliderButton {

        private final OptionInstance<N> instance;

        private final OptionInstance.SliderableValueSet<N> values;

        private final OptionInstance.TooltipSupplier<N> tooltipSupplier;

        private final Consumer<N> onValueChanged;

        OptionInstanceSliderButton(Options options0, int int1, int int2, int int3, int int4, OptionInstance<N> optionInstanceN5, OptionInstance.SliderableValueSet<N> optionInstanceSliderableValueSetN6, OptionInstance.TooltipSupplier<N> optionInstanceTooltipSupplierN7, Consumer<N> consumerN8) {
            super(options0, int1, int2, int3, int4, optionInstanceSliderableValueSetN6.toSliderValue(optionInstanceN5.get()));
            this.instance = optionInstanceN5;
            this.values = optionInstanceSliderableValueSetN6;
            this.tooltipSupplier = optionInstanceTooltipSupplierN7;
            this.onValueChanged = consumerN8;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.m_93666_((Component) this.instance.toString.apply(this.instance.get()));
            this.m_257544_(this.tooltipSupplier.apply(this.values.fromSliderValue(this.f_93577_)));
        }

        @Override
        protected void applyValue() {
            this.instance.set(this.values.fromSliderValue(this.f_93577_));
            this.f_93377_.save();
            this.onValueChanged.accept(this.instance.get());
        }
    }

    interface SliderableOrCyclableValueSet<T> extends OptionInstance.CycleableValueSet<T>, OptionInstance.SliderableValueSet<T> {

        boolean createCycleButton();

        @Override
        default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> optionInstanceTooltipSupplierT0, Options options1, int int2, int int3, int int4, Consumer<T> consumerT5) {
            return this.createCycleButton() ? OptionInstance.CycleableValueSet.super.createButton(optionInstanceTooltipSupplierT0, options1, int2, int3, int4, consumerT5) : OptionInstance.SliderableValueSet.super.createButton(optionInstanceTooltipSupplierT0, options1, int2, int3, int4, consumerT5);
        }
    }

    interface SliderableValueSet<T> extends OptionInstance.ValueSet<T> {

        double toSliderValue(T var1);

        T fromSliderValue(double var1);

        @Override
        default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> optionInstanceTooltipSupplierT0, Options options1, int int2, int int3, int int4, Consumer<T> consumerT5) {
            return p_261355_ -> new OptionInstance.OptionInstanceSliderButton<>(options1, int2, int3, int4, 20, p_261355_, this, optionInstanceTooltipSupplierT0, consumerT5);
        }
    }

    @FunctionalInterface
    public interface TooltipSupplier<T> {

        @Nullable
        Tooltip apply(T var1);
    }

    public static enum UnitDouble implements OptionInstance.SliderableValueSet<Double> {

        INSTANCE;

        public Optional<Double> validateValue(Double p_231747_) {
            return p_231747_ >= 0.0 && p_231747_ <= 1.0 ? Optional.of(p_231747_) : Optional.empty();
        }

        public double toSliderValue(Double p_231756_) {
            return p_231756_;
        }

        public Double fromSliderValue(double p_231741_) {
            return p_231741_;
        }

        public <R> OptionInstance.SliderableValueSet<R> xmap(final DoubleFunction<? extends R> p_231751_, final ToDoubleFunction<? super R> p_231752_) {
            return new OptionInstance.SliderableValueSet<R>() {

                @Override
                public Optional<R> validateValue(R p_231773_) {
                    return UnitDouble.this.validateValue(p_231752_.applyAsDouble(p_231773_)).map(p_231751_::apply);
                }

                @Override
                public double toSliderValue(R p_231777_) {
                    return UnitDouble.this.toSliderValue(p_231752_.applyAsDouble(p_231777_));
                }

                @Override
                public R fromSliderValue(double p_231775_) {
                    return (R) p_231751_.apply(UnitDouble.this.fromSliderValue(p_231775_));
                }

                @Override
                public Codec<R> codec() {
                    return UnitDouble.this.codec().xmap(p_231751_::apply, p_231752_::applyAsDouble);
                }
            };
        }

        @Override
        public Codec<Double> codec() {
            return Codec.either(Codec.doubleRange(0.0, 1.0), Codec.BOOL).xmap(p_231743_ -> (Double) p_231743_.map(p_231760_ -> p_231760_, p_231745_ -> p_231745_ ? 1.0 : 0.0), Either::left);
        }
    }

    interface ValueSet<T> {

        Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> var1, Options var2, int var3, int var4, int var5, Consumer<T> var6);

        Optional<T> validateValue(T var1);

        Codec<T> codec();
    }
}