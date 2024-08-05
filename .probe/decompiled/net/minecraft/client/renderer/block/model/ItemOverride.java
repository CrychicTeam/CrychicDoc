package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class ItemOverride {

    private final ResourceLocation model;

    private final List<ItemOverride.Predicate> predicates;

    public ItemOverride(ResourceLocation resourceLocation0, List<ItemOverride.Predicate> listItemOverridePredicate1) {
        this.model = resourceLocation0;
        this.predicates = ImmutableList.copyOf(listItemOverridePredicate1);
    }

    public ResourceLocation getModel() {
        return this.model;
    }

    public Stream<ItemOverride.Predicate> getPredicates() {
        return this.predicates.stream();
    }

    protected static class Deserializer implements JsonDeserializer<ItemOverride> {

        public ItemOverride deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            ResourceLocation $$4 = new ResourceLocation(GsonHelper.getAsString($$3, "model"));
            List<ItemOverride.Predicate> $$5 = this.getPredicates($$3);
            return new ItemOverride($$4, $$5);
        }

        protected List<ItemOverride.Predicate> getPredicates(JsonObject jsonObject0) {
            Map<ResourceLocation, Float> $$1 = Maps.newLinkedHashMap();
            JsonObject $$2 = GsonHelper.getAsJsonObject(jsonObject0, "predicate");
            for (Entry<String, JsonElement> $$3 : $$2.entrySet()) {
                $$1.put(new ResourceLocation((String) $$3.getKey()), GsonHelper.convertToFloat((JsonElement) $$3.getValue(), (String) $$3.getKey()));
            }
            return (List<ItemOverride.Predicate>) $$1.entrySet().stream().map(p_173453_ -> new ItemOverride.Predicate((ResourceLocation) p_173453_.getKey(), (Float) p_173453_.getValue())).collect(ImmutableList.toImmutableList());
        }
    }

    public static class Predicate {

        private final ResourceLocation property;

        private final float value;

        public Predicate(ResourceLocation resourceLocation0, float float1) {
            this.property = resourceLocation0;
            this.value = float1;
        }

        public ResourceLocation getProperty() {
            return this.property;
        }

        public float getValue() {
            return this.value;
        }
    }
}