package net.minecraft.world.item.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FireworkStarRecipe extends CustomRecipe {

    private static final Ingredient SHAPE_INGREDIENT = Ingredient.of(Items.FIRE_CHARGE, Items.FEATHER, Items.GOLD_NUGGET, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.PLAYER_HEAD, Items.DRAGON_HEAD, Items.ZOMBIE_HEAD, Items.PIGLIN_HEAD);

    private static final Ingredient TRAIL_INGREDIENT = Ingredient.of(Items.DIAMOND);

    private static final Ingredient FLICKER_INGREDIENT = Ingredient.of(Items.GLOWSTONE_DUST);

    private static final Map<Item, FireworkRocketItem.Shape> SHAPE_BY_ITEM = Util.make(Maps.newHashMap(), p_261449_ -> {
        p_261449_.put(Items.FIRE_CHARGE, FireworkRocketItem.Shape.LARGE_BALL);
        p_261449_.put(Items.FEATHER, FireworkRocketItem.Shape.BURST);
        p_261449_.put(Items.GOLD_NUGGET, FireworkRocketItem.Shape.STAR);
        p_261449_.put(Items.SKELETON_SKULL, FireworkRocketItem.Shape.CREEPER);
        p_261449_.put(Items.WITHER_SKELETON_SKULL, FireworkRocketItem.Shape.CREEPER);
        p_261449_.put(Items.CREEPER_HEAD, FireworkRocketItem.Shape.CREEPER);
        p_261449_.put(Items.PLAYER_HEAD, FireworkRocketItem.Shape.CREEPER);
        p_261449_.put(Items.DRAGON_HEAD, FireworkRocketItem.Shape.CREEPER);
        p_261449_.put(Items.ZOMBIE_HEAD, FireworkRocketItem.Shape.CREEPER);
        p_261449_.put(Items.PIGLIN_HEAD, FireworkRocketItem.Shape.CREEPER);
    });

    private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);

    public FireworkStarRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        boolean $$2 = false;
        boolean $$3 = false;
        boolean $$4 = false;
        boolean $$5 = false;
        boolean $$6 = false;
        for (int $$7 = 0; $$7 < craftingContainer0.m_6643_(); $$7++) {
            ItemStack $$8 = craftingContainer0.m_8020_($$7);
            if (!$$8.isEmpty()) {
                if (SHAPE_INGREDIENT.test($$8)) {
                    if ($$4) {
                        return false;
                    }
                    $$4 = true;
                } else if (FLICKER_INGREDIENT.test($$8)) {
                    if ($$6) {
                        return false;
                    }
                    $$6 = true;
                } else if (TRAIL_INGREDIENT.test($$8)) {
                    if ($$5) {
                        return false;
                    }
                    $$5 = true;
                } else if (GUNPOWDER_INGREDIENT.test($$8)) {
                    if ($$2) {
                        return false;
                    }
                    $$2 = true;
                } else {
                    if (!($$8.getItem() instanceof DyeItem)) {
                        return false;
                    }
                    $$3 = true;
                }
            }
        }
        return $$2 && $$3;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        ItemStack $$2 = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag $$3 = $$2.getOrCreateTagElement("Explosion");
        FireworkRocketItem.Shape $$4 = FireworkRocketItem.Shape.SMALL_BALL;
        List<Integer> $$5 = Lists.newArrayList();
        for (int $$6 = 0; $$6 < craftingContainer0.m_6643_(); $$6++) {
            ItemStack $$7 = craftingContainer0.m_8020_($$6);
            if (!$$7.isEmpty()) {
                if (SHAPE_INGREDIENT.test($$7)) {
                    $$4 = (FireworkRocketItem.Shape) SHAPE_BY_ITEM.get($$7.getItem());
                } else if (FLICKER_INGREDIENT.test($$7)) {
                    $$3.putBoolean("Flicker", true);
                } else if (TRAIL_INGREDIENT.test($$7)) {
                    $$3.putBoolean("Trail", true);
                } else if ($$7.getItem() instanceof DyeItem) {
                    $$5.add(((DyeItem) $$7.getItem()).getDyeColor().getFireworkColor());
                }
            }
        }
        $$3.putIntArray("Colors", $$5);
        $$3.putByte("Type", (byte) $$4.getId());
        return $$2;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return new ItemStack(Items.FIREWORK_STAR);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.FIREWORK_STAR;
    }
}