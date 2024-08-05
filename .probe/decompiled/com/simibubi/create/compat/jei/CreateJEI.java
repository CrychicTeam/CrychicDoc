package com.simibubi.create.compat.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.compat.jei.category.BlockCuttingCategory;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.CrushingCategory;
import com.simibubi.create.compat.jei.category.DeployingCategory;
import com.simibubi.create.compat.jei.category.FanBlastingCategory;
import com.simibubi.create.compat.jei.category.FanHauntingCategory;
import com.simibubi.create.compat.jei.category.FanSmokingCategory;
import com.simibubi.create.compat.jei.category.FanWashingCategory;
import com.simibubi.create.compat.jei.category.ItemApplicationCategory;
import com.simibubi.create.compat.jei.category.ItemDrainCategory;
import com.simibubi.create.compat.jei.category.MechanicalCraftingCategory;
import com.simibubi.create.compat.jei.category.MillingCategory;
import com.simibubi.create.compat.jei.category.MixingCategory;
import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;
import com.simibubi.create.compat.jei.category.PackingCategory;
import com.simibubi.create.compat.jei.category.PolishingCategory;
import com.simibubi.create.compat.jei.category.PressingCategory;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.SawingCategory;
import com.simibubi.create.compat.jei.category.SequencedAssemblyCategory;
import com.simibubi.create.compat.jei.category.SpoutCategory;
import com.simibubi.create.content.equipment.blueprint.BlueprintScreen;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerScreen;
import com.simibubi.create.content.trains.schedule.ScheduleScreen;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.data.recipe.LogStrippingFakeRecipes;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.ModList;

@ParametersAreNonnullByDefault
@JeiPlugin
public class CreateJEI implements IModPlugin {

    private static final ResourceLocation ID = Create.asResource("jei_plugin");

    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList();

    private IIngredientManager ingredientManager;

