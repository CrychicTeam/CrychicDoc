package vectorwing.farmersdelight.common.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;

@EventBusSubscriber(modid = "farmersdelight", bus = Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void registerRecipeElements(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(new ResourceLocation("farmersdelight", "tool_action"), ToolActionIngredient.SERIALIZER);
        }
    }
}