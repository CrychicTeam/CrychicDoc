package net.minecraftforge.client.model.generators;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelBuilder extends ModelBuilder<ItemModelBuilder> {

    protected List<ItemModelBuilder.OverrideBuilder> overrides = new ArrayList();

    public ItemModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }

    public ItemModelBuilder.OverrideBuilder override() {
        ItemModelBuilder.OverrideBuilder ret = new ItemModelBuilder.OverrideBuilder();
        this.overrides.add(ret);
        return ret;
    }

    public ItemModelBuilder.OverrideBuilder override(int index) {
        Preconditions.checkElementIndex(index, this.overrides.size(), "override");
        return (ItemModelBuilder.OverrideBuilder) this.overrides.get(index);
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = super.toJson();
        if (!this.overrides.isEmpty()) {
            JsonArray overridesJson = new JsonArray();
            this.overrides.stream().map(ItemModelBuilder.OverrideBuilder::toJson).forEach(overridesJson::add);
            root.add("overrides", overridesJson);
        }
        return root;
    }

    public class OverrideBuilder {

        private ModelFile model;

        private final Map<ResourceLocation, Float> predicates = new LinkedHashMap();

        public ItemModelBuilder.OverrideBuilder model(ModelFile model) {
            this.model = model;
            model.assertExistence();
            return this;
        }

        public ItemModelBuilder.OverrideBuilder predicate(ResourceLocation key, float value) {
            this.predicates.put(key, value);
            return this;
        }

        public ItemModelBuilder end() {
            return ItemModelBuilder.this;
        }

        JsonObject toJson() {
            JsonObject ret = new JsonObject();
            JsonObject predicatesJson = new JsonObject();
            this.predicates.forEach((key, val) -> predicatesJson.addProperty(key.toString(), val));
            ret.add("predicate", predicatesJson);
            ret.addProperty("model", this.model.getLocation().toString());
            return ret;
        }
    }
}