    private void loadCategories() {
        this.allCategories.clear();
        CreateRecipeCategory<?> milling = this.builder(AbstractCrushingRecipe.class).addTypedRecipes(AllRecipeTypes.MILLING).catalyst(AllBlocks.MILLSTONE::get).doubleItemIcon((ItemLike) AllBlocks.MILLSTONE.get(), (ItemLike) AllItems.WHEAT_FLOUR.get()).emptyBackground(177, 53).build("milling", MillingCategory::new);
        CreateRecipeCategory<?> crushing = this.builder(AbstractCrushingRecipe.class).addTypedRecipes(AllRecipeTypes.CRUSHING).addTypedRecipesExcluding(AllRecipeTypes.MILLING::getType, AllRecipeTypes.CRUSHING::getType).catalyst(AllBlocks.CRUSHING_WHEEL::get).doubleItemIcon((ItemLike) AllBlocks.CRUSHING_WHEEL.get(), (ItemLike) AllItems.CRUSHED_GOLD.get()).emptyBackground(177, 100).build("crushing", CrushingCategory::new);
        CreateRecipeCategory<?> pressing = this.builder(PressingRecipe.class).addTypedRecipes(AllRecipeTypes.PRESSING).catalyst(AllBlocks.MECHANICAL_PRESS::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_PRESS.get(), (ItemLike) AllItems.IRON_SHEET.get()).emptyBackground(177, 70).build("pressing", PressingCategory::new);
        CreateRecipeCategory<?> washing = this.builder(SplashingRecipe.class).addTypedRecipes(AllRecipeTypes.SPLASHING).catalystStack(ProcessingViaFanCategory.getFan("fan_washing")).doubleItemIcon((ItemLike) AllItems.PROPELLER.get(), Items.WATER_BUCKET).emptyBackground(178, 72).build("fan_washing", FanWashingCategory::new);
        CreateRecipeCategory<?> smoking = this.builder(SmokingRecipe.class).addTypedRecipes(() -> RecipeType.SMOKING).catalystStack(ProcessingViaFanCategory.getFan("fan_smoking")).doubleItemIcon((ItemLike) AllItems.PROPELLER.get(), Items.CAMPFIRE).emptyBackground(178, 72).build("fan_smoking", FanSmokingCategory::new);
        CreateRecipeCategory<?> blasting = this.builder(AbstractCookingRecipe.class).addTypedRecipesExcluding(() -> RecipeType.SMELTING, () -> RecipeType.BLASTING).addTypedRecipes(() -> RecipeType.BLASTING).removeRecipes(() -> RecipeType.SMOKING).catalystStack(ProcessingViaFanCategory.getFan("fan_blasting")).doubleItemIcon((ItemLike) AllItems.PROPELLER.get(), Items.LAVA_BUCKET).emptyBackground(178, 72).build("fan_blasting", FanBlastingCategory::new);
        CreateRecipeCategory<?> haunting = this.builder(HauntingRecipe.class).addTypedRecipes(AllRecipeTypes.HAUNTING).catalystStack(ProcessingViaFanCategory.getFan("fan_haunting")).doubleItemIcon((ItemLike) AllItems.PROPELLER.get(), Items.SOUL_CAMPFIRE).emptyBackground(178, 72).build("fan_haunting", FanHauntingCategory::new);
        CreateRecipeCategory<?> mixing = this.builder(BasinRecipe.class).addTypedRecipes(AllRecipeTypes.MIXING).catalyst(AllBlocks.MECHANICAL_MIXER::get).catalyst(AllBlocks.BASIN::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_MIXER.get(), (ItemLike) AllBlocks.BASIN.get()).emptyBackground(177, 103).build("mixing", MixingCategory::standard);
        CreateRecipeCategory<?> autoShapeless = this.builder(BasinRecipe.class).enableWhen(c -> c.allowShapelessInMixer).addAllRecipesIf(r -> r instanceof CraftingRecipe && !(r instanceof IShapedRecipe) && r.getIngredients().size() > 1 && !MechanicalPressBlockEntity.canCompress(r) && !AllRecipeTypes.shouldIgnoreInAutomation(r), BasinRecipe::convertShapeless).catalyst(AllBlocks.MECHANICAL_MIXER::get).catalyst(AllBlocks.BASIN::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_MIXER.get(), Items.CRAFTING_TABLE).emptyBackground(177, 85).build("automatic_shapeless", MixingCategory::autoShapeless);
        CreateRecipeCategory<?> brewing = this.builder(BasinRecipe.class).enableWhen(c -> c.allowBrewingInMixer).addRecipes(() -> PotionMixingRecipes.ALL).catalyst(AllBlocks.MECHANICAL_MIXER::get).catalyst(AllBlocks.BASIN::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_MIXER.get(), Blocks.BREWING_STAND).emptyBackground(177, 103).build("automatic_brewing", MixingCategory::autoBrewing);
        CreateRecipeCategory<?> packing = this.builder(BasinRecipe.class).addTypedRecipes(AllRecipeTypes.COMPACTING).catalyst(AllBlocks.MECHANICAL_PRESS::get).catalyst(AllBlocks.BASIN::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_PRESS.get(), (ItemLike) AllBlocks.BASIN.get()).emptyBackground(177, 103).build("packing", PackingCategory::standard);
        CreateRecipeCategory<?> autoSquare = this.builder(BasinRecipe.class).enableWhen(c -> c.allowShapedSquareInPress).addAllRecipesIf(r -> r instanceof CraftingRecipe && !(r instanceof MechanicalCraftingRecipe) && MechanicalPressBlockEntity.canCompress(r) && !AllRecipeTypes.shouldIgnoreInAutomation(r), BasinRecipe::convertShapeless).catalyst(AllBlocks.MECHANICAL_PRESS::get).catalyst(AllBlocks.BASIN::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_PRESS.get(), Blocks.CRAFTING_TABLE).emptyBackground(177, 85).build("automatic_packing", PackingCategory::autoSquare);
        CreateRecipeCategory<?> sawing = this.builder(CuttingRecipe.class).addTypedRecipes(AllRecipeTypes.CUTTING).catalyst(AllBlocks.MECHANICAL_SAW::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_SAW.get(), Items.OAK_LOG).emptyBackground(177, 70).build("sawing", SawingCategory::new);
        CreateRecipeCategory<?> blockCutting = this.builder(BlockCuttingCategory.CondensedBlockCuttingRecipe.class).enableWhen(c -> c.allowStonecuttingOnSaw).addRecipes(() -> BlockCuttingCategory.condenseRecipes(getTypedRecipesExcluding(RecipeType.STONECUTTING, AllRecipeTypes::shouldIgnoreInAutomation))).catalyst(AllBlocks.MECHANICAL_SAW::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_SAW.get(), Items.STONE_BRICK_STAIRS).emptyBackground(177, 70).build("block_cutting", BlockCuttingCategory::new);
        CreateRecipeCategory<?> woodCutting = this.builder(BlockCuttingCategory.CondensedBlockCuttingRecipe.class).enableIf(c -> c.allowWoodcuttingOnSaw.get() && ModList.get().isLoaded("druidcraft")).addRecipes(() -> BlockCuttingCategory.condenseRecipes(getTypedRecipesExcluding((RecipeType<?>) SawBlockEntity.woodcuttingRecipeType.get(), AllRecipeTypes::shouldIgnoreInAutomation))).catalyst(AllBlocks.MECHANICAL_SAW::get).doubleItemIcon((ItemLike) AllBlocks.MECHANICAL_SAW.get(), Items.OAK_STAIRS).emptyBackground(177, 70).build("wood_cutting", BlockCuttingCategory::new);
        CreateRecipeCategory<?> polishing = this.builder(SandPaperPolishingRecipe.class).addTypedRecipes(AllRecipeTypes.SANDPAPER_POLISHING).catalyst(AllItems.SAND_PAPER::get).catalyst(AllItems.RED_SAND_PAPER::get).itemIcon((ItemLike) AllItems.SAND_PAPER.get()).emptyBackground(177, 55).build("sandpaper_polishing", PolishingCategory::new);
        CreateRecipeCategory<?> item_application = this.builder(ItemApplicationRecipe.class).addTypedRecipes(AllRecipeTypes.ITEM_APPLICATION).addRecipes(LogStrippingFakeRecipes::createRecipes).itemIcon((ItemLike) AllItems.BRASS_HAND.get()).emptyBackground(177, 60).build("item_application", ItemApplicationCategory::new);
        CreateRecipeCategory<?> deploying = this.builder(DeployerApplicationRecipe.class).addTypedRecipes(AllRecipeTypes.DEPLOYING).addTypedRecipes(AllRecipeTypes.SANDPAPER_POLISHING::getType, DeployerApplicationRecipe::convert).addTypedRecipes(AllRecipeTypes.ITEM_APPLICATION::getType, ManualApplicationRecipe::asDeploying).catalyst(AllBlocks.DEPLOYER::get).catalyst(AllBlocks.DEPOT::get).catalyst(AllItems.BELT_CONNECTOR::get).itemIcon((ItemLike) AllBlocks.DEPLOYER.get()).emptyBackground(177, 70).build("deploying", DeployingCategory::new);
        CreateRecipeCategory<?> spoutFilling = this.builder(FillingRecipe.class).addTypedRecipes(AllRecipeTypes.FILLING).addRecipeListConsumer(recipes -> SpoutCategory.consumeRecipes(recipes::add, this.ingredientManager)).catalyst(AllBlocks.SPOUT::get).doubleItemIcon((ItemLike) AllBlocks.SPOUT.get(), Items.WATER_BUCKET).emptyBackground(177, 70).build("spout_filling", SpoutCategory::new);
        CreateRecipeCategory<?> draining = this.builder(EmptyingRecipe.class).addRecipeListConsumer(recipes -> ItemDrainCategory.consumeRecipes(recipes::add, this.ingredientManager)).addTypedRecipes(AllRecipeTypes.EMPTYING).catalyst(AllBlocks.ITEM_DRAIN::get).doubleItemIcon((ItemLike) AllBlocks.ITEM_DRAIN.get(), Items.WATER_BUCKET).emptyBackground(177, 50).build("draining", ItemDrainCategory::new);
        CreateRecipeCategory<?> autoShaped = this.builder(CraftingRecipe.class).enableWhen(c -> c.allowRegularCraftingInCrafter).addAllRecipesIf(r -> r instanceof CraftingRecipe && !(r instanceof IShapedRecipe) && r.getIngredients().size() == 1 && !AllRecipeTypes.shouldIgnoreInAutomation(r)).addTypedRecipesIf(() -> RecipeType.CRAFTING, recipe -> recipe instanceof IShapedRecipe && !AllRecipeTypes.shouldIgnoreInAutomation(recipe)).catalyst(AllBlocks.MECHANICAL_CRAFTER::get).itemIcon((ItemLike) AllBlocks.MECHANICAL_CRAFTER.get()).emptyBackground(177, 107).build("automatic_shaped", MechanicalCraftingCategory::new);
        CreateRecipeCategory<?> mechanicalCrafting = this.builder(CraftingRecipe.class).addTypedRecipes(AllRecipeTypes.MECHANICAL_CRAFTING).catalyst(AllBlocks.MECHANICAL_CRAFTER::get).itemIcon((ItemLike) AllBlocks.MECHANICAL_CRAFTER.get()).emptyBackground(177, 107).build("mechanical_crafting", MechanicalCraftingCategory::new);
        CreateRecipeCategory<?> seqAssembly = this.builder(SequencedAssemblyRecipe.class).addTypedRecipes(AllRecipeTypes.SEQUENCED_ASSEMBLY).itemIcon((ItemLike) AllItems.PRECISION_MECHANISM.get()).emptyBackground(180, 115).build("sequenced_assembly", SequencedAssemblyCategory::new);
        CreateRecipeCategory<?> mysteryConversion = this.builder(ConversionRecipe.class).addRecipes(() -> MysteriousItemConversionCategory.RECIPES).itemIcon((ItemLike) AllBlocks.PECULIAR_BELL.get()).emptyBackground(177, 50).build("mystery_conversion", MysteriousItemConversionCategory::new);
    }

