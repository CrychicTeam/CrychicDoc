package team.lodestar.lodestone.registry.common;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.recipe.NBTCarryRecipe;

public class LodestoneRecipeSerializerRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "lodestone");

    public static final RegistryObject<RecipeSerializer<NBTCarryRecipe>> NBT_CARRY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("nbt_carry", NBTCarryRecipe.Serializer::new);
}