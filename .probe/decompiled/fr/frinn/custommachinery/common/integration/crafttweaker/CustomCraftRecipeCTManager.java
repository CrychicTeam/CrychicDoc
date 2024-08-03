package fr.frinn.custommachinery.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.data.MapData;
import com.blamejared.crafttweaker.api.data.visitor.DataToJsonStringVisitor;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult.PartialResult;
import fr.frinn.custommachinery.common.crafting.craft.CustomCraftRecipe;
import fr.frinn.custommachinery.common.crafting.craft.CustomCraftRecipeBuilder;
import fr.frinn.custommachinery.common.init.Registration;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.CraftRecipeManager")
public class CustomCraftRecipeCTManager implements IRecipeManager<CustomCraftRecipe> {

    public static final CustomCraftRecipeCTManager INSTANCE = new CustomCraftRecipeCTManager();

    public RecipeType<CustomCraftRecipe> getRecipeType() {
        return (RecipeType<CustomCraftRecipe>) Registration.CUSTOM_CRAFT_RECIPE.get();
    }

    public ResourceLocation getBracketResourceLocation() {
        return Registration.CUSTOM_CRAFT_RECIPE.getId();
    }

    public void addJsonRecipe(String name, MapData mapData) {
        JsonObject recipeObject = (JsonObject) JSON_RECIPE_GSON.fromJson((String) mapData.accept(DataToJsonStringVisitor.INSTANCE), JsonObject.class);
        DataResult<CustomCraftRecipeBuilder> result = CustomCraftRecipeBuilder.CODEC.read(JsonOps.INSTANCE, recipeObject);
        if (!result.error().isPresent() && !result.result().isEmpty()) {
            ResourceLocation id = ResourceLocation.tryParse(name);
            if (id == null) {
                CraftTweakerAPI.getLogger("custommachinery").error("Invalid id for custom craft recipe: {}", name);
            } else {
                CustomCraftRecipe recipe = ((CustomCraftRecipeBuilder) result.result().get()).build(id);
                CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
            }
        } else {
            CraftTweakerAPI.getLogger("custommachinery").error("Couldn't add custom craft recipe {} from json: {}", name, ((PartialResult) result.error().get()).message());
        }
    }

    public List<CustomCraftRecipe> getRecipesByOutput(IIngredient output) {
        throw new UnsupportedOperationException("Can't get custom craft recipe by output");
    }

    public void remove(IIngredient output) {
        throw new UnsupportedOperationException("Can't remove custom craft recipe by output");
    }

    public void removeByInput(IItemStack input) {
        throw new UnsupportedOperationException("Can't remove custom craft recipe by input");
    }

    @Method
    public CustomCraftRecipeCTBuilder create(String machine, IItemStack output) {
        return CustomCraftRecipeCTBuilder.create(machine, output);
    }
}