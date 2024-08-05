package dev.xkmc.l2backpack.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import dev.xkmc.l2backpack.init.registrate.BackpackMisc;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import dev.xkmc.l2library.serial.recipe.CustomShapelessBuilder;
import java.util.function.BiFunction;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeGen {

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        for (int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.values()[i];
            Item wool = ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_wool"));
            Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_dye"));
            Item backpack = (Item) BackpackItems.BACKPACKS[i].get();
            assert wool != null;
            unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, backpack, 1)::m_126132_, backpack).group("backpack_craft").pattern(" A ").pattern("DCD").pattern("BBB").define('A', Tags.Items.LEATHER).define('B', wool).define('C', Items.CHEST).define('D', Items.IRON_INGOT).m_176500_(pvd, "l2backpack:shaped/craft_backpack_" + color.getName());
            unlock(pvd, new CustomShapelessBuilder(BackpackMisc.RSC_BAG_DYE, backpack, 1)::m_126132_, backpack).group("backpack_dye").requires(Ingredient.of(TagGen.BACKPACKS)).requires(Ingredient.of(dye)).m_176500_(pvd, "l2backpack:shapeless/dye_backpack_" + color.getName());
            RecipeSerializer var10003 = (RecipeSerializer) BackpackMisc.RSC_BAG_UPGRADE.get();
            Ingredient var10004 = AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
            unlock(pvd, new SmithingTransformRecipeBuilder(var10003, var10004, Ingredient.of(backpack), Ingredient.of((ItemLike) BackpackItems.ENDER_POCKET.get()), RecipeCategory.MISC, backpack)::m_266439_, backpack).save(pvd, "l2backpack:smithing/upgrade_backpack_" + color.getName());
            Item storage = (Item) BackpackItems.DIMENSIONAL_STORAGE[i].get();
            unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, storage, 1)::m_126132_, storage).group("dimensional_storage_craft").pattern("EAE").pattern("DCD").pattern("BAB").define('A', (ItemLike) BackpackItems.ENDER_POCKET.get()).define('B', wool).define('C', Items.ENDER_CHEST).define('D', Items.POPPED_CHORUS_FRUIT).define('E', Items.GOLD_NUGGET).m_176500_(pvd, "l2backpack:shaped/craft_storage_" + color.getName());
        }
        RecipeCategory var18 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var18, (ItemLike) BackpackItems.PICKUP_TWEAKER.get(), 1)::m_126132_, Items.STICK).pattern(" G ").pattern(" IG").pattern("S  ").define('S', Items.STICK).define('G', Items.GOLD_NUGGET).define('I', Items.IRON_INGOT).m_176498_(pvd);
        var18 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var18, (ItemLike) BackpackItems.DESTROY_TWEAKER.get(), 1)::m_126132_, Items.STICK).pattern(" G ").pattern(" IG").pattern("S  ").define('S', Items.STICK).define('G', Items.GOLD_NUGGET).define('I', Items.COPPER_INGOT).m_176498_(pvd);
        Item ender = (Item) BackpackItems.ENDER_BACKPACK.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ender, 1)::m_126132_, ender).pattern("EAE").pattern("BCB").pattern("DDD").define('A', Tags.Items.LEATHER).define('B', Items.IRON_INGOT).define('C', Items.ENDER_CHEST).define('D', Items.PURPLE_WOOL).define('E', Items.GOLD_NUGGET).m_176498_(pvd);
        ender = (Item) BackpackItems.ENDER_POCKET.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ender, 4)::m_126132_, ender).pattern("ADA").pattern("BCB").pattern("ADA").define('C', Items.ENDER_PEARL).define('B', Items.GOLD_NUGGET).define('A', Tags.Items.LEATHER).define('D', Items.LAPIS_LAZULI).m_176498_(pvd);
        ender = (Item) BackpackItems.ENDER_POCKET.get();
        Item bag = (Item) BackpackItems.ARMOR_BAG.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, ender).pattern("DCD").pattern("ABA").pattern(" A ").define('A', Tags.Items.LEATHER).define('B', ender).define('D', Items.STRING).define('C', Items.IRON_CHESTPLATE).m_176498_(pvd);
        bag = (Item) BackpackItems.BOOK_BAG.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, ender).pattern("DCD").pattern("ABA").pattern(" A ").define('A', Tags.Items.LEATHER).define('B', ender).define('D', Items.STRING).define('C', Items.BOOK).m_176498_(pvd);
        bag = (Item) BackpackItems.QUIVER.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, Items.LEATHER).pattern(" A ").pattern("ABA").pattern(" AD").define('A', Tags.Items.LEATHER).define('B', Items.ARROW).define('D', Items.STRING).m_176498_(pvd);
        bag = (Item) BackpackItems.SCABBARD.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, Items.LEATHER).pattern(" A ").pattern("ABA").pattern(" AD").define('A', Tags.Items.LEATHER).define('B', Items.STONE_SWORD).define('D', Items.IRON_INGOT).m_176498_(pvd);
        bag = (Item) BackpackItems.ARMOR_SWAP.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, Items.LEATHER).pattern(" A ").pattern("ABA").pattern("DAD").define('A', Tags.Items.LEATHER).define('B', Items.IRON_HELMET).define('D', Items.IRON_INGOT).m_176498_(pvd);
        bag = (Item) BackpackItems.SUIT_SWAP.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, Items.LEATHER).pattern("EAE").pattern("ABA").pattern("DAD").define('A', Tags.Items.LEATHER).define('B', Items.IRON_CHESTPLATE).define('D', Items.GOLD_INGOT).define('E', ender).m_176498_(pvd);
        bag = (Item) BackpackItems.DRAWER.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, ender).pattern("CAC").pattern("ABA").pattern("DAD").define('A', Items.GLASS).define('B', ender).define('C', Tags.Items.DYES_PURPLE).define('D', Tags.Items.DYES_YELLOW).save(pvd, new ResourceLocation("l2backpack", "drawer_cheap"));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, ender).pattern("CAC").pattern("ABA").pattern("DAD").define('A', Items.GLASS).define('B', ender).define('C', Items.AMETHYST_SHARD).define('D', Items.GOLD_NUGGET).m_176498_(pvd);
        RecipeSerializer var20 = (RecipeSerializer) BackpackMisc.RSC_DRAWER_UPGRADE.get();
        Ingredient var21 = AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
        unlock(pvd, new SmithingTransformRecipeBuilder(var20, var21, Ingredient.of(bag), Ingredient.of((ItemLike) BackpackItems.ENDER_POCKET.get()), RecipeCategory.MISC, bag)::m_266439_, bag).save(pvd, "l2backpack:smithing/upgrade_drawer");
        bag = (Item) BackpackItems.ENDER_DRAWER.get();
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, ender).pattern("DAD").pattern("ABA").pattern("DED").define('B', ender).define('A', Items.GLASS).define('D', Items.OBSIDIAN).define('E', Items.ENDER_CHEST).m_176498_(pvd);
        unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, bag, 1)::m_126132_, bag).requires(bag).save(pvd, new ResourceLocation("l2backpack", "shapeless/clear_ender_drawer"));
    }

    private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items(item, new Item[0]).getCritereon(pvd));
    }
}