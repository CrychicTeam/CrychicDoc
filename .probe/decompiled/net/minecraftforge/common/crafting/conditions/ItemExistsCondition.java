package net.minecraftforge.common.crafting.conditions;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemExistsCondition implements ICondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "item_exists");

    private final ResourceLocation item;

    public ItemExistsCondition(String location) {
        this(new ResourceLocation(location));
    }

    public ItemExistsCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public ItemExistsCondition(ResourceLocation item) {
        this.item = item;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        return ForgeRegistries.ITEMS.containsKey(this.item);
    }

    public String toString() {
        return "item_exists(\"" + this.item + "\")";
    }

    public static class Serializer implements IConditionSerializer<ItemExistsCondition> {

        public static final ItemExistsCondition.Serializer INSTANCE = new ItemExistsCondition.Serializer();

        public void write(JsonObject json, ItemExistsCondition value) {
            json.addProperty("item", value.item.toString());
        }

        public ItemExistsCondition read(JsonObject json) {
            return new ItemExistsCondition(new ResourceLocation(GsonHelper.getAsString(json, "item")));
        }

        @Override
        public ResourceLocation getID() {
            return ItemExistsCondition.NAME;
        }
    }
}