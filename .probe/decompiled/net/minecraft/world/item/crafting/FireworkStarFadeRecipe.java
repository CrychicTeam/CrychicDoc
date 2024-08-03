package net.minecraft.world.item.crafting;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FireworkStarFadeRecipe extends CustomRecipe {

    private static final Ingredient STAR_INGREDIENT = Ingredient.of(Items.FIREWORK_STAR);

    public FireworkStarFadeRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        boolean $$2 = false;
        boolean $$3 = false;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.getItem() instanceof DyeItem) {
                    $$2 = true;
                } else {
                    if (!STAR_INGREDIENT.test($$5)) {
                        return false;
                    }
                    if ($$3) {
                        return false;
                    }
                    $$3 = true;
                }
            }
        }
        return $$3 && $$2;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        List<Integer> $$2 = Lists.newArrayList();
        ItemStack $$3 = null;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            Item $$6 = $$5.getItem();
            if ($$6 instanceof DyeItem) {
                $$2.add(((DyeItem) $$6).getDyeColor().getFireworkColor());
            } else if (STAR_INGREDIENT.test($$5)) {
                $$3 = $$5.copyWithCount(1);
            }
        }
        if ($$3 != null && !$$2.isEmpty()) {
            $$3.getOrCreateTagElement("Explosion").putIntArray("FadeColors", $$2);
            return $$3;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.FIREWORK_STAR_FADE;
    }
}