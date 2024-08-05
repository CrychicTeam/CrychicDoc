package net.minecraft.data.models.blockstates;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public interface Condition extends Supplier<JsonElement> {

    void validate(StateDefinition<?, ?> var1);

    static Condition.TerminalCondition condition() {
        return new Condition.TerminalCondition();
    }

    static Condition and(Condition... condition0) {
        return new Condition.CompositeCondition(Condition.Operation.AND, Arrays.asList(condition0));
    }

    static Condition or(Condition... condition0) {
        return new Condition.CompositeCondition(Condition.Operation.OR, Arrays.asList(condition0));
    }

    public static class CompositeCondition implements Condition {

        private final Condition.Operation operation;

        private final List<Condition> subconditions;

        CompositeCondition(Condition.Operation conditionOperation0, List<Condition> listCondition1) {
            this.operation = conditionOperation0;
            this.subconditions = listCondition1;
        }

        @Override
        public void validate(StateDefinition<?, ?> stateDefinition0) {
            this.subconditions.forEach(p_125152_ -> p_125152_.validate(stateDefinition0));
        }

        public JsonElement get() {
            JsonArray $$0 = new JsonArray();
            this.subconditions.stream().map(Supplier::get).forEach($$0::add);
            JsonObject $$1 = new JsonObject();
            $$1.add(this.operation.id, $$0);
            return $$1;
        }
    }

    public static enum Operation {

        AND("AND"), OR("OR");

        final String id;

        private Operation(String p_125163_) {
            this.id = p_125163_;
        }
    }

    public static class TerminalCondition implements Condition {

        private final Map<Property<?>, String> terms = Maps.newHashMap();

        private static <T extends Comparable<T>> String joinValues(Property<T> propertyT0, Stream<T> streamT1) {
            return (String) streamT1.map(propertyT0::m_6940_).collect(Collectors.joining("|"));
        }

        private static <T extends Comparable<T>> String getTerm(Property<T> propertyT0, T t1, T[] t2) {
            return joinValues(propertyT0, Stream.concat(Stream.of(t1), Stream.of(t2)));
        }

        private <T extends Comparable<T>> void putValue(Property<T> propertyT0, String string1) {
            String $$2 = (String) this.terms.put(propertyT0, string1);
            if ($$2 != null) {
                throw new IllegalStateException("Tried to replace " + propertyT0 + " value from " + $$2 + " to " + string1);
            }
        }

        public final <T extends Comparable<T>> Condition.TerminalCondition term(Property<T> propertyT0, T t1) {
            this.putValue(propertyT0, propertyT0.getName(t1));
            return this;
        }

        @SafeVarargs
        public final <T extends Comparable<T>> Condition.TerminalCondition term(Property<T> propertyT0, T t1, T... t2) {
            this.putValue(propertyT0, getTerm(propertyT0, t1, t2));
            return this;
        }

        public final <T extends Comparable<T>> Condition.TerminalCondition negatedTerm(Property<T> propertyT0, T t1) {
            this.putValue(propertyT0, "!" + propertyT0.getName(t1));
            return this;
        }

        @SafeVarargs
        public final <T extends Comparable<T>> Condition.TerminalCondition negatedTerm(Property<T> propertyT0, T t1, T... t2) {
            this.putValue(propertyT0, "!" + getTerm(propertyT0, t1, t2));
            return this;
        }

        public JsonElement get() {
            JsonObject $$0 = new JsonObject();
            this.terms.forEach((p_125191_, p_125192_) -> $$0.addProperty(p_125191_.getName(), p_125192_));
            return $$0;
        }

        @Override
        public void validate(StateDefinition<?, ?> stateDefinition0) {
            List<Property<?>> $$1 = (List<Property<?>>) this.terms.keySet().stream().filter(p_125175_ -> stateDefinition0.getProperty(p_125175_.getName()) != p_125175_).collect(Collectors.toList());
            if (!$$1.isEmpty()) {
                throw new IllegalStateException("Properties " + $$1 + " are missing from " + stateDefinition0);
            }
        }
    }
}