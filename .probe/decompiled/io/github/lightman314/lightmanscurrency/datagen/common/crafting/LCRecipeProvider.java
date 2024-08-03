package io.github.lightman314.lightmanscurrency.datagen.common.crafting;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCTags;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBundle;
import io.github.lightman314.lightmanscurrency.common.core.variants.Color;
import io.github.lightman314.lightmanscurrency.common.core.variants.WoodType;
import io.github.lightman314.lightmanscurrency.common.crafting.condition.LCCraftingConditions;
import io.github.lightman314.lightmanscurrency.datagen.common.crafting.builders.MasterTicketRecipeBuilder;
import io.github.lightman314.lightmanscurrency.datagen.common.crafting.builders.MintRecipeBuilder;
import io.github.lightman314.lightmanscurrency.datagen.common.crafting.builders.TicketRecipeBuilder;
import io.github.lightman314.lightmanscurrency.datagen.common.crafting.builders.WalletUpgradeRecipeBuilder;
import io.github.lightman314.lightmanscurrency.datagen.util.ColorHelper;
import io.github.lightman314.lightmanscurrency.datagen.util.WoodData;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LCRecipeProvider extends RecipeProvider {

    private static final String ADV_PREFIX = "recipes/misc/";

    public LCRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        LCCraftingConditions.register();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TRADING_CORE.get()).unlockedBy("money", MoneyKnowledge()).pattern("rqr").pattern("rdr").pattern("rpr").define('r', Tags.Items.DUSTS_REDSTONE).define('q', Tags.Items.GEMS_QUARTZ).define('d', Items.DROPPER).define('p', Items.COMPARATOR).save(consumer, ItemID(ModItems.TRADING_CORE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ATM.get()).unlockedBy("money", MoneyKnowledge()).pattern("igi").pattern("igi").pattern("iri").define('i', Tags.Items.INGOTS_IRON).define('g', Tags.Items.GLASS_PANES_COLORLESS).define('r', Tags.Items.DUSTS_REDSTONE).save(consumer, ItemID(ModBlocks.ATM));
        GenerateSwapRecipes(consumer, ModBlocks.ATM.get(), ModItems.PORTABLE_ATM.get(), Lists.newArrayList(new Pair[] { Pair.of("money", MoneyKnowledge()) }));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TERMINAL.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("sgs").pattern("sgs").pattern("iei").define('e', Items.ENDER_EYE).define('g', Tags.Items.GLASS_COLORLESS).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.STONE).save(consumer, ItemID(ModBlocks.TERMINAL));
        GenerateSwapRecipes(consumer, ModBlocks.TERMINAL.get(), ModItems.PORTABLE_TERMINAL.get(), Lists.newArrayList(new Pair[] { Pair.of("money", MoneyKnowledge()), Pair.of("trader", TraderKnowledge()) }));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GEM_TERMINAL.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("aaa").pattern("aea").pattern("asa").define('e', Items.ENDER_EYE).define('a', Tags.Items.GEMS_AMETHYST).define('s', Tags.Items.STONE).save(consumer, ItemID(ModBlocks.GEM_TERMINAL));
        GenerateSwapRecipes(consumer, ModBlocks.GEM_TERMINAL.get(), ModItems.PORTABLE_GEM_TERMINAL.get(), Lists.newArrayList(new Pair[] { Pair.of("money", MoneyKnowledge()), Pair.of("trader", TraderKnowledge()) }));
        GenerateWalletRecipes(consumer, Lists.newArrayList(new Pair[] { Pair.of(Ingredient.of(Tags.Items.INGOTS_COPPER), ModItems.WALLET_COPPER), Pair.of(Ingredient.of(Tags.Items.INGOTS_IRON), ModItems.WALLET_IRON), Pair.of(Ingredient.of(Tags.Items.INGOTS_GOLD), ModItems.WALLET_GOLD), Pair.of(Ingredient.of(Tags.Items.GEMS_EMERALD), ModItems.WALLET_EMERALD), Pair.of(Ingredient.of(Tags.Items.GEMS_DIAMOND), ModItems.WALLET_DIAMOND), Pair.of(Ingredient.of(Tags.Items.INGOTS_NETHERITE), ModItems.WALLET_NETHERITE) }));
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_COPPER, ModBlocks.COINPILE_COPPER, ModBlocks.COINBLOCK_COPPER);
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_IRON, ModBlocks.COINPILE_IRON, ModBlocks.COINBLOCK_IRON);
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_GOLD, ModBlocks.COINPILE_GOLD, ModBlocks.COINBLOCK_GOLD);
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_EMERALD, ModBlocks.COINPILE_EMERALD, ModBlocks.COINBLOCK_EMERALD);
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_DIAMOND, ModBlocks.COINPILE_DIAMOND, ModBlocks.COINBLOCK_DIAMOND);
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_NETHERITE, ModBlocks.COINPILE_NETHERITE, ModBlocks.COINBLOCK_NETHERITE);
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_CHOCOLATE_COPPER, ModBlocks.COINPILE_CHOCOLATE_COPPER, ModBlocks.COINBLOCK_CHOCOLATE_COPPER, "chocolate_", LazyTrigger(LCTags.Items.EVENT_COIN_CHOCOLATE));
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_CHOCOLATE_IRON, ModBlocks.COINPILE_CHOCOLATE_IRON, ModBlocks.COINBLOCK_CHOCOLATE_IRON, "chocolate_", LazyTrigger(LCTags.Items.EVENT_COIN_CHOCOLATE));
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_CHOCOLATE_GOLD, ModBlocks.COINPILE_CHOCOLATE_GOLD, ModBlocks.COINBLOCK_CHOCOLATE_GOLD, "chocolate_", LazyTrigger(LCTags.Items.EVENT_COIN_CHOCOLATE));
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_CHOCOLATE_EMERALD, ModBlocks.COINPILE_CHOCOLATE_EMERALD, ModBlocks.COINBLOCK_CHOCOLATE_EMERALD, "chocolate_", LazyTrigger(LCTags.Items.EVENT_COIN_CHOCOLATE));
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_CHOCOLATE_DIAMOND, ModBlocks.COINPILE_CHOCOLATE_DIAMOND, ModBlocks.COINBLOCK_CHOCOLATE_DIAMOND, "chocolate_", LazyTrigger(LCTags.Items.EVENT_COIN_CHOCOLATE));
        GenerateCoinBlockRecipes(consumer, ModItems.COIN_CHOCOLATE_NETHERITE, ModBlocks.COINPILE_CHOCOLATE_NETHERITE, ModBlocks.COINBLOCK_CHOCOLATE_NETHERITE, "chocolate_", LazyTrigger(LCTags.Items.EVENT_COIN_CHOCOLATE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COIN_MINT.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("material", LazyTrigger(LCTags.Items.COIN_MINTING_MATERIAL)).pattern("ipi").pattern("i i").pattern("sss").define('i', Tags.Items.INGOTS_IRON).define('p', Items.PISTON).define('s', Items.SMOOTH_STONE).save(consumer, ItemID(ModBlocks.COIN_MINT));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TICKET_STATION.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("ticket_trader", LazyTrigger(ModBlocks.TICKET_KIOSK)).unlockedBy("ticket", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).pattern("igi").pattern("igi").pattern("rrr").define('i', Tags.Items.INGOTS_IRON).define('g', Items.INK_SAC).define('r', Tags.Items.DUSTS_REDSTONE).save(consumer, ID("ticket_station"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CASH_REGISTER.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("iii").pattern("ede").pattern("iii").define('i', Tags.Items.INGOTS_IRON).define('e', Tags.Items.ENDER_PEARLS).define('d', Items.DROPPER).save(consumer, ItemID(ModBlocks.CASH_REGISTER));
        ConditionalRecipe.Builder conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.CoinChest.INSTANCE);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COIN_CHEST.get()).unlockedBy("money", MoneyKnowledge()).pattern("ppp").pattern("prp").pattern("ppp").define('p', ItemTags.PLANKS).define('r', Items.COMPARATOR).save(conditional::addRecipe, ItemID(ModBlocks.COIN_CHEST));
        conditional.generateAdvancement(ItemID("recipes/misc/", ModBlocks.COIN_CHEST)).build(consumer, ItemID(ModBlocks.COIN_CHEST));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DISPLAY_CASE.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("g").pattern("x").pattern("w").define('x', ModItems.TRADING_CORE.get()).define('g', Tags.Items.GLASS_COLORLESS).define('w', Items.WHITE_WOOL).save(consumer, ItemID("traders/", ModBlocks.DISPLAY_CASE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.VENDING_MACHINE.get(Color.WHITE)).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("igi").pattern("igi").pattern("cxc").define('x', ModItems.TRADING_CORE.get()).define('g', Tags.Items.GLASS_COLORLESS).define('i', Tags.Items.INGOTS_IRON).define('c', Tags.Items.CHESTS_WOODEN).save(consumer, ItemID("traders/", ModBlocks.VENDING_MACHINE.get(Color.WHITE)));
        GenerateColoredDyeAndWashRecipes(consumer, ModBlocks.VENDING_MACHINE, ModBlocks.VENDING_MACHINE.get(Color.WHITE), "vending_machine_dyes", "traders/vending_machine/", Lists.newArrayList(new Pair[] { Pair.of("money", MoneyKnowledge()), Pair.of("trader", TraderKnowledge()) }));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.VENDING_MACHINE_LARGE.get(Color.WHITE)).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("vending_machine", LazyTrigger(ModBlocks.VENDING_MACHINE.get(Color.WHITE))).pattern("igi").pattern("igi").pattern("cxc").define('x', ModBlocks.VENDING_MACHINE.get(Color.WHITE)).define('g', Tags.Items.GLASS_COLORLESS).define('i', Tags.Items.INGOTS_IRON).define('c', Tags.Items.CHESTS_WOODEN).save(consumer, ItemID("traders/", ModBlocks.VENDING_MACHINE_LARGE.get(Color.WHITE)));
        GenerateColoredDyeAndWashRecipes(consumer, ModBlocks.VENDING_MACHINE_LARGE, ModBlocks.VENDING_MACHINE_LARGE.get(Color.WHITE), "vending_machine_large_dyes", "traders/large_vending_machine/", Lists.newArrayList(new Pair[] { Pair.of("money", MoneyKnowledge()), Pair.of("trader", TraderKnowledge()) }));
        ModBlocks.SHELF.forEach((woodType, shelf) -> {
            ConditionalRecipe.Builder tempConditional = woodType.isVanilla() ? null : ConditionalRecipe.builder().addCondition(new ModLoadedCondition(woodType.getModID()));
            Consumer<FinishedRecipe> cx = tempConditional == null ? consumer : tempConditional::addRecipe;
            WoodData data = woodType.getData();
            Item slab = data == null ? null : data.getSlab();
            if (slab != null) {
                ResourceLocation id = WoodID("traders/shelf/", woodType);
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) shelf.get()).group("shelf_trader").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("x").pattern("s").define('x', ModItems.TRADING_CORE.get()).define('s', slab).save(cx, id);
                if (tempConditional != null) {
                    tempConditional.generateAdvancement(id.withPrefix("recipes/misc/")).build(consumer, id);
                }
            } else {
                LightmansCurrency.LogDebug("Could not generate shelf recipe for WoodType '" + woodType.id + "' as it has no defined slab item.");
            }
        });
        ModBlocks.SHELF_2x2.forEach((woodType, shelf) -> {
            ConditionalRecipe.Builder tempConditional = woodType.isVanilla() ? null : ConditionalRecipe.builder().addCondition(new ModLoadedCondition(woodType.getModID()));
            Consumer<FinishedRecipe> cx = tempConditional == null ? consumer : tempConditional::addRecipe;
            WoodData data = woodType.getData();
            Item slab = data == null ? null : data.getSlab();
            ItemLike trader = ModBlocks.SHELF.get(woodType);
            if (slab != null && trader != null) {
                ResourceLocation id = WoodID("traders/shelf2/", woodType);
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) shelf.get()).group("shelf_trader_2x2").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("shelf", LazyTrigger(trader)).pattern("c").pattern("x").pattern("s").define('c', Tags.Items.CHESTS_WOODEN).define('x', trader).define('s', slab).save(cx, id);
                if (tempConditional != null) {
                    tempConditional.generateAdvancement(id.withPrefix("recipes/misc/")).build(consumer, id);
                }
            } else {
                LightmansCurrency.LogDebug("Could not generate 2x2 shelf recipe for WoodType '" + woodType.id + "' as it has no defined slab item.");
            }
        });
        ModBlocks.CARD_DISPLAY.forEach((woodType, color, card_display) -> {
            ConditionalRecipe.Builder tempConditional = woodType.isVanilla() ? null : ConditionalRecipe.builder().addCondition(new ModLoadedCondition(woodType.getModID()));
            Consumer<FinishedRecipe> cx = tempConditional == null ? consumer : tempConditional::addRecipe;
            WoodData data = woodType.getData();
            Item log = data == null ? null : data.getLog();
            if (log != null) {
                ResourceLocation id = ColoredWoodID("traders/card_display/", woodType, color);
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, card_display.get()).group("card_display_trader_" + woodType.id).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("  w").pattern(" xl").pattern("llc").define('x', ModItems.TRADING_CORE.get()).define('l', log).define('w', ColorHelper.GetWoolOfColor(color)).define('c', Tags.Items.CHESTS_WOODEN).save(cx, id);
                if (tempConditional != null) {
                    tempConditional.generateAdvancement(id.withPrefix("recipes/misc/")).build(consumer, id);
                }
            } else {
                LightmansCurrency.LogDebug("Could not generate card display recipe for WoodType '" + woodType.id + "' as it has no defined log item.");
            }
        });
        for (Color c : Color.values()) {
            ItemLike cp = ColorHelper.GetConcretePowderOfColor(c);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FREEZER.get(c)).group("freezer_crafting").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("igi").pattern("igi").pattern("cxc").define('x', ModItems.TRADING_CORE.get()).define('g', Tags.Items.GLASS_COLORLESS).define('i', cp).define('c', Tags.Items.CHESTS_WOODEN).save(consumer, ID("traders/freezer/" + c.getResourceSafeName()));
        }
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.NetworkTrader.INSTANCE);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ITEM_NETWORK_TRADER_1.get()).group("item_network_trader").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("terminal", LazyTrigger(LCTags.Items.NETWORK_TERMINAL)).pattern("ici").pattern("ixi").pattern("iei").define('x', ModItems.TRADING_CORE.get()).define('e', Items.ENDER_EYE).define('i', Tags.Items.INGOTS_IRON).define('c', Tags.Items.CHESTS_WOODEN).m_176498_(conditional::addRecipe);
        conditional.generateAdvancement(ID("network/item_network_trader_1").withPrefix("recipes/misc/")).build(consumer, ID("network/item_network_trader_1"));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.NetworkTrader.INSTANCE);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ITEM_NETWORK_TRADER_2.get()).group("item_network_trader").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("terminal", LazyTrigger(LCTags.Items.NETWORK_TERMINAL)).unlockedBy("previous", LazyTrigger(ModBlocks.ITEM_NETWORK_TRADER_1)).pattern("c").pattern("x").pattern("i").define('x', ModBlocks.ITEM_NETWORK_TRADER_1.get()).define('i', Tags.Items.INGOTS_IRON).define('c', Tags.Items.CHESTS_WOODEN).m_176498_(conditional::addRecipe);
        conditional.generateAdvancement(ID("network/item_network_trader_2").withPrefix("recipes/misc/")).build(consumer, ID("network/item_network_trader_2"));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.NetworkTrader.INSTANCE);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ITEM_NETWORK_TRADER_3.get()).group("item_network_trader").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("terminal", LazyTrigger(LCTags.Items.NETWORK_TERMINAL)).unlockedBy("previous", LazyTrigger(ModBlocks.ITEM_NETWORK_TRADER_2)).pattern("c").pattern("x").pattern("i").define('x', ModBlocks.ITEM_NETWORK_TRADER_2.get()).define('i', Tags.Items.INGOTS_IRON).define('c', Tags.Items.CHESTS_WOODEN).m_176498_(conditional::addRecipe);
        conditional.generateAdvancement(ID("network/item_network_trader_3").withPrefix("recipes/misc/")).build(consumer, ID("network/item_network_trader_3"));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.NetworkTrader.INSTANCE);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ITEM_NETWORK_TRADER_4.get()).group("item_network_trader").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("terminal", LazyTrigger(LCTags.Items.NETWORK_TERMINAL)).unlockedBy("previous", LazyTrigger(ModBlocks.ITEM_NETWORK_TRADER_3)).pattern("c").pattern("x").pattern("i").define('x', ModBlocks.ITEM_NETWORK_TRADER_3.get()).define('i', Tags.Items.INGOTS_IRON).define('c', Tags.Items.CHESTS_WOODEN).m_176498_(conditional::addRecipe);
        conditional.generateAdvancement(ID("network/item_network_trader_4").withPrefix("recipes/misc/")).build(consumer, ID("network/item_network_trader_4"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ARMOR_DISPLAY.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("ggg").pattern("gag").pattern("ixi").define('x', ModItems.TRADING_CORE.get()).define('g', Tags.Items.GLASS_COLORLESS).define('i', Tags.Items.INGOTS_IRON).define('a', Items.ARMOR_STAND).save(consumer, ItemID("traders/", ModBlocks.ARMOR_DISPLAY));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TICKET_KIOSK.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("iii").pattern("igi").pattern("rxr").define('i', Tags.Items.INGOTS_IRON).define('g', Items.INK_SAC).define('r', Tags.Items.DUSTS_REDSTONE).define('x', ModItems.TRADING_CORE.get()).save(consumer, ItemID("traders/", ModBlocks.TICKET_KIOSK));
        ModBlocks.BOOKSHELF_TRADER.forEach((woodType, bookshelf) -> {
            ConditionalRecipe.Builder tempConditional = woodType.isVanilla() ? null : ConditionalRecipe.builder().addCondition(new ModLoadedCondition(woodType.getModID()));
            Consumer<FinishedRecipe> cx = tempConditional == null ? consumer : tempConditional::addRecipe;
            WoodData data = woodType.getData();
            Item plank = data == null ? null : data.getPlank();
            Item slab = data == null ? null : data.getSlab();
            if (plank != null && slab != null) {
                ResourceLocation id = WoodID("traders/bookshelf/", woodType);
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) bookshelf.get()).group("bookshelf_trader").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("ppp").pattern("sxs").pattern("ppp").define('x', ModItems.TRADING_CORE.get()).define('p', plank).define('s', slab).save(cx, id);
                if (tempConditional != null) {
                    tempConditional.generateAdvancement(id.withPrefix("recipes/misc/")).build(consumer, id);
                }
            } else {
                LightmansCurrency.LogDebug("Could not generate bookshelf recipe for WoodType '" + woodType.id + "' as it has no defined plank and/or slab item.");
            }
        });
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SLOT_MACHINE.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("igi").pattern("idi").pattern("rxr").define('x', ModItems.TRADING_CORE.get()).define('g', Tags.Items.GLASS_COLORLESS).define('i', Tags.Items.INGOTS_IRON).define('r', Items.COMPARATOR).define('d', Tags.Items.DUSTS_GLOWSTONE).save(consumer, ItemID("traders/", ModBlocks.SLOT_MACHINE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PAYGATE.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("ticket", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).pattern("iri").pattern("ixi").pattern("iti").define('i', Tags.Items.INGOTS_IRON).define('t', Items.REDSTONE_TORCH).define('r', Items.REDSTONE_BLOCK).define('x', ModItems.TRADING_CORE.get()).save(consumer, ItemID("traders/", ModBlocks.PAYGATE));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.TraderInterface.INSTANCE);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ITEM_TRADER_INTERFACE.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("terminal", LazyTrigger(LCTags.Items.NETWORK_TERMINAL)).pattern("ici").pattern("iti").pattern("ici").define('i', Tags.Items.INGOTS_IRON).define('t', LCTags.Items.NETWORK_TERMINAL).define('c', Tags.Items.CHESTS_WOODEN).m_176498_(conditional::addRecipe);
        conditional.generateAdvancement(ItemID(ModBlocks.ITEM_TRADER_INTERFACE).withPrefix("recipes/misc/")).build(consumer, ItemID(ModBlocks.ITEM_TRADER_INTERFACE));
        ModBlocks.AUCTION_STAND.forEach((woodType, auction_stand) -> {
            ConditionalRecipe.Builder tempConditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.AuctionStand.INSTANCE);
            if (!woodType.isVanilla()) {
                tempConditional.addCondition(new ModLoadedCondition(woodType.getModID()));
            }
            WoodData data = woodType.getData();
            Item log = data == null ? null : data.getLog();
            if (log != null) {
                ResourceLocation id = WoodID("auction_stand/", woodType);
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) auction_stand.get()).group("auction_stand").unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).unlockedBy("terminal", LazyTrigger(LCTags.Items.NETWORK_TERMINAL)).pattern("g").pattern("x").pattern("l").define('x', ModItems.TRADING_CORE.get()).define('g', Tags.Items.GLASS_COLORLESS).define('l', log).save(tempConditional::addRecipe, id);
                tempConditional.generateAdvancement(id.withPrefix("recipes/misc/")).build(consumer, id);
            } else {
                LightmansCurrency.LogDebug("Could not generate auction stand recipe for WoodType '" + woodType.id + "' as it has no defined log item.");
            }
        });
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PIGGY_BANK.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("wallet", LazyTrigger(LCTags.Items.WALLET)).pattern("b  ").pattern("bbb").pattern("bdb").define('b', Tags.Items.INGOTS_BRICK).define('d', Tags.Items.DYES_PINK).save(consumer, ItemID("coin_jar/", ModBlocks.PIGGY_BANK));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.COINJAR_BLUE.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("wallet", LazyTrigger(LCTags.Items.WALLET)).pattern("b b").pattern("bdb").pattern(" b ").define('b', Tags.Items.INGOTS_BRICK).define('d', Tags.Items.DYES_BLUE).save(consumer, ID("coin_jar/blue"));
        GenerateMintAndMeltRecipes(consumer, ModItems.COIN_COPPER, Tags.Items.INGOTS_COPPER, Items.COPPER_INGOT);
        GenerateMintAndMeltRecipes(consumer, ModItems.COIN_IRON, Tags.Items.INGOTS_IRON, Items.IRON_INGOT);
        GenerateMintAndMeltRecipes(consumer, ModItems.COIN_GOLD, Tags.Items.INGOTS_GOLD, Items.GOLD_INGOT);
        GenerateMintAndMeltRecipes(consumer, ModItems.COIN_EMERALD, Tags.Items.GEMS_EMERALD, Items.EMERALD);
        GenerateMintAndMeltRecipes(consumer, ModItems.COIN_DIAMOND, Tags.Items.GEMS_DIAMOND, Items.DIAMOND);
        GenerateMintAndMeltRecipes(consumer, ModItems.COIN_NETHERITE, Tags.Items.INGOTS_NETHERITE, Items.NETHERITE_INGOT);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UPGRADE_SMITHING_TEMPLATE.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("nnn").pattern("ncn").pattern("nnn").define('c', ModItems.TRADING_CORE.get()).define('n', Tags.Items.NUGGETS_IRON).save(consumer, ID("upgrades/create_template"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UPGRADE_SMITHING_TEMPLATE.get(), 2).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("nnn").pattern("ntn").pattern("nnn").define('t', ModItems.UPGRADE_SMITHING_TEMPLATE.get()).define('n', Tags.Items.NUGGETS_IRON).save(consumer, ID("upgrades/copy_template"));
        Ingredient TEMPLATE = Ingredient.of(ModItems.UPGRADE_SMITHING_TEMPLATE.get());
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(Tags.Items.CHESTS_WOODEN), Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC, ModItems.ITEM_CAPACITY_UPGRADE_1.get()).unlocks("money", MoneyKnowledge()).unlocks("trader", TraderKnowledge()).save(consumer, ItemID("upgrades/", ModItems.ITEM_CAPACITY_UPGRADE_1));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.ITEM_CAPACITY_UPGRADE_1.get()), Ingredient.of(Tags.Items.INGOTS_GOLD), RecipeCategory.MISC, ModItems.ITEM_CAPACITY_UPGRADE_2.get()).unlocks("money", MoneyKnowledge()).unlocks("trader", TraderKnowledge()).unlocks("previous", LazyTrigger(ModItems.ITEM_CAPACITY_UPGRADE_1)).save(consumer, ItemID("upgrades/", ModItems.ITEM_CAPACITY_UPGRADE_2));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.ITEM_CAPACITY_UPGRADE_2.get()), Ingredient.of(Tags.Items.GEMS_DIAMOND), RecipeCategory.MISC, ModItems.ITEM_CAPACITY_UPGRADE_3.get()).unlocks("money", MoneyKnowledge()).unlocks("trader", TraderKnowledge()).unlocks("previous", LazyTrigger(ModItems.ITEM_CAPACITY_UPGRADE_2)).save(consumer, ItemID("upgrades/", ModItems.ITEM_CAPACITY_UPGRADE_3));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.ITEM_CAPACITY_UPGRADE_3.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), RecipeCategory.MISC, ModItems.ITEM_CAPACITY_UPGRADE_4.get()).unlocks("money", MoneyKnowledge()).unlocks("trader", TraderKnowledge()).unlocks("previous", LazyTrigger(ModItems.ITEM_CAPACITY_UPGRADE_3)).save(consumer, ItemID("upgrades/", ModItems.ITEM_CAPACITY_UPGRADE_4));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(Items.ENDER_EYE), Ingredient.of(Tags.Items.INGOTS_GOLD), RecipeCategory.MISC, ModItems.NETWORK_UPGRADE.get()).unlocks("money", MoneyKnowledge()).unlocks("trader", TraderKnowledge()).unlocks("terminal", TerminalKnowledge()).save(consumer, ItemID("upgrades/", ModItems.NETWORK_UPGRADE));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(Items.HOPPER), Ingredient.of(Tags.Items.INGOTS_GOLD), RecipeCategory.MISC, ModItems.HOPPER_UPGRADE.get()).unlocks("money", MoneyKnowledge()).unlocks("trader_interface", LazyTrigger(LCTags.Items.TRADER_INTERFACE)).save(consumer, ItemID("upgrades/", ModItems.HOPPER_UPGRADE));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(Items.CLOCK), Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC, ModItems.SPEED_UPGRADE_1.get()).unlocks("money", MoneyKnowledge()).unlocks("trader_interface", LazyTrigger(LCTags.Items.TRADER_INTERFACE)).unlocks("coin_mint", LazyTrigger(ModBlocks.COIN_MINT)).save(consumer, ItemID("upgrades/", ModItems.SPEED_UPGRADE_1));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.SPEED_UPGRADE_1.get()), Ingredient.of(Tags.Items.INGOTS_GOLD), RecipeCategory.MISC, ModItems.SPEED_UPGRADE_2.get()).unlocks("money", MoneyKnowledge()).unlocks("trader_interface", LazyTrigger(LCTags.Items.TRADER_INTERFACE)).unlocks("coin_mint", LazyTrigger(ModBlocks.COIN_MINT)).unlocks("previous", LazyTrigger(ModItems.SPEED_UPGRADE_1)).save(consumer, ItemID("upgrades/", ModItems.SPEED_UPGRADE_2));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.SPEED_UPGRADE_2.get()), Ingredient.of(Tags.Items.GEMS_EMERALD), RecipeCategory.MISC, ModItems.SPEED_UPGRADE_3.get()).unlocks("money", MoneyKnowledge()).unlocks("trader_interface", LazyTrigger(LCTags.Items.TRADER_INTERFACE)).unlocks("coin_mint", LazyTrigger(ModBlocks.COIN_MINT)).unlocks("previous", LazyTrigger(ModItems.SPEED_UPGRADE_2)).save(consumer, ItemID("upgrades/", ModItems.SPEED_UPGRADE_3));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.SPEED_UPGRADE_3.get()), Ingredient.of(Tags.Items.GEMS_DIAMOND), RecipeCategory.MISC, ModItems.SPEED_UPGRADE_4.get()).unlocks("money", MoneyKnowledge()).unlocks("trader_interface", LazyTrigger(LCTags.Items.TRADER_INTERFACE)).unlocks("coin_mint", LazyTrigger(ModBlocks.COIN_MINT)).unlocks("previous", LazyTrigger(ModItems.SPEED_UPGRADE_3)).save(consumer, ItemID("upgrades/", ModItems.SPEED_UPGRADE_4));
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.SPEED_UPGRADE_4.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), RecipeCategory.MISC, ModItems.SPEED_UPGRADE_5.get()).unlocks("money", MoneyKnowledge()).unlocks("trader_interface", LazyTrigger(LCTags.Items.TRADER_INTERFACE)).unlocks("coin_mint", LazyTrigger(ModBlocks.COIN_MINT)).unlocks("previous", LazyTrigger(ModItems.SPEED_UPGRADE_4)).save(consumer, ItemID("upgrades/", ModItems.SPEED_UPGRADE_5));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.CoinChestUpgradeExchange.INSTANCE);
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModBlocks.ATM.get()), Ingredient.of(Tags.Items.DUSTS_REDSTONE), RecipeCategory.MISC, ModItems.COIN_CHEST_EXCHANGE_UPGRADE.get()).unlocks("money", MoneyKnowledge()).unlocks("coin_chest", LazyTrigger(ModBlocks.COIN_CHEST)).save(conditional::addRecipe, "null:null");
        conditional.generateAdvancement(ItemID("upgrades/", ModItems.COIN_CHEST_EXCHANGE_UPGRADE).withPrefix("recipes/misc/")).build(consumer, ItemID("upgrades/", ModItems.COIN_CHEST_EXCHANGE_UPGRADE));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.CoinChestUpgradeMagnet.INSTANCE);
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.INGOTS_COPPER), RecipeCategory.MISC, ModItems.COIN_CHEST_MAGNET_UPGRADE_1.get()).unlocks("money", MoneyKnowledge()).unlocks("coin_chest", LazyTrigger(ModBlocks.COIN_CHEST)).save(conditional::addRecipe, "null:null");
        conditional.generateAdvancement(ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_1).withPrefix("recipes/misc/")).build(consumer, ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_1));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.CoinChestUpgradeMagnet.INSTANCE);
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.COIN_CHEST_MAGNET_UPGRADE_1.get()), Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC, ModItems.COIN_CHEST_MAGNET_UPGRADE_2.get()).unlocks("money", MoneyKnowledge()).unlocks("coin_chest", LazyTrigger(ModBlocks.COIN_CHEST)).unlocks("previous", LazyTrigger(ModItems.COIN_CHEST_MAGNET_UPGRADE_1)).save(conditional::addRecipe, "null:null");
        conditional.generateAdvancement(ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_2).withPrefix("recipes/misc/")).build(consumer, ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_2));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.CoinChestUpgradeMagnet.INSTANCE);
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.COIN_CHEST_MAGNET_UPGRADE_2.get()), Ingredient.of(Tags.Items.INGOTS_GOLD), RecipeCategory.MISC, ModItems.COIN_CHEST_MAGNET_UPGRADE_3.get()).unlocks("money", MoneyKnowledge()).unlocks("coin_chest", LazyTrigger(ModBlocks.COIN_CHEST)).unlocks("previous", LazyTrigger(ModItems.COIN_CHEST_MAGNET_UPGRADE_2)).save(conditional::addRecipe, "null:null");
        conditional.generateAdvancement(ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_3).withPrefix("recipes/misc/")).build(consumer, ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_3));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.CoinChestUpgradeMagnet.INSTANCE);
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(ModItems.COIN_CHEST_MAGNET_UPGRADE_3.get()), Ingredient.of(Tags.Items.GEMS_EMERALD), RecipeCategory.MISC, ModItems.COIN_CHEST_MAGNET_UPGRADE_4.get()).unlocks("money", MoneyKnowledge()).unlocks("coin_chest", LazyTrigger(ModBlocks.COIN_CHEST)).unlocks("previous", LazyTrigger(ModItems.COIN_CHEST_MAGNET_UPGRADE_3)).save(conditional::addRecipe, "null:null");
        conditional.generateAdvancement(ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_4).withPrefix("recipes/misc/")).build(consumer, ItemID("upgrades/", ModItems.COIN_CHEST_MAGNET_UPGRADE_4));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.CoinChestUpgradeSecurity.INSTANCE);
        SmithingTransformRecipeBuilder.smithing(TEMPLATE, Ingredient.of(Items.OBSIDIAN), Ingredient.of(Tags.Items.GEMS_DIAMOND), RecipeCategory.MISC, ModItems.COIN_CHEST_SECURITY_UPGRADE.get()).unlocks("money", MoneyKnowledge()).unlocks("coin_chest", LazyTrigger(ModBlocks.COIN_CHEST)).save(conditional::addRecipe, "null:null");
        conditional.generateAdvancement(ItemID("upgrades/", ModItems.COIN_CHEST_SECURITY_UPGRADE).withPrefix("recipes/misc/")).build(consumer, ItemID("upgrades/", ModItems.COIN_CHEST_SECURITY_UPGRADE));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.SUS_JAR.get()).requires(LCTags.Items.COIN_JAR_NORMAL).requires(Items.SUSPICIOUS_STEW).unlockedBy("money", MoneyKnowledge()).unlockedBy("jar", LazyTrigger(LCTags.Items.COIN_JAR_ALL)).save(consumer, ItemID("coin_jar/", ModBlocks.SUS_JAR));
        conditional = ConditionalRecipe.builder().addCondition(LCCraftingConditions.TaxCollector.INSTANCE);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TAX_COLLECTOR.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("trader", TraderKnowledge()).pattern("ghg").pattern("nxn").pattern("geg").define('g', Tags.Items.INGOTS_GOLD).define('n', Tags.Items.INGOTS_NETHERITE).define('x', ModItems.TRADING_CORE.get()).define('h', Items.HOPPER).define('e', Tags.Items.ENDER_PEARLS).m_176498_(conditional::addRecipe);
        conditional.generateAdvancement(ItemID(ModBlocks.TAX_COLLECTOR).withPrefix("recipes/misc/")).build(consumer, ItemID(ModBlocks.TAX_COLLECTOR));
        MasterTicketRecipeBuilder.of(LCTags.Items.TICKET_MATERIAL_PAPER).withResult(ModItems.TICKET_MASTER).unlockedBy("ticket_station", LazyTrigger(ModBlocks.TICKET_STATION)).unlockedBy("tickets", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).save(consumer, ItemID("ticket_station/", ModItems.TICKET_MASTER));
        TicketRecipeBuilder.of(LCTags.Items.TICKET_MATERIAL_PAPER, ModItems.TICKET.get()).withMasterTicket(ModItems.TICKET_MASTER.get()).unlockedBy("ticket_station", LazyTrigger(ModBlocks.TICKET_STATION)).unlockedBy("tickets", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).save(consumer, ItemID("ticket_station/", ModItems.TICKET));
        TicketRecipeBuilder.of(LCTags.Items.TICKET_MATERIAL_PAPER, ModItems.TICKET_PASS.get()).withMasterTicket(ModItems.TICKET_MASTER.get()).unlockedBy("ticket_station", LazyTrigger(ModBlocks.TICKET_STATION)).unlockedBy("tickets", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).save(consumer, ItemID("ticket_station/", ModItems.TICKET_PASS));
        MasterTicketRecipeBuilder.of(LCTags.Items.TICKET_MATERIAL_GOLD).withResult(ModItems.GOLDEN_TICKET_MASTER).unlockedBy("ticket_station", LazyTrigger(ModBlocks.TICKET_STATION)).unlockedBy("tickets", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).save(consumer, ItemID("ticket_station/", ModItems.GOLDEN_TICKET_MASTER));
        TicketRecipeBuilder.of(LCTags.Items.TICKET_MATERIAL_GOLD, ModItems.GOLDEN_TICKET.get()).withMasterTicket(ModItems.GOLDEN_TICKET_MASTER.get()).unlockedBy("ticket_station", LazyTrigger(ModBlocks.TICKET_STATION)).unlockedBy("tickets", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).save(consumer, ItemID("ticket_station/", ModItems.GOLDEN_TICKET));
        TicketRecipeBuilder.of(LCTags.Items.TICKET_MATERIAL_GOLD, ModItems.GOLDEN_TICKET_PASS.get()).withMasterTicket(ModItems.GOLDEN_TICKET_MASTER.get()).unlockedBy("ticket_station", LazyTrigger(ModBlocks.TICKET_STATION)).unlockedBy("tickets", LazyTrigger(LCTags.Items.TICKETS)).unlockedBy("ticket_material", LazyTrigger(LCTags.Items.TICKET_MATERIAL)).save(consumer, ItemID("ticket_station/", ModItems.GOLDEN_TICKET_PASS));
    }

    private static void GenerateWalletRecipes(@Nonnull Consumer<FinishedRecipe> consumer, List<Pair<Ingredient, RegistryObject<? extends ItemLike>>> ingredientWalletPairs) {
        Ingredient leather = Ingredient.of(Tags.Items.LEATHER);
        List<Ingredient> ingredients = ingredientWalletPairs.stream().map(Pair::getFirst).toList();
        List<? extends ItemLike> wallets = ingredientWalletPairs.stream().map(p -> (ItemLike) ((RegistryObject) p.getSecond()).get()).toList();
        for (int w = 0; w < wallets.size(); w++) {
            ItemLike wallet = (ItemLike) wallets.get(w);
            ShapelessRecipeBuilder b = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) wallets.get(w)).group("wallet_crafting").unlockedBy("coin", MoneyKnowledge()).unlockedBy("wallet", LazyTrigger(LCTags.Items.WALLET)).requires(leather);
            for (int i = 0; i < ingredients.size() && i <= w; i++) {
                b.requires((Ingredient) ingredients.get(i));
            }
            b.requires(leather);
            b.save(consumer, ID("wallet/" + ItemPath(wallet)));
        }
        for (int w = 0; w < wallets.size() - 1; w++) {
            for (int w2 = w + 1; w2 < wallets.size(); w2++) {
                ItemLike first = (ItemLike) wallets.get(w);
                ItemLike result = (ItemLike) wallets.get(w2);
                WalletUpgradeRecipeBuilder b = WalletUpgradeRecipeBuilder.shapeless(RecipeCategory.MISC, result).group("wallet_upgrading").unlockedBy("coin", MoneyKnowledge()).unlockedBy("wallet", LazyTrigger(LCTags.Items.WALLET)).requires(first);
                for (int i = w + 1; i < ingredients.size() && i <= w2; i++) {
                    b.requires((Ingredient) ingredients.get(i));
                }
                b.save(consumer, ID("wallet/upgrade_" + ItemPath(first) + "_to_" + ItemPath(result)));
            }
        }
    }

    private static void GenerateSwapRecipes(@Nonnull Consumer<FinishedRecipe> consumer, ItemLike item1, ItemLike item2, List<Pair<String, CriterionTriggerInstance>> criteria) {
        String group = ItemPath(item2) + "_swap";
        GenerateSwapRecipe(consumer, item1, item2, group, criteria);
        GenerateSwapRecipe(consumer, item2, item1, group, criteria);
    }

    private static void GenerateSwapRecipe(@Nonnull Consumer<FinishedRecipe> consumer, ItemLike item1, ItemLike item2, @Nullable String group, List<Pair<String, CriterionTriggerInstance>> criteria) {
        ShapelessRecipeBuilder b = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item2).group(group);
        ApplyCriteria(b, criteria);
        b.unlockedBy("other", LazyTrigger(item1)).requires(item1).save(consumer, ID(ItemPath(item1) + "_swap"));
    }

    private static void GenerateCoinBlockRecipes(@Nonnull Consumer<FinishedRecipe> consumer, RegistryObject<? extends ItemLike> coin, RegistryObject<? extends ItemLike> coinPile, RegistryObject<? extends ItemLike> coinBlock) {
        GenerateCoinBlockRecipes(consumer, coin, coinPile, coinBlock, "", MoneyKnowledge());
    }

    private static void GenerateCoinBlockRecipes(@Nonnull Consumer<FinishedRecipe> consumer, RegistryObject<? extends ItemLike> coin, RegistryObject<? extends ItemLike> coinPile, RegistryObject<? extends ItemLike> coinBlock, @Nonnull String prefix, @Nonnull CriterionTriggerInstance moneyKnowledge) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, coinPile.get()).group(prefix + "coin_pile_from_coin").unlockedBy("money", moneyKnowledge).unlockedBy("coin", LazyTrigger(coin)).requires(coin.get(), 9).save(consumer, ID("coins/" + ItemPath(coinPile) + "_from_coin"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, coinBlock.get()).group(prefix + "coin_block_from_pile").unlockedBy("money", moneyKnowledge).unlockedBy("pile", LazyTrigger(coinPile)).pattern("xx").pattern("xx").define('x', coinPile.get()).save(consumer, ID("coins/" + ItemPath(coinBlock) + "_from_pile"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, coinPile.get(), 4).group(prefix + "coin_pile_from_block").unlockedBy("money", moneyKnowledge).unlockedBy("block", LazyTrigger(coinBlock)).requires(coinBlock.get()).save(consumer, ID("coins/" + ItemPath(coinPile) + "_from_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, coin.get(), 9).group(prefix + "coin_from_pile").unlockedBy("money", moneyKnowledge).unlockedBy("pile", LazyTrigger(coinPile)).requires(coinPile.get()).save(consumer, ID("coins/" + ItemPath(coin) + "_from_pile"));
    }

    private static void GenerateColoredDyeAndWashRecipes(@Nonnull Consumer<FinishedRecipe> consumer, RegistryObjectBundle<? extends ItemLike, Color> bundle, ItemLike cleanItem, @Nullable String dyeGroup, String prefix, List<Pair<String, CriterionTriggerInstance>> criteria) {
        List<ItemLike> coloredSet = new ArrayList();
        for (Color color : Color.values()) {
            ItemLike result = bundle.get(color);
            if (result.asItem() != cleanItem.asItem()) {
                coloredSet.add(result);
                ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result).group(dyeGroup).unlockedBy("cleanitem", LazyTrigger(cleanItem)).requires(cleanItem).requires(color.dyeTag);
                ApplyCriteria(builder, criteria);
                builder.save(consumer, ID(prefix + "dye_" + color.getResourceSafeName()));
            }
        }
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, cleanItem);
        ApplyCriteria(builder, criteria);
        builder.unlockedBy("colored", LazyTrigger(coloredSet)).requires(Ingredient.of(coloredSet.stream().map(ItemStack::new))).requires(Items.WATER_BUCKET).save(consumer, ID(prefix + "washing"));
    }

    private static void GenerateMintAndMeltRecipes(@Nonnull Consumer<FinishedRecipe> consumer, RegistryObject<? extends ItemLike> coin, TagKey<Item> materialTag, ItemLike materialItem) {
        MintRecipeBuilder.mint(coin.get()).unlockedBy("money", MoneyKnowledge()).unlockedBy("coin_mint", LazyTrigger(ModBlocks.COIN_MINT)).accepts(materialTag).save(consumer, ItemID("coin_mint/mint_", coin));
        MintRecipeBuilder.melt(materialItem).unlockedBy("money", MoneyKnowledge()).unlockedBy("coin_mint", LazyTrigger(ModBlocks.COIN_MINT)).accepts(coin.get()).save(consumer, ItemID("coin_mint/melt_", coin));
    }

    private static CriterionTriggerInstance MoneyKnowledge() {
        return LazyTrigger(LCTags.Items.COINS);
    }

    private static CriterionTriggerInstance TraderKnowledge() {
        return LazyTrigger(LCTags.Items.TRADER);
    }

    private static CriterionTriggerInstance TerminalKnowledge() {
        return LazyTrigger(LCTags.Items.NETWORK_TERMINAL);
    }

    private static CriterionTriggerInstance LazyTrigger(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build());
    }

    private static CriterionTriggerInstance LazyTrigger(List<? extends ItemLike> items) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of((ItemLike[]) items.toArray(new ItemLike[0])).build());
    }

    private static CriterionTriggerInstance LazyTrigger(RegistryObject<? extends ItemLike> item) {
        return LazyTrigger(item.get());
    }

    private static CriterionTriggerInstance LazyTrigger(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static void ApplyCriteria(ShapelessRecipeBuilder builder, List<Pair<String, CriterionTriggerInstance>> criteria) {
        for (Pair<String, CriterionTriggerInstance> c : criteria) {
            builder.unlockedBy((String) c.getFirst(), (CriterionTriggerInstance) c.getSecond());
        }
    }

    private static String ItemPath(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }

    private static String ItemPath(RegistryObject<? extends ItemLike> item) {
        return ItemPath(item.get());
    }

    private static ResourceLocation ItemID(String prefix, ItemLike item) {
        return ID(prefix + ItemPath(item));
    }

    private static ResourceLocation ItemID(RegistryObject<? extends ItemLike> item) {
        return ID(ItemPath(item));
    }

    private static ResourceLocation ItemID(String prefix, RegistryObject<? extends ItemLike> item) {
        return ID(prefix + ItemPath(item));
    }

    private static ResourceLocation WoodID(String prefix, WoodType woodType) {
        return ID(woodType.generateResourceLocation(prefix));
    }

    private static ResourceLocation WoodID(String prefix, WoodType woodType, String postfix) {
        return ID(woodType.generateResourceLocation(prefix, postfix));
    }

    private static ResourceLocation ColoredWoodID(String prefix, WoodType woodType, Color color) {
        return WoodID(prefix, woodType, "/" + color.getResourceSafeName());
    }

    private static ResourceLocation ID(String path) {
        return new ResourceLocation("lightmanscurrency", path);
    }
}