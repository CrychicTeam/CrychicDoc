package net.minecraft.data.models.blockstates;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class PropertyDispatch {

    private final Map<Selector, List<Variant>> values = Maps.newHashMap();

    protected void putValue(Selector selector0, List<Variant> listVariant1) {
        List<Variant> $$2 = (List<Variant>) this.values.put(selector0, listVariant1);
        if ($$2 != null) {
            throw new IllegalStateException("Value " + selector0 + " is already defined");
        }
    }

    Map<Selector, List<Variant>> getEntries() {
        this.verifyComplete();
        return ImmutableMap.copyOf(this.values);
    }

    private void verifyComplete() {
        List<Property<?>> $$0 = this.getDefinedProperties();
        Stream<Selector> $$1 = Stream.of(Selector.empty());
        for (Property<?> $$2 : $$0) {
            $$1 = $$1.flatMap(p_125316_ -> $$2.getAllValues().map(p_125316_::m_125486_));
        }
        List<Selector> $$3 = (List<Selector>) $$1.filter(p_125318_ -> !this.values.containsKey(p_125318_)).collect(Collectors.toList());
        if (!$$3.isEmpty()) {
            throw new IllegalStateException("Missing definition for properties: " + $$3);
        }
    }

    abstract List<Property<?>> getDefinedProperties();

    public static <T1 extends Comparable<T1>> PropertyDispatch.C1<T1> property(Property<T1> propertyT0) {
        return new PropertyDispatch.C1<>(propertyT0);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> PropertyDispatch.C2<T1, T2> properties(Property<T1> propertyT0, Property<T2> propertyT1) {
        return new PropertyDispatch.C2<>(propertyT0, propertyT1);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> PropertyDispatch.C3<T1, T2, T3> properties(Property<T1> propertyT0, Property<T2> propertyT1, Property<T3> propertyT2) {
        return new PropertyDispatch.C3<>(propertyT0, propertyT1, propertyT2);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> PropertyDispatch.C4<T1, T2, T3, T4> properties(Property<T1> propertyT0, Property<T2> propertyT1, Property<T3> propertyT2, Property<T4> propertyT3) {
        return new PropertyDispatch.C4<>(propertyT0, propertyT1, propertyT2, propertyT3);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> PropertyDispatch.C5<T1, T2, T3, T4, T5> properties(Property<T1> propertyT0, Property<T2> propertyT1, Property<T3> propertyT2, Property<T4> propertyT3, Property<T5> propertyT4) {
        return new PropertyDispatch.C5<>(propertyT0, propertyT1, propertyT2, propertyT3, propertyT4);
    }

    public static class C1<T1 extends Comparable<T1>> extends PropertyDispatch {

        private final Property<T1> property1;

        C1(Property<T1> propertyT0) {
            this.property1 = propertyT0;
        }

        @Override
        public List<Property<?>> getDefinedProperties() {
            return ImmutableList.of(this.property1);
        }

        public PropertyDispatch.C1<T1> select(T1 t0, List<Variant> listVariant1) {
            Selector $$2 = Selector.of(this.property1.value(t0));
            this.m_125319_($$2, listVariant1);
            return this;
        }

        public PropertyDispatch.C1<T1> select(T1 t0, Variant variant1) {
            return this.select(t0, Collections.singletonList(variant1));
        }

        public PropertyDispatch generate(Function<T1, Variant> functionTVariant0) {
            this.property1.getPossibleValues().forEach(p_125340_ -> this.select((T1) p_125340_, (Variant) functionTVariant0.apply(p_125340_)));
            return this;
        }

        public PropertyDispatch generateList(Function<T1, List<Variant>> functionTListVariant0) {
            this.property1.getPossibleValues().forEach(p_176312_ -> this.select((T1) p_176312_, (List<Variant>) functionTListVariant0.apply(p_176312_)));
            return this;
        }
    }

    public static class C2<T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends PropertyDispatch {

        private final Property<T1> property1;

        private final Property<T2> property2;

        C2(Property<T1> propertyT0, Property<T2> propertyT1) {
            this.property1 = propertyT0;
            this.property2 = propertyT1;
        }

        @Override
        public List<Property<?>> getDefinedProperties() {
            return ImmutableList.of(this.property1, this.property2);
        }

        public PropertyDispatch.C2<T1, T2> select(T1 t0, T2 t1, List<Variant> listVariant2) {
            Selector $$3 = Selector.of(this.property1.value(t0), this.property2.value(t1));
            this.m_125319_($$3, listVariant2);
            return this;
        }

        public PropertyDispatch.C2<T1, T2> select(T1 t0, T2 t1, Variant variant2) {
            return this.select(t0, t1, Collections.singletonList(variant2));
        }

        public PropertyDispatch generate(BiFunction<T1, T2, Variant> biFunctionTTVariant0) {
            this.property1.getPossibleValues().forEach(p_125376_ -> this.property2.getPossibleValues().forEach(p_176322_ -> this.select((T1) p_125376_, (T2) p_176322_, (Variant) biFunctionTTVariant0.apply(p_125376_, p_176322_))));
            return this;
        }

        public PropertyDispatch generateList(BiFunction<T1, T2, List<Variant>> biFunctionTTListVariant0) {
            this.property1.getPossibleValues().forEach(p_125366_ -> this.property2.getPossibleValues().forEach(p_176318_ -> this.select((T1) p_125366_, (T2) p_176318_, (List<Variant>) biFunctionTTListVariant0.apply(p_125366_, p_176318_))));
            return this;
        }
    }

    public static class C3<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends PropertyDispatch {

        private final Property<T1> property1;

        private final Property<T2> property2;

        private final Property<T3> property3;

        C3(Property<T1> propertyT0, Property<T2> propertyT1, Property<T3> propertyT2) {
            this.property1 = propertyT0;
            this.property2 = propertyT1;
            this.property3 = propertyT2;
        }

        @Override
        public List<Property<?>> getDefinedProperties() {
            return ImmutableList.of(this.property1, this.property2, this.property3);
        }

        public PropertyDispatch.C3<T1, T2, T3> select(T1 t0, T2 t1, T3 t2, List<Variant> listVariant3) {
            Selector $$4 = Selector.of(this.property1.value(t0), this.property2.value(t1), this.property3.value(t2));
            this.m_125319_($$4, listVariant3);
            return this;
        }

        public PropertyDispatch.C3<T1, T2, T3> select(T1 t0, T2 t1, T3 t2, Variant variant3) {
            return this.select(t0, t1, t2, Collections.singletonList(variant3));
        }

        public PropertyDispatch generate(PropertyDispatch.TriFunction<T1, T2, T3, Variant> propertyDispatchTriFunctionTTTVariant0) {
            this.property1.getPossibleValues().forEach(p_125404_ -> this.property2.getPossibleValues().forEach(p_176343_ -> this.property3.getPossibleValues().forEach(p_176339_ -> this.select((T1) p_125404_, (T2) p_176343_, (T3) p_176339_, propertyDispatchTriFunctionTTTVariant0.apply((T1) p_125404_, (T2) p_176343_, (T3) p_176339_)))));
            return this;
        }

        public PropertyDispatch generateList(PropertyDispatch.TriFunction<T1, T2, T3, List<Variant>> propertyDispatchTriFunctionTTTListVariant0) {
            this.property1.getPossibleValues().forEach(p_176334_ -> this.property2.getPossibleValues().forEach(p_176331_ -> this.property3.getPossibleValues().forEach(p_176327_ -> this.select((T1) p_176334_, (T2) p_176331_, (T3) p_176327_, propertyDispatchTriFunctionTTTListVariant0.apply((T1) p_176334_, (T2) p_176331_, (T3) p_176327_)))));
            return this;
        }
    }

    public static class C4<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> extends PropertyDispatch {

        private final Property<T1> property1;

        private final Property<T2> property2;

        private final Property<T3> property3;

        private final Property<T4> property4;

        C4(Property<T1> propertyT0, Property<T2> propertyT1, Property<T3> propertyT2, Property<T4> propertyT3) {
            this.property1 = propertyT0;
            this.property2 = propertyT1;
            this.property3 = propertyT2;
            this.property4 = propertyT3;
        }

        @Override
        public List<Property<?>> getDefinedProperties() {
            return ImmutableList.of(this.property1, this.property2, this.property3, this.property4);
        }

        public PropertyDispatch.C4<T1, T2, T3, T4> select(T1 t0, T2 t1, T3 t2, T4 t3, List<Variant> listVariant4) {
            Selector $$5 = Selector.of(this.property1.value(t0), this.property2.value(t1), this.property3.value(t2), this.property4.value(t3));
            this.m_125319_($$5, listVariant4);
            return this;
        }

        public PropertyDispatch.C4<T1, T2, T3, T4> select(T1 t0, T2 t1, T3 t2, T4 t3, Variant variant4) {
            return this.select(t0, t1, t2, t3, Collections.singletonList(variant4));
        }

        public PropertyDispatch generate(PropertyDispatch.QuadFunction<T1, T2, T3, T4, Variant> propertyDispatchQuadFunctionTTTTVariant0) {
            this.property1.getPossibleValues().forEach(p_176385_ -> this.property2.getPossibleValues().forEach(p_176380_ -> this.property3.getPossibleValues().forEach(p_176376_ -> this.property4.getPossibleValues().forEach(p_176371_ -> this.select((T1) p_176385_, (T2) p_176380_, (T3) p_176376_, (T4) p_176371_, propertyDispatchQuadFunctionTTTTVariant0.apply((T1) p_176385_, (T2) p_176380_, (T3) p_176376_, (T4) p_176371_))))));
            return this;
        }

        public PropertyDispatch generateList(PropertyDispatch.QuadFunction<T1, T2, T3, T4, List<Variant>> propertyDispatchQuadFunctionTTTTListVariant0) {
            this.property1.getPossibleValues().forEach(p_176365_ -> this.property2.getPossibleValues().forEach(p_176360_ -> this.property3.getPossibleValues().forEach(p_176356_ -> this.property4.getPossibleValues().forEach(p_176351_ -> this.select((T1) p_176365_, (T2) p_176360_, (T3) p_176356_, (T4) p_176351_, propertyDispatchQuadFunctionTTTTListVariant0.apply((T1) p_176365_, (T2) p_176360_, (T3) p_176356_, (T4) p_176351_))))));
            return this;
        }
    }

    public static class C5<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> extends PropertyDispatch {

        private final Property<T1> property1;

        private final Property<T2> property2;

        private final Property<T3> property3;

        private final Property<T4> property4;

        private final Property<T5> property5;

        C5(Property<T1> propertyT0, Property<T2> propertyT1, Property<T3> propertyT2, Property<T4> propertyT3, Property<T5> propertyT4) {
            this.property1 = propertyT0;
            this.property2 = propertyT1;
            this.property3 = propertyT2;
            this.property4 = propertyT3;
            this.property5 = propertyT4;
        }

        @Override
        public List<Property<?>> getDefinedProperties() {
            return ImmutableList.of(this.property1, this.property2, this.property3, this.property4, this.property5);
        }

        public PropertyDispatch.C5<T1, T2, T3, T4, T5> select(T1 t0, T2 t1, T3 t2, T4 t3, T5 t4, List<Variant> listVariant5) {
            Selector $$6 = Selector.of(this.property1.value(t0), this.property2.value(t1), this.property3.value(t2), this.property4.value(t3), this.property5.value(t4));
            this.m_125319_($$6, listVariant5);
            return this;
        }

        public PropertyDispatch.C5<T1, T2, T3, T4, T5> select(T1 t0, T2 t1, T3 t2, T4 t3, T5 t4, Variant variant5) {
            return this.select(t0, t1, t2, t3, t4, Collections.singletonList(variant5));
        }

        public PropertyDispatch generate(PropertyDispatch.PentaFunction<T1, T2, T3, T4, T5, Variant> propertyDispatchPentaFunctionTTTTTVariant0) {
            this.property1.getPossibleValues().forEach(p_176439_ -> this.property2.getPossibleValues().forEach(p_176434_ -> this.property3.getPossibleValues().forEach(p_176430_ -> this.property4.getPossibleValues().forEach(p_176425_ -> this.property5.getPossibleValues().forEach(p_176419_ -> this.select((T1) p_176439_, (T2) p_176434_, (T3) p_176430_, (T4) p_176425_, (T5) p_176419_, propertyDispatchPentaFunctionTTTTTVariant0.apply((T1) p_176439_, (T2) p_176434_, (T3) p_176430_, (T4) p_176425_, (T5) p_176419_)))))));
            return this;
        }

        public PropertyDispatch generateList(PropertyDispatch.PentaFunction<T1, T2, T3, T4, T5, List<Variant>> propertyDispatchPentaFunctionTTTTTListVariant0) {
            this.property1.getPossibleValues().forEach(p_176412_ -> this.property2.getPossibleValues().forEach(p_176407_ -> this.property3.getPossibleValues().forEach(p_176403_ -> this.property4.getPossibleValues().forEach(p_176398_ -> this.property5.getPossibleValues().forEach(p_176392_ -> this.select((T1) p_176412_, (T2) p_176407_, (T3) p_176403_, (T4) p_176398_, (T5) p_176392_, propertyDispatchPentaFunctionTTTTTListVariant0.apply((T1) p_176412_, (T2) p_176407_, (T3) p_176403_, (T4) p_176398_, (T5) p_176392_)))))));
            return this;
        }
    }

    @FunctionalInterface
    public interface PentaFunction<P1, P2, P3, P4, P5, R> {

        R apply(P1 var1, P2 var2, P3 var3, P4 var4, P5 var5);
    }

    @FunctionalInterface
    public interface QuadFunction<P1, P2, P3, P4, R> {

        R apply(P1 var1, P2 var2, P3 var3, P4 var4);
    }

    @FunctionalInterface
    public interface TriFunction<P1, P2, P3, R> {

        R apply(P1 var1, P2 var2, P3 var3);
    }
}