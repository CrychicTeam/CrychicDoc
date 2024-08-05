package net.minecraftforge.common.crafting.conditions;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;

public class TagEmptyCondition implements ICondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "tag_empty");

    private final TagKey<Item> tag;

    public TagEmptyCondition(String location) {
        this(new ResourceLocation(location));
    }

    public TagEmptyCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public TagEmptyCondition(ResourceLocation tag) {
        this.tag = TagKey.create(Registries.ITEM, tag);
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        return context.getTag(this.tag).isEmpty();
    }

    public String toString() {
        return "tag_empty(\"" + this.tag.location() + "\")";
    }

    public static class Serializer implements IConditionSerializer<TagEmptyCondition> {

        public static final TagEmptyCondition.Serializer INSTANCE = new TagEmptyCondition.Serializer();

        public void write(JsonObject json, TagEmptyCondition value) {
            json.addProperty("tag", value.tag.location().toString());
        }

        public TagEmptyCondition read(JsonObject json) {
            return new TagEmptyCondition(new ResourceLocation(GsonHelper.getAsString(json, "tag")));
        }

        @Override
        public ResourceLocation getID() {
            return TagEmptyCondition.NAME;
        }
    }
}