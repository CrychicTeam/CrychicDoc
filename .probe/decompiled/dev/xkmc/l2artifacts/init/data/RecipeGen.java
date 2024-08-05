package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.LASets;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.serial.conditions.BooleanValueCondition;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2library.serial.recipe.NBTRecipe;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class RecipeGen {

    private static void craftCombine(RegistrateRecipeProvider pvd, ItemEntry<?>[] arr) {
        for (int i = 1; i < 5; i++) {
            ItemEntry<?> input = arr[i - 1];
            ItemEntry<?> output = arr[i];
            pvd.singleItemUnfinished(DataIngredient.items((Item) input.get(), new Item[0]), RecipeCategory.MISC, output, 2, 1).save(pvd, output.getId().withPrefix("upgrades/rank_up_"));
            pvd.singleItemUnfinished(DataIngredient.items((Item) output.get(), new Item[0]), RecipeCategory.MISC, input, 1, 2).save(pvd, output.getId().withPrefix("upgrades/rank_down_"));
        }
    }

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        ITagManager<Item> manager = (ITagManager<Item>) Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
        for (int i = 0; i < 5; i++) {
            int rank = i + 1;
            TagKey<Item> rank_tag = manager.createTagKey(new ResourceLocation("l2artifacts", "rank_" + rank));
            ItemEntry<?> output = ArtifactItems.ITEM_EXP[i];
            pvd.singleItem(DataIngredient.tag(rank_tag), RecipeCategory.MISC, output, 1, 1);
        }
        craftCombine(pvd, ArtifactItems.ITEM_EXP);
        craftCombine(pvd, ArtifactItems.ITEM_STAT);
        craftCombine(pvd, ArtifactItems.ITEM_BOOST_MAIN);
        craftCombine(pvd, ArtifactItems.ITEM_BOOST_SUB);
        for (SetEntry<?> set : L2Artifacts.REGISTRATE.SET_LIST) {
            ItemEntry<BaseArtifact>[][] items = set.items;
            for (ItemEntry<BaseArtifact>[] slot : items) {
                int n = slot.length;
                for (int i = 1; i < n; i++) {
                    BaseArtifact input = (BaseArtifact) slot[i - 1].get();
                    BaseArtifact output = (BaseArtifact) slot[i].get();
                    unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, output, 1)::m_126132_, input).requires(input, 2).save(ConditionalRecipeWrapper.of(pvd, BooleanValueCondition.of(ArtifactConfig.COMMON_PATH, ArtifactConfig.COMMON.enableArtifactRankUpRecipe, true)), slot[i].getId().withPrefix("rank_up/"));
                }
            }
        }
        TagKey<Item> artifact = manager.createTagKey(new ResourceLocation("l2artifacts", "artifact"));
        RecipeCategory var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) ArtifactItems.FILTER.get(), 1)::m_126132_, Items.ENDER_PEARL).pattern(" A ").pattern("LEL").pattern(" L ").define('E', Items.ENDER_PEARL).define('L', Items.LEATHER).define('A', artifact).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) ArtifactItems.SWAP.get(), 1)::m_126132_, Items.ENDER_PEARL).pattern(" E ").pattern("LAL").pattern(" L ").define('E', Items.ENDER_PEARL).define('L', Items.LEATHER).define('A', artifact).m_176498_(pvd);
        unlock(pvd, SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of((ItemLike) ArtifactItems.FILTER.get()), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.MISC, (Item) ArtifactItems.UPGRADED_POCKET.get())::m_266439_, Items.NETHERITE_INGOT).save(pvd, "l2artifacts:upgraded_pocket");
        List<SetEntry<?>> set0 = List.of(LASets.SET_GAMBLER, LASets.SET_ARCHER, LASets.SET_BERSERKER, LASets.SET_PHYSICAL, LASets.SET_MAGE, LASets.SET_PIRATE, LASets.SET_DAMOCLES, LASets.SET_LUCKCLOVER);
        List<SetEntry<?>> set1 = List.of(LASets.SET_PERFECTION, LASets.SET_SAINT, LASets.SET_EXECUTOR, LASets.SET_VAMPIRE, LASets.SET_GLUTTONY, LASets.SET_FALLEN, LASets.SET_SUN_BLOCK, LASets.SET_LUCKCLOVER);
        List<SetEntry<?>> set2 = List.of(LASets.SET_FROZE, LASets.SET_WRATH, LASets.SET_ANCIENT, LASets.SET_FLESH, LASets.SET_FUNGUS, LASets.SET_SLIMY, LASets.SET_PHOTOSYN, LASets.SET_LUCKCLOVER);
        List<SetEntry<?>> set3 = List.of(LASets.SET_LONGSHOOTER, LASets.SET_ABYSSMEDAL, LASets.SET_CELL, LASets.SET_GILDED, LASets.SET_POISONOUS, LASets.SET_THERMAL, LASets.SET_PROTECTION, LASets.SET_LUCKCLOVER);
        for (int i = 0; i < 5; i++) {
            int rank = i + 1;
            unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM_SET[i], 1)::m_126132_, (Item) LCItems.RESONANT_FEATHER.get()).pattern("XXX").pattern("A1A").pattern("AAA").define('A', ArtifactItems.RANDOM[i]).define('X', ArtifactItems.ITEM_EXP[i]).define('1', (ItemLike) LCItems.RESONANT_FEATHER.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
            unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::m_126132_, (Item) LCItems.RESONANT_FEATHER.get()).pattern("1A2").pattern("AXA").pattern("2A1").define('A', ArtifactItems.RANDOM[i]).define('X', ArtifactItems.ITEM_EXP[i]).define('1', (ItemLike) LCItems.RESONANT_FEATHER.get()).define('2', (ItemLike) LCItems.EXPLOSION_SHARD.get()).save(e -> ConditionalRecipeWrapper.mod(pvd, "l2complements").accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set0))), new ResourceLocation("l2artifacts", "directed/set_0_rank_" + rank));
            unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::m_126132_, (Item) LCItems.STORM_CORE.get()).pattern("1A2").pattern("AXA").pattern("2A1").define('A', ArtifactItems.RANDOM[i]).define('X', ArtifactItems.ITEM_EXP[i]).define('1', (ItemLike) LCItems.CAPTURED_BULLET.get()).define('2', (ItemLike) LCItems.STORM_CORE.get()).save(e -> ConditionalRecipeWrapper.mod(pvd, "l2complements").accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set1))), new ResourceLocation("l2artifacts", "directed/set_1_rank_" + rank));
            unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::m_126132_, (Item) LCItems.SOUL_FLAME.get()).pattern("1A2").pattern("AXA").pattern("2A1").define('A', ArtifactItems.RANDOM[i]).define('X', ArtifactItems.ITEM_EXP[i]).define('1', (ItemLike) LCItems.HARD_ICE.get()).define('2', (ItemLike) LCItems.SOUL_FLAME.get()).save(e -> ConditionalRecipeWrapper.mod(pvd, "l2complements").accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set2))), new ResourceLocation("l2artifacts", "directed/set_2_rank_" + rank));
            unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::m_126132_, (Item) LCItems.CAPTURED_WIND.get()).pattern("1A2").pattern("AXA").pattern("2A1").define('A', ArtifactItems.RANDOM[i]).define('X', ArtifactItems.ITEM_EXP[i]).define('1', (ItemLike) LCItems.WARDEN_BONE_SHARD.get()).define('2', (ItemLike) LCItems.CAPTURED_WIND.get()).save(e -> ConditionalRecipeWrapper.mod(pvd, "l2complements").accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set3))), new ResourceLocation("l2artifacts", "directed/set_3_rank_" + rank));
        }
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) ArtifactItems.ITEM_STAT[0].get(), 4)::m_126132_, (Item) LCItems.RESONANT_FEATHER.get()).pattern("AAA").pattern("BCB").pattern("AAA").define('A', Items.GOLD_INGOT).define('B', (ItemLike) LCItems.SOUL_FLAME.get()).define('C', (ItemLike) LCItems.RESONANT_FEATHER.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) ArtifactItems.ITEM_BOOST_MAIN[0].get(), 4)::m_126132_, (Item) LCItems.FORCE_FIELD.get()).pattern("ABA").pattern("BCB").pattern("ABA").define('A', Items.GOLD_INGOT).define('B', (ItemLike) LCItems.STORM_CORE.get()).define('C', (ItemLike) LCItems.FORCE_FIELD.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) ArtifactItems.ITEM_BOOST_SUB[0].get(), 4)::m_126132_, (Item) LCItems.EMERALD.get()).pattern("ABA").pattern("BCB").pattern("ABA").define('A', Items.GOLD_INGOT).define('B', (ItemLike) LCItems.CAPTURED_WIND.get()).define('C', (ItemLike) LCItems.EMERALD.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
    }

    public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items(item, new Item[0]).getCritereon(pvd));
    }
}