    private <T extends Recipe<?>> CreateJEI.CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new CreateJEI.CategoryBuilder<>(recipeClass);
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        this.loadCategories();
        registration.addRecipeCategories((IRecipeCategory<?>[]) this.allCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        this.ingredientManager = registration.getIngredientManager();
        this.allCategories.forEach(c -> c.registerRecipes(registration));
        registration.addRecipes(RecipeTypes.CRAFTING, ToolboxColoringRecipeMaker.createRecipes().toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        this.allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BlueprintTransferHandler(), RecipeTypes.CRAFTING);
    }

    @Override
    public <T> void registerFluidSubtypes(ISubtypeRegistration registration, IPlatformFluidHelper<T> platformFluidHelper) {
        PotionFluidSubtypeInterpreter interpreter = new PotionFluidSubtypeInterpreter();
        PotionFluid potionFluid = (PotionFluid) AllFluids.POTION.get();
        registration.registerSubtypeInterpreter(ForgeTypes.FLUID_STACK, potionFluid.m_5613_(), interpreter);
        registration.registerSubtypeInterpreter(ForgeTypes.FLUID_STACK, potionFluid.m_5615_(), interpreter);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(AbstractSimiContainerScreen.class, new SlotMover());
        registration.addGhostIngredientHandler(AbstractFilterScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(BlueprintScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(LinkedControllerScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(ScheduleScreen.class, new GhostIngredientHandler());
    }

    public static void consumeAllRecipes(Consumer<Recipe<?>> consumer) {
        Minecraft.getInstance().getConnection().getRecipeManager().getRecipes().forEach(consumer);
    }

    public static <T extends Recipe<?>> void consumeTypedRecipes(Consumer<T> consumer, RecipeType<?> type) {
        Map<ResourceLocation, Recipe<?>> map = (Map<ResourceLocation, Recipe<?>>) Minecraft.getInstance().getConnection().getRecipeManager().recipes.get(type);
        if (map != null) {
            map.values().forEach(recipe -> consumer.accept(recipe));
        }
    }

    public static List<Recipe<?>> getTypedRecipes(RecipeType<?> type) {
        List<Recipe<?>> recipes = new ArrayList();
        consumeTypedRecipes(recipes::add, type);
        return recipes;
    }

    public static List<Recipe<?>> getTypedRecipesExcluding(RecipeType<?> type, Predicate<Recipe<?>> exclusionPred) {
        List<Recipe<?>> recipes = getTypedRecipes(type);
        recipes.removeIf(exclusionPred);
        return recipes;
    }

    public static boolean doInputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        if (!recipe1.getIngredients().isEmpty() && !recipe2.getIngredients().isEmpty()) {
            ItemStack[] matchingStacks = recipe1.getIngredients().get(0).getItems();
            return matchingStacks.length == 0 ? false : recipe2.getIngredients().get(0).test(matchingStacks[0]);
        } else {
            return false;
        }
    }

    public static boolean doOutputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.m_9598_();
        return ItemHelper.sameItem(recipe1.getResultItem(registryAccess), recipe2.getResultItem(registryAccess));
    }

    private class CategoryBuilder<T extends Recipe<?>> {

        private final Class<? extends T> recipeClass;

        private Predicate<CRecipes> predicate = cRecipes -> true;

        private IDrawable background;

        private IDrawable icon;

        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList();

        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList();

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public CreateJEI.CategoryBuilder<T> enableIf(Predicate<CRecipes> predicate) {
            this.predicate = predicate;
            return this;
        }

        public CreateJEI.CategoryBuilder<T> enableWhen(Function<CRecipes, ConfigBase.ConfigBool> configValue) {
            this.predicate = c -> ((ConfigBase.ConfigBool) configValue.apply(c)).get();
            return this;
        }

        public CreateJEI.CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            this.recipeListConsumers.add(consumer);
            return this;
        }

        public CreateJEI.CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
            return this.addRecipeListConsumer(recipes -> recipes.addAll((Collection) collection.get()));
        }

