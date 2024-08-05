package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class RequirementCombinationAnd implements IRequirement {

    private final IRequirement first;

    private final IRequirement second;

    public RequirementCombinationAnd(IRequirement first, IRequirement second) {
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
        return this.first.test(recipeWrapper) && this.second.test(recipeWrapper);
    }

    public static RequirementCombinationAnd fromJson(JsonObject object) {
        IRequirement first = IRequirement.fromJson(GsonHelper.getAsJsonObject(object, "first"));
        IRequirement second = IRequirement.fromJson(GsonHelper.getAsJsonObject(object, "second"));
        return new RequirementCombinationAnd(first, second);
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", RequirementType.COMBINATION_AND.name());
        obj.add("first", this.first.toJson());
        obj.add("second", this.second.toJson());
        return obj;
    }

    public static RequirementCombinationAnd fromNetwork(FriendlyByteBuf buffer) {
        return new RequirementCombinationAnd(IRequirement.fromNetwork(buffer), IRequirement.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.COMBINATION_AND);
        this.first.toNetwork(buffer);
        this.second.toNetwork(buffer);
    }
}