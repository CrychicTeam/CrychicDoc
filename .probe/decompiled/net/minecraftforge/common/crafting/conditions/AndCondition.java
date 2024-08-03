package net.minecraftforge.common.crafting.conditions;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;

public class AndCondition implements ICondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "and");

    private final ICondition[] children;

    public AndCondition(ICondition... values) {
        if (values != null && values.length != 0) {
            for (ICondition child : values) {
                if (child == null) {
                    throw new IllegalArgumentException("Value must not be null");
                }
            }
            this.children = values;
        } else {
            throw new IllegalArgumentException("Values must not be empty");
        }
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        for (ICondition child : this.children) {
            if (!child.test(context)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return Joiner.on(" && ").join(this.children);
    }

    public static class Serializer implements IConditionSerializer<AndCondition> {

        public static final AndCondition.Serializer INSTANCE = new AndCondition.Serializer();

        public void write(JsonObject json, AndCondition value) {
            JsonArray values = new JsonArray();
            for (ICondition child : value.children) {
                values.add(CraftingHelper.serialize(child));
            }
            json.add("values", values);
        }

        public AndCondition read(JsonObject json) {
            List<ICondition> children = new ArrayList();
            for (JsonElement j : GsonHelper.getAsJsonArray(json, "values")) {
                if (!j.isJsonObject()) {
                    throw new JsonSyntaxException("And condition values must be an array of JsonObjects");
                }
                children.add(CraftingHelper.getCondition(j.getAsJsonObject()));
            }
            return new AndCondition((ICondition[]) children.toArray(new ICondition[children.size()]));
        }

        @Override
        public ResourceLocation getID() {
            return AndCondition.NAME;
        }
    }
}