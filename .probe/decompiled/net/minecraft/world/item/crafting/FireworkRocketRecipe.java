package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FireworkRocketRecipe extends CustomRecipe {

    private static final Ingredient PAPER_INGREDIENT = Ingredient.of(Items.PAPER);

    private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);

    private static final Ingredient STAR_INGREDIENT = Ingredient.of(Items.FIREWORK_STAR);

    public FireworkRocketRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        boolean $$2 = false;
        int $$3 = 0;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if (PAPER_INGREDIENT.test($$5)) {
                    if ($$2) {
                        return false;
                    }
                    $$2 = true;
                } else if (GUNPOWDER_INGREDIENT.test($$5)) {
                    if (++$$3 > 3) {
                        return false;
                    }
                } else if (!STAR_INGREDIENT.test($$5)) {
                    return false;
                }
            }
        }
        return $$2 && $$3 >= 1;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        ItemStack $$2 = new ItemStack(Items.FIREWORK_ROCKET, 3);
        CompoundTag $$3 = $$2.getOrCreateTagElement("Fireworks");
        ListTag $$4 = new ListTag();
        int $$5 = 0;
        for (int $$6 = 0; $$6 < craftingContainer0.m_6643_(); $$6++) {
            ItemStack $$7 = craftingContainer0.m_8020_($$6);
            if (!$$7.isEmpty()) {
                if (GUNPOWDER_INGREDIENT.test($$7)) {
                    $$5++;
                } else if (STAR_INGREDIENT.test($$7)) {
                    CompoundTag $$8 = $$7.getTagElement("Explosion");
                    if ($$8 != null) {
                        $$4.add($$8);
                    }
                }
            }
        }
        $$3.putByte("Flight", (byte) $$5);
        if (!$$4.isEmpty()) {
            $$3.put("Explosions", $$4);
        }
        return $$2;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return new ItemStack(Items.FIREWORK_ROCKET);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.FIREWORK_ROCKET;
    }
}