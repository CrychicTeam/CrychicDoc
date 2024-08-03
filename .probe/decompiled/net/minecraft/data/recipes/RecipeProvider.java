package net.minecraft.data.recipes;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public abstract class RecipeProvider implements DataProvider {

    private final PackOutput.PathProvider recipePathProvider;

    private final PackOutput.PathProvider advancementPathProvider;

    private static final Map<BlockFamily.Variant, BiFunction<ItemLike, ItemLike, RecipeBuilder>> SHAPE_BUILDERS = ImmutableMap.builder().put(BlockFamily.Variant.BUTTON, (BiFunction) (p_176733_, p_176734_) -> buttonBuilder(p_176733_, Ingredient.of(p_176734_))).put(BlockFamily.Variant.CHISELED, (BiFunction) (p_248037_, p_248038_) -> chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, p_248037_, Ingredient.of(p_248038_))).put(BlockFamily.Variant.CUT, (BiFunction) (p_248026_, p_248027_) -> cutBuilder(RecipeCategory.BUILDING_BLOCKS, p_248026_, Ingredient.of(p_248027_))).put(BlockFamily.Variant.DOOR, (BiFunction) (p_176714_, p_176715_) -> doorBuilder(p_176714_, Ingredient.of(p_176715_))).put(BlockFamily.Variant.CUSTOM_FENCE, (BiFunction) (p_176708_, p_176709_) -> fenceBuilder(p_176708_, Ingredient.of(p_176709_))).put(BlockFamily.Variant.FENCE, (BiFunction) (p_248031_, p_248032_) -> fenceBuilder(p_248031_, Ingredient.of(p_248032_))).put(BlockFamily.Variant.CUSTOM_FENCE_GATE, (BiFunction) (p_176698_, p_176699_) -> fenceGateBuilder(p_176698_, Ingredient.of(p_176699_))).put(BlockFamily.Variant.FENCE_GATE, (BiFunction) (p_248035_, p_248036_) -> fenceGateBuilder(p_248035_, Ingredient.of(p_248036_))).put(BlockFamily.Variant.SIGN, (BiFunction) (p_176688_, p_176689_) -> signBuilder(p_176688_, Ingredient.of(p_176689_))).put(BlockFamily.Variant.SLAB, (BiFunction) (p_248017_, p_248018_) -> slabBuilder(RecipeCategory.BUILDING_BLOCKS, p_248017_, Ingredient.of(p_248018_))).put(BlockFamily.Variant.STAIRS, (BiFunction) (p_176674_, p_176675_) -> stairBuilder(p_176674_, Ingredient.of(p_176675_))).put(BlockFamily.Variant.PRESSURE_PLATE, (BiFunction) (p_248039_, p_248040_) -> pressurePlateBuilder(RecipeCategory.REDSTONE, p_248039_, Ingredient.of(p_248040_))).put(BlockFamily.Variant.POLISHED, (BiFunction) (p_248019_, p_248020_) -> polishedBuilder(RecipeCategory.BUILDING_BLOCKS, p_248019_, Ingredient.of(p_248020_))).put(BlockFamily.Variant.TRAPDOOR, (BiFunction) (p_176638_, p_176639_) -> trapdoorBuilder(p_176638_, Ingredient.of(p_176639_))).put(BlockFamily.Variant.WALL, (BiFunction) (p_248024_, p_248025_) -> wallBuilder(RecipeCategory.DECORATIONS, p_248024_, Ingredient.of(p_248025_))).build();

    public RecipeProvider(PackOutput packOutput0) {
        this.recipePathProvider = packOutput0.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
        this.advancementPathProvider = packOutput0.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        Set<ResourceLocation> $$1 = Sets.newHashSet();
        List<CompletableFuture<?>> $$2 = new ArrayList();
        this.buildRecipes(p_253413_ -> {
            if (!$$1.add(p_253413_.getId())) {
                throw new IllegalStateException("Duplicate recipe " + p_253413_.getId());
            } else {
                $$2.add(DataProvider.saveStable(cachedOutput0, p_253413_.serializeRecipe(), this.recipePathProvider.json(p_253413_.getId())));
                JsonObject $$4 = p_253413_.serializeAdvancement();
                if ($$4 != null) {
                    $$2.add(DataProvider.saveStable(cachedOutput0, $$4, this.advancementPathProvider.json(p_253413_.getAdvancementId())));
                }
            }
        });
        return CompletableFuture.allOf((CompletableFuture[]) $$2.toArray(CompletableFuture[]::new));
    }

    protected CompletableFuture<?> buildAdvancement(CachedOutput cachedOutput0, ResourceLocation resourceLocation1, Advancement.Builder advancementBuilder2) {
        return DataProvider.saveStable(cachedOutput0, advancementBuilder2.serializeToJson(), this.advancementPathProvider.json(resourceLocation1));
    }

    protected abstract void buildRecipes(Consumer<FinishedRecipe> var1);

    protected static void generateForEnabledBlockFamilies(Consumer<FinishedRecipe> consumerFinishedRecipe0, FeatureFlagSet featureFlagSet1) {
        BlockFamilies.getAllFamilies().filter(p_248034_ -> p_248034_.shouldGenerateRecipe(featureFlagSet1)).forEach(p_176624_ -> generateRecipes(consumerFinishedRecipe0, p_176624_));
    }

    protected static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2, @Nullable String string3) {
        oneToOneConversionRecipe(consumerFinishedRecipe0, itemLike1, itemLike2, string3, 1);
    }

    protected static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2, @Nullable String string3, int int4) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, itemLike1, int4).requires(itemLike2).group(string3).unlockedBy(getHasName(itemLike2), has(itemLike2)).m_176500_(consumerFinishedRecipe0, getConversionRecipeName(itemLike1, itemLike2));
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> consumerFinishedRecipe0, List<ItemLike> listItemLike1, RecipeCategory recipeCategory2, ItemLike itemLike3, float float4, int int5, String string6) {
        oreCooking(consumerFinishedRecipe0, RecipeSerializer.SMELTING_RECIPE, listItemLike1, recipeCategory2, itemLike3, float4, int5, string6, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> consumerFinishedRecipe0, List<ItemLike> listItemLike1, RecipeCategory recipeCategory2, ItemLike itemLike3, float float4, int int5, String string6) {
        oreCooking(consumerFinishedRecipe0, RecipeSerializer.BLASTING_RECIPE, listItemLike1, recipeCategory2, itemLike3, float4, int5, string6, "_from_blasting");
    }

    private static void oreCooking(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializerExtendsAbstractCookingRecipe1, List<ItemLike> listItemLike2, RecipeCategory recipeCategory3, ItemLike itemLike4, float float5, int int6, String string7, String string8) {
        for (ItemLike $$9 : listItemLike2) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of($$9), recipeCategory3, itemLike4, float5, int6, recipeSerializerExtendsAbstractCookingRecipe1).group(string7).unlockedBy(getHasName($$9), has($$9)).m_176500_(consumerFinishedRecipe0, getItemName(itemLike4) + string8 + "_" + getItemName($$9));
        }
    }

    protected static void netheriteSmithing(Consumer<FinishedRecipe> consumerFinishedRecipe0, Item item1, RecipeCategory recipeCategory2, Item item3) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(item1), Ingredient.of(Items.NETHERITE_INGOT), recipeCategory2, item3).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(consumerFinishedRecipe0, getItemName(item3) + "_smithing");
    }

    protected static void trimSmithing(Consumer<FinishedRecipe> consumerFinishedRecipe0, Item item1, ResourceLocation resourceLocation2) {
        SmithingTrimRecipeBuilder.smithingTrim(Ingredient.of(item1), Ingredient.of(ItemTags.TRIMMABLE_ARMOR), Ingredient.of(ItemTags.TRIM_MATERIALS), RecipeCategory.MISC).unlocks("has_smithing_trim_template", has(item1)).save(consumerFinishedRecipe0, resourceLocation2);
    }

    protected static void twoByTwoPacker(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        ShapedRecipeBuilder.shaped(recipeCategory1, itemLike2, 1).define('#', itemLike3).pattern("##").pattern("##").unlockedBy(getHasName(itemLike3), has(itemLike3)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void threeByThreePacker(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3, String string4) {
        ShapelessRecipeBuilder.shapeless(recipeCategory1, itemLike2).requires(itemLike3, 9).unlockedBy(string4, has(itemLike3)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void threeByThreePacker(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        threeByThreePacker(consumerFinishedRecipe0, recipeCategory1, itemLike2, itemLike3, getHasName(itemLike3));
    }

    protected static void planksFromLog(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, TagKey<Item> tagKeyItem2, int int3) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, itemLike1, int3).requires(tagKeyItem2).group("planks").unlockedBy("has_log", has(tagKeyItem2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void planksFromLogs(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, TagKey<Item> tagKeyItem2, int int3) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, itemLike1, int3).requires(tagKeyItem2).group("planks").unlockedBy("has_logs", has(tagKeyItem2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void woodFromLogs(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike1, 3).define('#', itemLike2).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void woodenBoat(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, itemLike1).define('#', itemLike2).pattern("# #").pattern("###").group("boat").unlockedBy("in_water", insideOf(Blocks.WATER)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void chestBoat(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, itemLike1).requires(Blocks.CHEST).requires(itemLike2).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).m_176498_(consumerFinishedRecipe0);
    }

    private static RecipeBuilder buttonBuilder(ItemLike itemLike0, Ingredient ingredient1) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, itemLike0).requires(ingredient1);
    }

    protected static RecipeBuilder doorBuilder(ItemLike itemLike0, Ingredient ingredient1) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, itemLike0, 3).define('#', ingredient1).pattern("##").pattern("##").pattern("##");
    }

    private static RecipeBuilder fenceBuilder(ItemLike itemLike0, Ingredient ingredient1) {
        int $$2 = itemLike0 == Blocks.NETHER_BRICK_FENCE ? 6 : 3;
        Item $$3 = itemLike0 == Blocks.NETHER_BRICK_FENCE ? Items.NETHER_BRICK : Items.STICK;
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike0, $$2).define('W', ingredient1).define('#', $$3).pattern("W#W").pattern("W#W");
    }

    private static RecipeBuilder fenceGateBuilder(ItemLike itemLike0, Ingredient ingredient1) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, itemLike0).define('#', Items.STICK).define('W', ingredient1).pattern("#W#").pattern("#W#");
    }

    protected static void pressurePlate(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        pressurePlateBuilder(RecipeCategory.REDSTONE, itemLike1, Ingredient.of(itemLike2)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumerFinishedRecipe0);
    }

    private static RecipeBuilder pressurePlateBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, Ingredient ingredient2) {
        return ShapedRecipeBuilder.shaped(recipeCategory0, itemLike1).define('#', ingredient2).pattern("##");
    }

    protected static void slab(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        slabBuilder(recipeCategory1, itemLike2, Ingredient.of(itemLike3)).unlockedBy(getHasName(itemLike3), has(itemLike3)).save(consumerFinishedRecipe0);
    }

    protected static RecipeBuilder slabBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, Ingredient ingredient2) {
        return ShapedRecipeBuilder.shaped(recipeCategory0, itemLike1, 6).define('#', ingredient2).pattern("###");
    }

    protected static RecipeBuilder stairBuilder(ItemLike itemLike0, Ingredient ingredient1) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike0, 4).define('#', ingredient1).pattern("#  ").pattern("## ").pattern("###");
    }

    private static RecipeBuilder trapdoorBuilder(ItemLike itemLike0, Ingredient ingredient1) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, itemLike0, 2).define('#', ingredient1).pattern("###").pattern("###");
    }

    private static RecipeBuilder signBuilder(ItemLike itemLike0, Ingredient ingredient1) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike0, 3).group("sign").define('#', ingredient1).define('X', Items.STICK).pattern("###").pattern("###").pattern(" X ");
    }

    protected static void hangingSign(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike1, 6).group("hanging_sign").define('#', itemLike2).define('X', Items.CHAIN).pattern("X X").pattern("###").pattern("###").unlockedBy("has_stripped_logs", has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void colorBlockWithDye(Consumer<FinishedRecipe> consumerFinishedRecipe0, List<Item> listItem1, List<Item> listItem2, String string3) {
        for (int $$4 = 0; $$4 < listItem1.size(); $$4++) {
            Item $$5 = (Item) listItem1.get($$4);
            Item $$6 = (Item) listItem2.get($$4);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, $$6).requires($$5).requires(Ingredient.of(listItem2.stream().filter(p_288265_ -> !p_288265_.equals($$6)).map(ItemStack::new))).group(string3).unlockedBy("has_needed_dye", has($$5)).m_176500_(consumerFinishedRecipe0, "dye_" + getItemName($$6));
        }
    }

    protected static void carpet(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike1, 3).define('#', itemLike2).pattern("##").group("carpet").unlockedBy(getHasName(itemLike2), has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void bedFromPlanksAndWool(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike1).define('#', itemLike2).define('X', ItemTags.PLANKS).pattern("###").pattern("XXX").group("bed").unlockedBy(getHasName(itemLike2), has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void banner(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike1).define('#', itemLike2).define('|', Items.STICK).pattern("###").pattern("###").pattern(" | ").group("banner").unlockedBy(getHasName(itemLike2), has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void stainedGlassFromGlassAndDye(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike1, 8).define('#', Blocks.GLASS).define('X', itemLike2).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", has(Blocks.GLASS)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void stainedGlassPaneFromStainedGlass(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike1, 16).define('#', itemLike2).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void stainedGlassPaneFromGlassPaneAndDye(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike1, 8).define('#', Blocks.GLASS_PANE).define('$', itemLike2).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE)).unlockedBy(getHasName(itemLike2), has(itemLike2)).m_176500_(consumerFinishedRecipe0, getConversionRecipeName(itemLike1, Blocks.GLASS_PANE));
    }

    protected static void coloredTerracottaFromTerracottaAndDye(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike1, 8).define('#', Blocks.TERRACOTTA).define('X', itemLike2).pattern("###").pattern("#X#").pattern("###").group("stained_terracotta").unlockedBy("has_terracotta", has(Blocks.TERRACOTTA)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void concretePowder(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, itemLike1, 8).requires(itemLike2).requires(Blocks.SAND, 4).requires(Blocks.GRAVEL, 4).group("concrete_powder").unlockedBy("has_sand", has(Blocks.SAND)).unlockedBy("has_gravel", has(Blocks.GRAVEL)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void candle(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, itemLike1).requires(Blocks.CANDLE).requires(itemLike2).group("dyed_candle").unlockedBy(getHasName(itemLike2), has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void wall(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        wallBuilder(recipeCategory1, itemLike2, Ingredient.of(itemLike3)).unlockedBy(getHasName(itemLike3), has(itemLike3)).save(consumerFinishedRecipe0);
    }

    private static RecipeBuilder wallBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, Ingredient ingredient2) {
        return ShapedRecipeBuilder.shaped(recipeCategory0, itemLike1, 6).define('#', ingredient2).pattern("###").pattern("###");
    }

    protected static void polished(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        polishedBuilder(recipeCategory1, itemLike2, Ingredient.of(itemLike3)).unlockedBy(getHasName(itemLike3), has(itemLike3)).save(consumerFinishedRecipe0);
    }

    private static RecipeBuilder polishedBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, Ingredient ingredient2) {
        return ShapedRecipeBuilder.shaped(recipeCategory0, itemLike1, 4).define('S', ingredient2).pattern("SS").pattern("SS");
    }

    protected static void cut(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        cutBuilder(recipeCategory1, itemLike2, Ingredient.of(itemLike3)).unlockedBy(getHasName(itemLike3), has(itemLike3)).m_176498_(consumerFinishedRecipe0);
    }

    private static ShapedRecipeBuilder cutBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, Ingredient ingredient2) {
        return ShapedRecipeBuilder.shaped(recipeCategory0, itemLike1, 4).define('#', ingredient2).pattern("##").pattern("##");
    }

    protected static void chiseled(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        chiseledBuilder(recipeCategory1, itemLike2, Ingredient.of(itemLike3)).unlockedBy(getHasName(itemLike3), has(itemLike3)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void mosaicBuilder(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        ShapedRecipeBuilder.shaped(recipeCategory1, itemLike2).define('#', itemLike3).pattern("#").pattern("#").unlockedBy(getHasName(itemLike3), has(itemLike3)).m_176498_(consumerFinishedRecipe0);
    }

    protected static ShapedRecipeBuilder chiseledBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, Ingredient ingredient2) {
        return ShapedRecipeBuilder.shaped(recipeCategory0, itemLike1).define('#', ingredient2).pattern("#").pattern("#");
    }

    protected static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3) {
        stonecutterResultFromBase(consumerFinishedRecipe0, recipeCategory1, itemLike2, itemLike3, 1);
    }

    protected static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, ItemLike itemLike3, int int4) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike3), recipeCategory1, itemLike2, int4).unlockedBy(getHasName(itemLike3), has(itemLike3)).m_176500_(consumerFinishedRecipe0, getConversionRecipeName(itemLike2, itemLike3) + "_stonecutting");
    }

    private static void smeltingResultFromBase(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(itemLike2), RecipeCategory.BUILDING_BLOCKS, itemLike1, 0.1F, 200).unlockedBy(getHasName(itemLike2), has(itemLike2)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, RecipeCategory recipeCategory3, ItemLike itemLike4) {
        nineBlockStorageRecipes(consumerFinishedRecipe0, recipeCategory1, itemLike2, recipeCategory3, itemLike4, getSimpleRecipeName(itemLike4), null, getSimpleRecipeName(itemLike2), null);
    }

    protected static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, RecipeCategory recipeCategory3, ItemLike itemLike4, String string5, String string6) {
        nineBlockStorageRecipes(consumerFinishedRecipe0, recipeCategory1, itemLike2, recipeCategory3, itemLike4, string5, string6, getSimpleRecipeName(itemLike2), null);
    }

    protected static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, RecipeCategory recipeCategory3, ItemLike itemLike4, String string5, String string6) {
        nineBlockStorageRecipes(consumerFinishedRecipe0, recipeCategory1, itemLike2, recipeCategory3, itemLike4, getSimpleRecipeName(itemLike4), null, string5, string6);
    }

    private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumerFinishedRecipe0, RecipeCategory recipeCategory1, ItemLike itemLike2, RecipeCategory recipeCategory3, ItemLike itemLike4, String string5, @Nullable String string6, String string7, @Nullable String string8) {
        ShapelessRecipeBuilder.shapeless(recipeCategory1, itemLike2, 9).requires(itemLike4).group(string8).unlockedBy(getHasName(itemLike4), has(itemLike4)).save(consumerFinishedRecipe0, new ResourceLocation(string7));
        ShapedRecipeBuilder.shaped(recipeCategory3, itemLike4).define('#', itemLike2).pattern("###").pattern("###").pattern("###").group(string6).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumerFinishedRecipe0, new ResourceLocation(string5));
    }

    protected static void copySmithingTemplate(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, TagKey<Item> tagKeyItem2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemLike1, 2).define('#', Items.DIAMOND).define('C', tagKeyItem2).define('S', itemLike1).pattern("#S#").pattern("#C#").pattern("###").unlockedBy(getHasName(itemLike1), has(itemLike1)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void copySmithingTemplate(Consumer<FinishedRecipe> consumerFinishedRecipe0, ItemLike itemLike1, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemLike1, 2).define('#', Items.DIAMOND).define('C', itemLike2).define('S', itemLike1).pattern("#S#").pattern("#C#").pattern("###").unlockedBy(getHasName(itemLike1), has(itemLike1)).m_176498_(consumerFinishedRecipe0);
    }

    protected static void cookRecipes(Consumer<FinishedRecipe> consumerFinishedRecipe0, String string1, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializerExtendsAbstractCookingRecipe2, int int3) {
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.BEEF, Items.COOKED_BEEF, 0.35F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.CHICKEN, Items.COOKED_CHICKEN, 0.35F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.COD, Items.COOKED_COD, 0.35F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.KELP, Items.DRIED_KELP, 0.1F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.SALMON, Items.COOKED_SALMON, 0.35F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.MUTTON, Items.COOKED_MUTTON, 0.35F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.PORKCHOP, Items.COOKED_PORKCHOP, 0.35F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.POTATO, Items.BAKED_POTATO, 0.35F);
        simpleCookingRecipe(consumerFinishedRecipe0, string1, recipeSerializerExtendsAbstractCookingRecipe2, int3, Items.RABBIT, Items.COOKED_RABBIT, 0.35F);
    }

    private static void simpleCookingRecipe(Consumer<FinishedRecipe> consumerFinishedRecipe0, String string1, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializerExtendsAbstractCookingRecipe2, int int3, ItemLike itemLike4, ItemLike itemLike5, float float6) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(itemLike4), RecipeCategory.FOOD, itemLike5, float6, int3, recipeSerializerExtendsAbstractCookingRecipe2).unlockedBy(getHasName(itemLike4), has(itemLike4)).m_176500_(consumerFinishedRecipe0, getItemName(itemLike5) + "_from_" + string1);
    }

    protected static void waxRecipes(Consumer<FinishedRecipe> consumerFinishedRecipe0) {
        ((BiMap) HoneycombItem.WAXABLES.get()).forEach((p_248022_, p_248023_) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, p_248023_).requires(p_248022_).requires(Items.HONEYCOMB).group(getItemName(p_248023_)).unlockedBy(getHasName(p_248022_), has(p_248022_)).m_176500_(consumerFinishedRecipe0, getConversionRecipeName(p_248023_, Items.HONEYCOMB)));
    }

    protected static void generateRecipes(Consumer<FinishedRecipe> consumerFinishedRecipe0, BlockFamily blockFamily1) {
        blockFamily1.getVariants().forEach((p_176529_, p_176530_) -> {
            BiFunction<ItemLike, ItemLike, RecipeBuilder> $$4 = (BiFunction<ItemLike, ItemLike, RecipeBuilder>) SHAPE_BUILDERS.get(p_176529_);
            ItemLike $$5 = getBaseBlock(blockFamily1, p_176529_);
            if ($$4 != null) {
                RecipeBuilder $$6 = (RecipeBuilder) $$4.apply(p_176530_, $$5);
                blockFamily1.getRecipeGroupPrefix().ifPresent(p_176601_ -> $$6.group(p_176601_ + (p_176529_ == BlockFamily.Variant.CUT ? "" : "_" + p_176529_.getName())));
                $$6.unlockedBy((String) blockFamily1.getRecipeUnlockedBy().orElseGet(() -> getHasName($$5)), has($$5));
                $$6.save(consumerFinishedRecipe0);
            }
            if (p_176529_ == BlockFamily.Variant.CRACKED) {
                smeltingResultFromBase(consumerFinishedRecipe0, p_176530_, $$5);
            }
        });
    }

    private static Block getBaseBlock(BlockFamily blockFamily0, BlockFamily.Variant blockFamilyVariant1) {
        if (blockFamilyVariant1 == BlockFamily.Variant.CHISELED) {
            if (!blockFamily0.getVariants().containsKey(BlockFamily.Variant.SLAB)) {
                throw new IllegalStateException("Slab is not defined for the family.");
            } else {
                return blockFamily0.get(BlockFamily.Variant.SLAB);
            }
        } else {
            return blockFamily0.getBaseBlock();
        }
    }

    private static EnterBlockTrigger.TriggerInstance insideOf(Block block0) {
        return new EnterBlockTrigger.TriggerInstance(ContextAwarePredicate.ANY, block0, StatePropertiesPredicate.ANY);
    }

    private static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints minMaxBoundsInts0, ItemLike itemLike1) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike1).withCount(minMaxBoundsInts0).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike0) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike0).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> tagKeyItem0) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tagKeyItem0).build());
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... itemPredicate0) {
        return new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicate0);
    }

    protected static String getHasName(ItemLike itemLike0) {
        return "has_" + getItemName(itemLike0);
    }

    protected static String getItemName(ItemLike itemLike0) {
        return BuiltInRegistries.ITEM.getKey(itemLike0.asItem()).getPath();
    }

    protected static String getSimpleRecipeName(ItemLike itemLike0) {
        return getItemName(itemLike0);
    }

    protected static String getConversionRecipeName(ItemLike itemLike0, ItemLike itemLike1) {
        return getItemName(itemLike0) + "_from_" + getItemName(itemLike1);
    }

    protected static String getSmeltingRecipeName(ItemLike itemLike0) {
        return getItemName(itemLike0) + "_from_smelting";
    }

    protected static String getBlastingRecipeName(ItemLike itemLike0) {
        return getItemName(itemLike0) + "_from_blasting";
    }

    @Override
    public final String getName() {
        return "Recipes";
    }
}