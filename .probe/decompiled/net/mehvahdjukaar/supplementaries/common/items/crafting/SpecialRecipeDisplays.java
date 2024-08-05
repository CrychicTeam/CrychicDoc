package net.mehvahdjukaar.supplementaries.common.items.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.mehvahdjukaar.supplementaries.common.items.AntiqueInkItem;
import net.mehvahdjukaar.supplementaries.common.items.BambooSpikesTippedItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;

public class SpecialRecipeDisplays {

    private static List<CraftingRecipe> createAntiqueMapSoapRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.antique_map_clean";
        ItemStack antique = new ItemStack(Items.FILLED_MAP);
        antique.setHoverName(Component.translatable("filled_map.antique"));
        AntiqueInkItem.setAntiqueInk(antique, true);
        Ingredient soap = Ingredient.of(new ItemStack((ItemLike) ModRegistry.SOAP.get()));
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(antique), soap);
        ResourceLocation id = new ResourceLocation("supplementaries", "antique_map_clean_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.MISC, new ItemStack(Items.FILLED_MAP), inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static List<CraftingRecipe> createAntiqueMapRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.antique_map";
        ItemStack stack = new ItemStack(Items.FILLED_MAP);
        stack.setHoverName(Component.translatable("filled_map.antique"));
        AntiqueInkItem.setAntiqueInk(stack, true);
        Ingredient ink = Ingredient.of(new ItemStack((ItemLike) ModRegistry.ANTIQUE_INK.get()));
        Ingredient map = Ingredient.of(new ItemStack(Items.FILLED_MAP));
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, map, ink);
        ResourceLocation id = new ResourceLocation("supplementaries", "antique_map_create_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.MISC, stack, inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static List<CraftingRecipe> createAntiqueBookRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.antique_book";
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        AntiqueInkItem.setAntiqueInk(stack, true);
        Ingredient ink = Ingredient.of(new ItemStack((ItemLike) ModRegistry.ANTIQUE_INK.get()));
        Ingredient map = Ingredient.of(new ItemStack(Items.WRITTEN_BOOK));
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, map, ink);
        ResourceLocation id = new ResourceLocation("supplementaries", "antique_book_create_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.MISC, stack, inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static List<CraftingRecipe> createRopeArrowCreateRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.rope_arrow";
        ItemStack ropeArrow = new ItemStack((ItemLike) ModRegistry.ROPE_ARROW_ITEM.get());
        ropeArrow.setDamageValue(ropeArrow.getMaxDamage() - 4);
        Ingredient arrow = Ingredient.of(new ItemStack(Items.ARROW));
        Ingredient rope = Ingredient.of(ModTags.ROPES);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, arrow, rope, rope, rope, rope);
        ResourceLocation id = new ResourceLocation("supplementaries", "rope_arrow_create_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.EQUIPMENT, ropeArrow, inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static List<CraftingRecipe> createRopeArrowAddRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.rope_arrow_add";
        ItemStack ropeArrow = new ItemStack((ItemLike) ModRegistry.ROPE_ARROW_ITEM.get());
        ItemStack ropeArrow2 = ropeArrow.copy();
        ropeArrow2.setDamageValue(8);
        Ingredient arrow = Ingredient.of(ropeArrow2);
        Ingredient rope = Ingredient.of(ModTags.ROPES);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, rope, rope, rope, rope, arrow, rope, rope, rope, rope);
        ResourceLocation id = new ResourceLocation("supplementaries", "rope_arrow_add_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.EQUIPMENT, ropeArrow, inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static List<CraftingRecipe> createSoapCleanRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.soap";
        for (String k : BlocksColorAPI.getBlockKeys()) {
            if (!((List) CommonConfigs.Functional.SOAP_DYE_CLEAN_BLACKLIST.get()).contains(k)) {
                HolderSet<Item> n = BlocksColorAPI.getItemHolderSet(k);
                Item out = BlocksColorAPI.getColoredItem(k, null);
                if (n != null && out != null) {
                    Ingredient ing = (Ingredient) n.unwrap().map(Ingredient::m_204132_, l -> Ingredient.of(l.stream().map(Holder::m_203334_).map(Item::m_7968_)));
                    NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, ing, Ingredient.of((ItemLike) ModRegistry.SOAP.get()));
                    ItemStack output = out.getDefaultInstance();
                    ResourceLocation id = Supplementaries.res("soap_clean_" + k.replace(":", "_"));
                    ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs);
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    private static List<CraftingRecipe> makeTrappedPresentRecipes() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.trapped_presents";
        for (DyeColor color : DyeColor.values()) {
            Ingredient baseShulkerIngredient = Ingredient.of((ItemLike) ((Supplier) ModRegistry.PRESENTS.get(color)).get());
            ItemStack output = ((Block) ((Supplier) ModRegistry.TRAPPED_PRESENTS.get(color)).get()).asItem().getDefaultInstance();
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, baseShulkerIngredient, Ingredient.of(Items.TRIPWIRE_HOOK));
            ResourceLocation id = Supplementaries.res("trapped_present_" + color.getName() + "_display");
            recipes.add(new ShapelessRecipe(id, group, CraftingBookCategory.BUILDING, output, inputs));
        }
        Ingredient ingredients = Ingredient.of((ItemLike) ((Supplier) ModRegistry.PRESENTS.get(null)).get());
        ItemStack output = ((Block) ((Supplier) ModRegistry.TRAPPED_PRESENTS.get(null)).get()).asItem().getDefaultInstance();
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, ingredients, Ingredient.of(Items.TRIPWIRE_HOOK));
        ResourceLocation id = Supplementaries.res("trapped_present_display");
        recipes.add(new ShapelessRecipe(id, group, CraftingBookCategory.BUILDING, output, inputs));
        return recipes;
    }

    private static List<CraftingRecipe> makePresentColoringRecipes() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "presents";
        Ingredient ingredients = Ingredient.of((ItemLike) ((Supplier) ModRegistry.PRESENTS.get(null)).get());
        for (DyeColor color : DyeColor.values()) {
            DyeItem dye = DyeItem.byColor(color);
            ItemStack output = ((Block) ((Supplier) ModRegistry.PRESENTS.get(color)).get()).asItem().getDefaultInstance();
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, ingredients, Ingredient.of(dye));
            ResourceLocation id = Supplementaries.res("present_" + color.getName() + "_display");
            recipes.add(new ShapelessRecipe(id, group, CraftingBookCategory.BUILDING, output, inputs));
        }
        return recipes;
    }

    private static List<CraftingRecipe> makeSackColoringRecipes() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "sacks";
        Ingredient ingredients = Ingredient.of((ItemLike) ModRegistry.SACK.get());
        for (DyeColor color : DyeColor.values()) {
            DyeItem dye = DyeItem.byColor(color);
            ItemStack output = BlocksColorAPI.changeColor(((Block) ModRegistry.SACK.get()).asItem(), color).getDefaultInstance();
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, ingredients, Ingredient.of(dye));
            ResourceLocation id = Supplementaries.res("sack_" + color.getName() + "_display");
            recipes.add(new ShapelessRecipe(id, group, CraftingBookCategory.BUILDING, output, inputs));
        }
        return recipes;
    }

    private static List<CraftingRecipe> createItemLoreRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "item_lore";
        ItemStack output = new ItemStack(Items.SLIME_BALL);
        ItemStack tag = new ItemStack(Items.NAME_TAG);
        MutableComponent c = Component.literal("Ew sticky!");
        tag.setHoverName(c);
        ItemLoreRecipe.addLore(c, output);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.SLIME_BALL), Ingredient.of(tag));
        ResourceLocation id = new ResourceLocation("supplementaries", "item_lore_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static List<CraftingRecipe> createBubbleBlowerChargeRecipe() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "bubble_blower";
        ItemStack ropeArrow = new ItemStack((ItemLike) ModRegistry.BUBBLE_BLOWER.get());
        ItemStack empty = ropeArrow.copy();
        empty.setDamageValue(empty.getMaxDamage());
        Ingredient base = Ingredient.of(empty);
        Ingredient soap = Ingredient.of((ItemLike) ModRegistry.SOAP.get());
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, base, soap);
        ResourceLocation id = new ResourceLocation("supplementaries", "bubble_blower_charge_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.MISC, ropeArrow, inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static List<CraftingRecipe> createTippedBambooSpikesRecipes() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "tipped_spikes";
        for (Potion potionType : BuiltInRegistries.POTION) {
            if (!potionType.getEffects().isEmpty() && BambooSpikesTippedItem.isPotionValid(potionType)) {
                recipes.add(makeSpikeRecipe(potionType, group));
            }
        }
        return recipes;
    }

    private static ShapelessRecipe makeSpikeRecipe(Potion potionType, String group) {
        ItemStack spikes = new ItemStack((ItemLike) ModRegistry.BAMBOO_SPIKES_ITEM.get());
        ItemStack lingeringPotion = PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potionType);
        Ingredient spikeIngredient = Ingredient.of(spikes);
        Ingredient potionIngredient = Ingredient.of(lingeringPotion);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, spikeIngredient, potionIngredient);
        ItemStack output = BambooSpikesTippedItem.makeSpikeItem(potionType);
        ResourceLocation id = new ResourceLocation("supplementaries", potionType.getName("tipped_spikes_display."));
        return new ShapelessRecipe(id, group, CraftingBookCategory.BUILDING, output, inputs);
    }

    private static List<CraftingRecipe> createFlagFromBanner() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "flag_from_banner";
        for (DyeColor color : DyeColor.values()) {
            ItemStack banner = new ItemStack(BannerBlock.byColor(color).asItem());
            ItemStack fullFlag = new ItemStack((ItemLike) ((Supplier) ModRegistry.FLAGS.get(color)).get());
            ListTag list = new ListTag();
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("Pattern", "mojang");
            compoundTag.putInt("Color", color == DyeColor.WHITE ? DyeColor.BLACK.getId() : DyeColor.WHITE.getId());
            list.add(compoundTag);
            CompoundTag com = banner.getOrCreateTagElement("BlockEntityTag");
            com.put("Patterns", list);
            CompoundTag com2 = fullFlag.getOrCreateTagElement("BlockEntityTag");
            com2.put("Patterns", list);
            Ingredient emptyFlag = Ingredient.of(new ItemStack((ItemLike) ((Supplier) ModRegistry.FLAGS.get(color)).get()));
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, emptyFlag, Ingredient.of(banner));
            ResourceLocation id = new ResourceLocation("supplementaries", "flag_from_banner_display_" + color.getName());
            ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.BUILDING, fullFlag, inputs);
            recipes.add(recipe);
        }
        return recipes;
    }

    private static List<CraftingRecipe> createBlackboardDuplicate() {
        List<CraftingRecipe> recipes = new ArrayList();
        String group = "supplementaries.blackboard_duplicate";
        ItemStack blackboard = getTroll();
        Ingredient emptyBoard = Ingredient.of(new ItemStack((ItemLike) ModRegistry.BLACKBOARD_ITEM.get()));
        Ingredient fullBoard = Ingredient.of(blackboard);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, emptyBoard, fullBoard);
        ResourceLocation id = new ResourceLocation("supplementaries", "blackboard_duplicate_display");
        ShapelessRecipe recipe = new ShapelessRecipe(id, group, CraftingBookCategory.BUILDING, blackboard, inputs);
        recipes.add(recipe);
        return recipes;
    }

    private static ItemStack getTroll() {
        ItemStack blackboard = new ItemStack((ItemLike) ModRegistry.BLACKBOARD_ITEM.get());
        CompoundTag com = new CompoundTag();
        byte[][] pixels = new byte[][] { { 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 } };
        com.putLongArray("Pixels", BlackboardBlockTile.packPixels(pixels));
        blackboard.addTagElement("BlockEntityTag", com);
        return blackboard;
    }

    private static ItemStack getSans() {
        ItemStack blackboard = new ItemStack((ItemLike) ModRegistry.BLACKBOARD_ITEM.get());
        CompoundTag com = new CompoundTag();
        byte[][] pixels = new byte[][] { { 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0, 3, 0, 0, 0, 1, 1, 1, 0, 1 }, { 0, 1, 0, 0, 0, 3, 3, 3, 3, 0, 0, 1, 0, 1, 0, 1 }, { 0, 1, 0, 0, 3, 0, 3, 0, 3, 0, 0, 1, 1, 0, 1, 0 }, { 0, 0, 1, 0, 0, 0, 3, 3, 3, 0, 1, 1, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0 } };
        com.putLongArray("Pixels", BlackboardBlockTile.packPixels(pixels));
        blackboard.addTagElement("BlockEntityTag", com);
        return blackboard;
    }

    public static void registerCraftingRecipes(Consumer<List<CraftingRecipe>> registry) {
        for (RecipeBookCategories c : (List) RecipeBookCategories.AGGREGATE_CATEGORIES.get(RecipeBookCategories.CRAFTING_SEARCH)) {
            registerRecipes(c, registry);
        }
    }

    public static void registerRecipes(RecipeBookCategories category, Consumer<List<CraftingRecipe>> registry) {
        if (category == RecipeBookCategories.CRAFTING_MISC) {
            if ((Boolean) CommonConfigs.Functional.TIPPED_SPIKES_ENABLED.get()) {
                registry.accept(createTippedBambooSpikesRecipes());
            }
            if ((Boolean) CommonConfigs.Building.FLAG_ENABLED.get()) {
                registry.accept(createFlagFromBanner());
            }
            if ((Boolean) CommonConfigs.Tools.ANTIQUE_INK_ENABLED.get()) {
                registry.accept(createAntiqueMapRecipe());
                registry.accept(createAntiqueBookRecipe());
            }
            if ((Boolean) CommonConfigs.Functional.SACK_ENABLED.get() && CompatHandler.SUPPSQUARED) {
                registry.accept(makeSackColoringRecipes());
            }
            registry.accept(createItemLoreRecipe());
            if ((Boolean) CommonConfigs.Functional.SOAP_ENABLED.get()) {
                registry.accept(createSoapCleanRecipe());
                if ((Boolean) CommonConfigs.Tools.ANTIQUE_INK_ENABLED.get()) {
                }
            }
            if ((Boolean) CommonConfigs.Functional.PRESENT_ENABLED.get()) {
                registry.accept(makePresentColoringRecipes());
                if ((Boolean) CommonConfigs.Functional.TRAPPED_PRESENT_ENABLED.get()) {
                    registry.accept(makeTrappedPresentRecipes());
                }
            }
        } else if (category == RecipeBookCategories.CRAFTING_BUILDING_BLOCKS) {
            if ((Boolean) CommonConfigs.Building.BLACKBOARD_ENABLED.get()) {
                registry.accept(createBlackboardDuplicate());
            }
        } else if (category == RecipeBookCategories.CRAFTING_EQUIPMENT) {
            if ((Boolean) CommonConfigs.Tools.ROPE_ARROW_ENABLED.get()) {
                registry.accept(createRopeArrowCreateRecipe());
                registry.accept(createRopeArrowAddRecipe());
            }
            if ((Boolean) CommonConfigs.Tools.BUBBLE_BLOWER_ENABLED.get()) {
                registry.accept(createBubbleBlowerChargeRecipe());
            }
        }
    }
}