        public CreateJEI.CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred) {
            return this.addRecipeListConsumer(recipes -> CreateJEI.consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(recipe);
                }
            }));
        }

        public CreateJEI.CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred, Function<Recipe<?>, T> converter) {
            return this.addRecipeListConsumer(recipes -> CreateJEI.consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add((Recipe) converter.apply(recipe));
                }
            }));
        }

        public CreateJEI.CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return this.addTypedRecipes(recipeTypeEntry::getType);
        }

        public CreateJEI.CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return this.addRecipeListConsumer(recipes -> CreateJEI.consumeTypedRecipes(recipes::add, (RecipeType<?>) recipeType.get()));
        }

        public CreateJEI.CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType, Function<Recipe<?>, T> converter) {
            return this.addRecipeListConsumer(recipes -> CreateJEI.consumeTypedRecipes(recipe -> recipes.add((Recipe) converter.apply(recipe)), (RecipeType<?>) recipeType.get()));
        }

        public CreateJEI.CategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
            return this.addRecipeListConsumer(recipes -> CreateJEI.consumeTypedRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(recipe);
                }
            }, (RecipeType<?>) recipeType.get()));
        }

        public CreateJEI.CategoryBuilder<T> addTypedRecipesExcluding(Supplier<RecipeType<? extends T>> recipeType, Supplier<RecipeType<? extends T>> excluded) {
            return this.addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes((RecipeType<?>) excluded.get());
                CreateJEI.consumeTypedRecipes(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes) {
                        if (CreateJEI.doInputsMatch(recipe, excludedRecipe)) {
                            return;
                        }
                    }
                    recipes.add(recipe);
                }, (RecipeType<?>) recipeType.get());
            });
        }

        public CreateJEI.CategoryBuilder<T> removeRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return this.addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes((RecipeType<?>) recipeType.get());
                recipes.removeIf(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes) {
                        if (CreateJEI.doInputsMatch(recipe, excludedRecipe) && CreateJEI.doOutputsMatch(recipe, excludedRecipe)) {
                            return true;
                        }
                    }
                    return false;
                });
            });
        }

        public CreateJEI.CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            this.catalysts.add(supplier);
            return this;
        }

        public CreateJEI.CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return this.catalystStack(() -> new ItemStack(((ItemLike) supplier.get()).asItem()));
        }

        public CreateJEI.CategoryBuilder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public CreateJEI.CategoryBuilder<T> itemIcon(ItemLike item) {
            this.icon(new ItemIcon(() -> new ItemStack(item)));
            return this;
        }

        public CreateJEI.CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
            this.icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
            return this;
        }

        public CreateJEI.CategoryBuilder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }

        public CreateJEI.CategoryBuilder<T> emptyBackground(int width, int height) {
            this.background(new EmptyBackground(width, height));
            return this;
        }

        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<T>> recipesSupplier;
            if (this.predicate.test(AllConfigs.server().recipes)) {
                recipesSupplier = () -> {
                    List<T> recipes = new ArrayList();
                    for (Consumer<List<T>> consumer : this.recipeListConsumers) {
                        consumer.accept(recipes);
                    }
                    return recipes;
                };
            } else {
                recipesSupplier = () -> Collections.emptyList();
            }
            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(new mezz.jei.api.recipe.RecipeType<>(Create.asResource(name), this.recipeClass), Lang.translateDirect("recipe." + name), this.background, this.icon, recipesSupplier, this.catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            CreateJEI.this.allCategories.add(category);
            return category;
        }
    }
}