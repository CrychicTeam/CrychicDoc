package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class RequirementCombinationOr implements IRequirement {

    private final IRequirement first;

    private final IRequirement second;

    public RequirementCombinationOr(IRequirement first, IRequirement second) {
        this.first = first;
        this.second = second;
    }

    public IRequirement getFirst() {
        return this.first;
    }

    public IRequirement getSecond() {
        return this.second;
    }

    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return this.first.test(recipeWrapper) || this.second.test(recipeWrapper);
    }

    public static RequirementCombinationOr fromJson(JsonObject object) {
        IRequirement first = IRequirement.fromJson(GsonHelper.getAsJsonObject(object, "first"));
        IRequirement second = IRequirement.fromJson(GsonHelper.getAsJsonObject(object, "second"));
        return new RequirementCombinationOr(first, second);
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", RequirementType.COMBINATION_OR.name());
        obj.add("first", this.first.toJson());
        obj.add("second", this.second.toJson());
        return obj;
    }

    public static RequirementCombinationOr fromNetwork(FriendlyByteBuf buffer) {
        return new RequirementCombinationOr(IRequirement.fromNetwork(buffer), IRequirement.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.COMBINATION_OR);
        this.first.toNetwork(buffer);
        this.second.toNetwork(buffer);
    }
}