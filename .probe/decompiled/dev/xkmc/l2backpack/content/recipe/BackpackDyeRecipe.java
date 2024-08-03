package dev.xkmc.l2backpack.content.recipe;

import dev.xkmc.l2backpack.init.data.TagGen;
import dev.xkmc.l2backpack.init.registrate.BackpackMisc;
import dev.xkmc.l2library.serial.recipe.AbstractShapelessRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BackpackDyeRecipe extends AbstractShapelessRecipe<BackpackDyeRecipe> {

    public BackpackDyeRecipe(ResourceLocation rl, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(rl, group, result, ingredients);
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        ItemStack bag = ItemStack.EMPTY;
        for (int i = 0; i < container.m_6643_(); i++) {
            if (container.m_8020_(i).is(TagGen.BACKPACKS)) {
                bag = container.m_8020_(i);
            }
        }
        ItemStack stack = super.m_5874_(container, access);
        stack.setTag(bag.getTag());
        return stack;
    }

    @Override
    public AbstractShapelessRecipe.Serializer<BackpackDyeRecipe> getSerializer() {
        return (AbstractShapelessRecipe.Serializer<BackpackDyeRecipe>) BackpackMisc.RSC_BAG_DYE.get();
    }
}