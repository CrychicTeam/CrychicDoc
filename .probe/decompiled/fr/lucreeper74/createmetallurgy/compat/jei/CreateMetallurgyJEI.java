package fr.lucreeper74.createmetallurgy.compat.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.GhostIngredientHandler;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.SlotMover;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.equipment.blueprint.BlueprintScreen;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerScreen;
import com.simibubi.create.content.trains.schedule.ScheduleScreen;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import fr.lucreeper74.createmetallurgy.compat.jei.category.AlloyingCategory;
import fr.lucreeper74.createmetallurgy.compat.jei.category.CastingInBasinCategory;
import fr.lucreeper74.createmetallurgy.compat.jei.category.CastingInTableCategory;
import fr.lucreeper74.createmetallurgy.compat.jei.category.GrindingCategory;
import fr.lucreeper74.createmetallurgy.compat.jei.category.MeltingCategory;
import fr.lucreeper74.createmetallurgy.compat.jei.category.PolishingWithGrinderCategory;
import fr.lucreeper74.createmetallurgy.content.belt_grinder.GrindingRecipe;
import fr.lucreeper74.createmetallurgy.content.casting.recipe.CastingBasinRecipe;
import fr.lucreeper74.createmetallurgy.content.casting.recipe.CastingTableRecipe;
import fr.lucreeper74.createmetallurgy.registries.CMBlocks;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

@JeiPlugin
public class CreateMetallurgyJEI implements IModPlugin {

    private static final ResourceLocation ID = CreateMetallurgy.genRL("jei_plugin");

    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList();

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    private void loadCategories() {
        this.allCategories.clear();
        CreateRecipeCategory<?> melting = this.builder(BasinRecipe.class).addTypedRecipes(CMRecipeTypes.MELTING).catalyst(CMBlocks.FOUNDRY_LID_BLOCK::get).catalyst(CMBlocks.FOUNDRY_BASIN_BLOCK::get).doubleItemIcon((ItemLike) CMBlocks.FOUNDRY_BASIN_BLOCK.get(), (ItemLike) CMBlocks.FOUNDRY_LID_BLOCK.get()).emptyBackground(177, 100).build("melting", MeltingCategory::new);
        CreateRecipeCategory<?> alloying = this.builder(BasinRecipe.class).addTypedRecipes(CMRecipeTypes.ALLOYING).catalyst(CMBlocks.FOUNDRY_MIXER_BLOCK::get).catalyst(CMBlocks.GLASSED_FOUNDRY_LID_BLOCK::get).catalyst(CMBlocks.FOUNDRY_BASIN_BLOCK::get).doubleItemIcon((ItemLike) CMBlocks.FOUNDRY_BASIN_BLOCK.get(), (ItemLike) CMBlocks.FOUNDRY_MIXER_BLOCK.get()).emptyBackground(177, 100).build("alloying", AlloyingCategory::new);
        CreateRecipeCategory<?> casting_in_basin = this.builder(CastingBasinRecipe.class).addTypedRecipes(CMRecipeTypes.CASTING_IN_BASIN).catalyst(AllBlocks.SPOUT::get).catalyst(CMBlocks.CASTING_BASIN_BLOCK::get).doubleItemIcon((ItemLike) CMBlocks.CASTING_BASIN_BLOCK.get(), Items.CLOCK).emptyBackground(177, 53).build("casting_in_basin", CastingInBasinCategory::new);
        CreateRecipeCategory<?> casting_in_table = this.builder(CastingTableRecipe.class).addTypedRecipes(CMRecipeTypes.CASTING_IN_TABLE).catalyst(AllBlocks.SPOUT::get).catalyst(CMBlocks.CASTING_TABLE_BLOCK::get).doubleItemIcon((ItemLike) CMBlocks.CASTING_TABLE_BLOCK.get(), Items.CLOCK).emptyBackground(177, 53).build("casting_in_table", CastingInTableCategory::new);
        CreateRecipeCategory<?> grinding = this.builder(GrindingRecipe.class).addTypedRecipes(CMRecipeTypes.GRINDING).catalyst(CMBlocks.BELT_GRINDER_BLOCK::get).doubleItemIcon((ItemLike) CMBlocks.BELT_GRINDER_BLOCK.get(), Items.IRON_INGOT).emptyBackground(177, 70).build("grinding", GrindingCategory::new);
        CreateRecipeCategory<?> polishing_with_grinder = this.builder(SandPaperPolishingRecipe.class).addTypedRecipes(AllRecipeTypes.SANDPAPER_POLISHING).catalyst(CMBlocks.BELT_GRINDER_BLOCK::get).doubleItemIcon((ItemLike) CMBlocks.BELT_GRINDER_BLOCK.get(), (ItemLike) AllItems.SAND_PAPER.get()).emptyBackground(177, 70).build("polishing_with_grinder", PolishingWithGrinderCategory::new);
    }

    private <T extends Recipe<?>> CreateMetallurgyJEI.CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new CreateMetallurgyJEI.CategoryBuilder<>(recipeClass);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        this.loadCategories();
        registration.addRecipeCategories((IRecipeCategory<?>[]) this.allCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        this.allCategories.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        this.allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(AbstractSimiContainerScreen.class, new SlotMover());
        registration.addGhostIngredientHandler(AbstractFilterScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(BlueprintScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(LinkedControllerScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(ScheduleScreen.class, new GhostIngredientHandler());
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

        public CreateMetallurgyJEI.CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            this.recipeListConsumers.add(consumer);
            return this;
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return this.addTypedRecipes(recipeTypeEntry::getType);
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return this.addRecipeListConsumer(recipes -> CreateJEI.consumeTypedRecipes(recipes::add, (RecipeType<?>) recipeType.get()));
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            this.catalysts.add(supplier);
            return this;
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return this.catalystStack(() -> new ItemStack(((ItemLike) supplier.get()).asItem()));
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> itemIcon(ItemLike item) {
            this.icon(new ItemIcon(() -> new ItemStack(item)));
            return this;
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
            this.icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
            return this;
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }

        public CreateMetallurgyJEI.CategoryBuilder<T> emptyBackground(int width, int height) {
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
            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(new mezz.jei.api.recipe.RecipeType<>(new ResourceLocation("createmetallurgy", name), this.recipeClass), Component.translatable("createmetallurgy.recipe." + name), this.background, this.icon, recipesSupplier, this.catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            CreateMetallurgyJEI.this.allCategories.add(category);
            return category;
        }
    }
}