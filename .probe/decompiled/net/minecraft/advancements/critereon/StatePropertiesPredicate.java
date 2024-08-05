package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;

public class StatePropertiesPredicate {

    public static final StatePropertiesPredicate ANY = new StatePropertiesPredicate(ImmutableList.of());

    private final List<StatePropertiesPredicate.PropertyMatcher> properties;

    private static StatePropertiesPredicate.PropertyMatcher fromJson(String string0, JsonElement jsonElement1) {
        if (jsonElement1.isJsonPrimitive()) {
            String $$2 = jsonElement1.getAsString();
            return new StatePropertiesPredicate.ExactPropertyMatcher(string0, $$2);
        } else {
            JsonObject $$3 = GsonHelper.convertToJsonObject(jsonElement1, "value");
            String $$4 = $$3.has("min") ? getStringOrNull($$3.get("min")) : null;
            String $$5 = $$3.has("max") ? getStringOrNull($$3.get("max")) : null;
            return (StatePropertiesPredicate.PropertyMatcher) ($$4 != null && $$4.equals($$5) ? new StatePropertiesPredicate.ExactPropertyMatcher(string0, $$4) : new StatePropertiesPredicate.RangedPropertyMatcher(string0, $$4, $$5));
        }
    }

    @Nullable
    private static String getStringOrNull(JsonElement jsonElement0) {
        return jsonElement0.isJsonNull() ? null : jsonElement0.getAsString();
    }

    StatePropertiesPredicate(List<StatePropertiesPredicate.PropertyMatcher> listStatePropertiesPredicatePropertyMatcher0) {
        this.properties = ImmutableList.copyOf(listStatePropertiesPredicatePropertyMatcher0);
    }

    public <S extends StateHolder<?, S>> boolean matches(StateDefinition<?, S> stateDefinitionS0, S s1) {
        for (StatePropertiesPredicate.PropertyMatcher $$2 : this.properties) {
            if (!$$2.match(stateDefinitionS0, s1)) {
                return false;
            }
        }
        return true;
    }

    public boolean matches(BlockState blockState0) {
        return this.matches(blockState0.m_60734_().getStateDefinition(), blockState0);
    }

    public boolean matches(FluidState fluidState0) {
        return this.matches(fluidState0.getType().getStateDefinition(), fluidState0);
    }

    public void checkState(StateDefinition<?, ?> stateDefinition0, Consumer<String> consumerString1) {
        this.properties.forEach(p_67678_ -> p_67678_.checkState(stateDefinition0, consumerString1));
    }

    public static StatePropertiesPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "properties");
            List<StatePropertiesPredicate.PropertyMatcher> $$2 = Lists.newArrayList();
            for (Entry<String, JsonElement> $$3 : $$1.entrySet()) {
                $$2.add(fromJson((String) $$3.getKey(), (JsonElement) $$3.getValue()));
            }
            return new StatePropertiesPredicate($$2);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (!this.properties.isEmpty()) {
                this.properties.forEach(p_67683_ -> $$0.add(p_67683_.getName(), p_67683_.toJson()));
            }
            return $$0;
        }
    }

    public static class Builder {

        private final List<StatePropertiesPredicate.PropertyMatcher> matchers = Lists.newArrayList();

        private Builder() {
        }

        public static StatePropertiesPredicate.Builder properties() {
            return new StatePropertiesPredicate.Builder();
        }

        public StatePropertiesPredicate.Builder hasProperty(Property<?> property0, String string1) {
            this.matchers.add(new StatePropertiesPredicate.ExactPropertyMatcher(property0.getName(), string1));
            return this;
        }

        public StatePropertiesPredicate.Builder hasProperty(Property<Integer> propertyInteger0, int int1) {
            return this.hasProperty(propertyInteger0, Integer.toString(int1));
        }

        public StatePropertiesPredicate.Builder hasProperty(Property<Boolean> propertyBoolean0, boolean boolean1) {
            return this.hasProperty(propertyBoolean0, Boolean.toString(boolean1));
        }

        public <T extends Comparable<T> & StringRepresentable> StatePropertiesPredicate.Builder hasProperty(Property<T> propertyT0, T t1) {
            return this.hasProperty(propertyT0, t1.getSerializedName());
        }

        public StatePropertiesPredicate build() {
            return new StatePropertiesPredicate(this.matchers);
        }
    }

    static class ExactPropertyMatcher extends StatePropertiesPredicate.PropertyMatcher {

        private final String value;

        public ExactPropertyMatcher(String string0, String string1) {
            super(string0);
            this.value = string1;
        }

        @Override
        protected <T extends Comparable<T>> boolean match(StateHolder<?, ?> stateHolder0, Property<T> propertyT1) {
            T $$2 = stateHolder0.getValue(propertyT1);
            Optional<T> $$3 = propertyT1.getValue(this.value);
            return $$3.isPresent() && $$2.compareTo((Comparable) $$3.get()) == 0;
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(this.value);
        }
    }

    abstract static class PropertyMatcher {

        private final String name;

        public PropertyMatcher(String string0) {
            this.name = string0;
        }

        public <S extends StateHolder<?, S>> boolean match(StateDefinition<?, S> stateDefinitionS0, S s1) {
            Property<?> $$2 = stateDefinitionS0.getProperty(this.name);
            return $$2 == null ? false : this.match(s1, $$2);
        }

        protected abstract <T extends Comparable<T>> boolean match(StateHolder<?, ?> var1, Property<T> var2);

        public abstract JsonElement toJson();

        public String getName() {
            return this.name;
        }

        public void checkState(StateDefinition<?, ?> stateDefinition0, Consumer<String> consumerString1) {
            Property<?> $$2 = stateDefinition0.getProperty(this.name);
            if ($$2 == null) {
                consumerString1.accept(this.name);
            }
        }
    }

    static class RangedPropertyMatcher extends StatePropertiesPredicate.PropertyMatcher {

        @Nullable
        private final String minValue;

        @Nullable
        private final String maxValue;

        public RangedPropertyMatcher(String string0, @Nullable String string1, @Nullable String string2) {
            super(string0);
            this.minValue = string1;
            this.maxValue = string2;
        }

        @Override
        protected <T extends Comparable<T>> boolean match(StateHolder<?, ?> stateHolder0, Property<T> propertyT1) {
            T $$2 = stateHolder0.getValue(propertyT1);
            if (this.minValue != null) {
                Optional<T> $$3 = propertyT1.getValue(this.minValue);
                if (!$$3.isPresent() || $$2.compareTo((Comparable) $$3.get()) < 0) {
                    return false;
                }
            }
            if (this.maxValue != null) {
                Optional<T> $$4 = propertyT1.getValue(this.maxValue);
                if (!$$4.isPresent() || $$2.compareTo((Comparable) $$4.get()) > 0) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public JsonElement toJson() {
            JsonObject $$0 = new JsonObject();
            if (this.minValue != null) {
                $$0.addProperty("min", this.minValue);
            }
            if (this.maxValue != null) {
                $$0.addProperty("max", this.maxValue);
            }
            return $$0;
        }
    }
}