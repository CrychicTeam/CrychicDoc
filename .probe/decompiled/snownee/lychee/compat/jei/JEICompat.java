package snownee.lychee.compat.jei;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.shedaniel.rei.plugincompatibilities.api.REIPluginCompatIgnore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.ItemLike;
import snownee.lychee.Lychee;
import snownee.lychee.LycheeTags;
import snownee.lychee.RecipeTypes;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.client.gui.RenderElement;
import snownee.lychee.client.gui.ScreenElement;
import snownee.lychee.compat.JEIREI;
import snownee.lychee.compat.jei.category.BaseJEICategory;
import snownee.lychee.compat.jei.category.BlockCrushingRecipeCategory;
import snownee.lychee.compat.jei.category.BlockExplodingRecipeCategory;
import snownee.lychee.compat.jei.category.BlockInteractionRecipeCategory;
import snownee.lychee.compat.jei.category.CraftingRecipeCategoryExtension;
import snownee.lychee.compat.jei.category.DripstoneRecipeCategory;
import snownee.lychee.compat.jei.category.ItemBurningRecipeCategory;
import snownee.lychee.compat.jei.category.ItemExplodingRecipeCategory;
import snownee.lychee.compat.jei.category.ItemInsideRecipeCategory;
import snownee.lychee.compat.jei.category.LightningChannelingRecipeCategory;
import snownee.lychee.compat.jei.ingredient.PostActionIngredientHelper;
import snownee.lychee.compat.jei.ingredient.PostActionIngredientRenderer;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.crafting.ShapedCraftingRecipe;
import snownee.lychee.util.CommonProxy;

@REIPluginCompatIgnore
@JeiPlugin
public class JEICompat implements IModPlugin {

    public static final ResourceLocation UID = new ResourceLocation("lychee", "main");

    public static final IIngredientType<PostAction> POST_ACTION = () -> PostAction.class;

    public static final Map<ResourceLocation, Map<ResourceLocation, BaseJEICategory<?, ?>>> CATEGORIES = Maps.newHashMap();

    public static final List<Consumer<Map<ResourceLocation, Function<JEIREI.CategoryCreationContext, BaseJEICategory<?, ?>>>>> FACTORY_PROVIDERS = Lists.newArrayList();

    private static final Map<AllGuiTextures, IDrawable> elMap = Maps.newIdentityHashMap();

    public static IJeiRuntime RUNTIME;

    public static IJeiHelpers HELPERS;

    public static IGuiHelper GUI;

    public static void addCategoryFactoryProvider(Consumer<Map<ResourceLocation, Function<JEIREI.CategoryCreationContext, BaseJEICategory<?, ?>>>> provider) {
        FACTORY_PROVIDERS.add(provider);
    }

    private static <C extends LycheeContext, T extends LycheeRecipe<C>> void forEachCategories(LycheeRecipeType<C, T> recipeType, Consumer<BaseJEICategory<C, T>> consumer) {
        ((Map) CATEGORIES.getOrDefault(recipeType.categoryId, Map.of())).values().stream().map($ -> $).forEach(consumer);
    }

    public static IDrawable slot(JEICompat.SlotType slotType) {
        return slotType.element;
    }

    public static IDrawable el(AllGuiTextures element) {
        return (IDrawable) elMap.computeIfAbsent(element, JEICompat.ScreenElementWrapper::new);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        HELPERS = registration.getJeiHelpers();
        GUI = HELPERS.getGuiHelper();
        Map<ResourceLocation, Function<JEIREI.CategoryCreationContext, BaseJEICategory<?, ?>>> factories = Maps.newHashMap();
        factories.put(RecipeTypes.ITEM_BURNING.categoryId, (Function) $ -> new ItemBurningRecipeCategory(RecipeTypes.ITEM_BURNING));
        factories.put(RecipeTypes.ITEM_INSIDE.categoryId, (Function) $ -> new ItemInsideRecipeCategory(RecipeTypes.ITEM_INSIDE, AllGuiTextures.JEI_DOWN_ARROW));
        factories.put(RecipeTypes.BLOCK_INTERACTING.categoryId, (Function) $ -> {
            ScreenElement mainIcon = $.recipes().stream().map(LycheeRecipe::getType).anyMatch(Predicate.isEqual(RecipeTypes.BLOCK_INTERACTING)) ? AllGuiTextures.RIGHT_CLICK : AllGuiTextures.LEFT_CLICK;
            return new BlockInteractionRecipeCategory(List.of(RecipeTypes.BLOCK_INTERACTING, RecipeTypes.BLOCK_CLICKING), mainIcon);
        });
        factories.put(RecipeTypes.BLOCK_CRUSHING.categoryId, (Function) $ -> new BlockCrushingRecipeCategory(RecipeTypes.BLOCK_CRUSHING));
        factories.put(RecipeTypes.LIGHTNING_CHANNELING.categoryId, (Function) $ -> new LightningChannelingRecipeCategory(RecipeTypes.LIGHTNING_CHANNELING));
        factories.put(RecipeTypes.ITEM_EXPLODING.categoryId, (Function) $ -> new ItemExplodingRecipeCategory(RecipeTypes.ITEM_EXPLODING));
        factories.put(RecipeTypes.BLOCK_EXPLODING.categoryId, (Function) $ -> new BlockExplodingRecipeCategory(RecipeTypes.BLOCK_EXPLODING, GuiGameElement.of(Items.TNT)));
        factories.put(RecipeTypes.DRIPSTONE_DRIPPING.categoryId, (Function) $ -> new DripstoneRecipeCategory(RecipeTypes.DRIPSTONE_DRIPPING));
        FACTORY_PROVIDERS.forEach($ -> $.accept(factories));
        CATEGORIES.clear();
        JEIREI.registerCategories(factories::containsKey, (categoryId, context) -> {
            BaseJEICategory<?, ?> category = (BaseJEICategory<?, ?>) ((Function) factories.get(categoryId)).apply(context);
            category.recipeType = new RecipeType(JEIREI.composeCategoryIdentifier(categoryId, context.group()), ((LycheeRecipeType) category.recipeTypes.get(0)).clazz);
            category.initialRecipes = context.recipes();
            category.icon = category.createIcon(GUI, context.recipes());
            registration.addRecipeCategories(category);
            ((Map) CATEGORIES.computeIfAbsent(categoryId, $ -> Maps.newHashMap())).put(context.group(), category);
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        CATEGORIES.values().forEach($ -> $.values().forEach($$ -> registration.addRecipes($$.recipeType, $$.initialRecipes)));
        try {
            List<IJeiAnvilRecipe> recipes = CommonProxy.recipes(RecipeTypes.ANVIL_CRAFTING).stream().filter($ -> !$.getResultItem().isEmpty() && !$.m_5598_() && $.showInRecipeViewer()).map($ -> {
                List<ItemStack> right = Stream.of($.getRight().getItems()).map(ItemStack::m_41777_).peek($$ -> $$.setCount($.getMaterialCost())).toList();
                return registration.getVanillaRecipeFactory().createAnvilRecipe(List.of($.getLeft().getItems()), right, List.of($.getResultItem()));
            }).toList();
            registration.addRecipes(mezz.jei.api.constants.RecipeTypes.ANVIL, recipes);
        } catch (Throwable var3) {
            Lychee.LOGGER.error("", var3);
        }
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(ShapedCraftingRecipe.class, CraftingRecipeCategoryExtension::new);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(POST_ACTION, List.of(), new PostActionIngredientHelper(), PostActionIngredientRenderer.INSTANCE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        forEachCategories(RecipeTypes.BLOCK_CRUSHING, $ -> $.initialRecipes.stream().map(BlockKeyRecipe::getBlock).distinct().map(BlockPredicateHelper::getMatchedBlocks).flatMap(Collection::stream).distinct().map(ItemLike::m_5456_).filter(Predicate.not(Items.AIR::equals)).map(Item::m_7968_).forEach($$ -> registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, $$, $.getRecipeType())));
        forEachCategories(RecipeTypes.LIGHTNING_CHANNELING, $ -> registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, Items.LIGHTNING_ROD.getDefaultInstance(), $.getRecipeType()));
        for (Item item : CommonProxy.tagElements(BuiltInRegistries.ITEM, LycheeTags.ITEM_EXPLODING_CATALYSTS)) {
            forEachCategories(RecipeTypes.ITEM_EXPLODING, $ -> registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, item.getDefaultInstance(), $.getRecipeType()));
        }
        for (Item item : CommonProxy.tagElements(BuiltInRegistries.ITEM, LycheeTags.BLOCK_EXPLODING_CATALYSTS)) {
            forEachCategories(RecipeTypes.BLOCK_EXPLODING, $ -> registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, item.getDefaultInstance(), $.getRecipeType()));
        }
        forEachCategories(RecipeTypes.DRIPSTONE_DRIPPING, $ -> registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, Items.POINTED_DRIPSTONE.getDefaultInstance(), $.getRecipeType()));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        RUNTIME = jeiRuntime;
        Minecraft.getInstance().execute(() -> {
            List<CraftingRecipe> recipes = CommonProxy.recipes(net.minecraft.world.item.crafting.RecipeType.CRAFTING).stream().filter(ILycheeRecipe.class::isInstance).map(ILycheeRecipe.class::cast).filter(Predicate.not(ILycheeRecipe::showInRecipeViewer)).map(CraftingRecipe.class::cast).toList();
            jeiRuntime.getRecipeManager().hideRecipes(mezz.jei.api.constants.RecipeTypes.CRAFTING, recipes);
        });
    }

    public static class ScreenElementWrapper implements IDrawable {

        private final ScreenElement element;

        private int width = 16;

        private int height = 16;

        private ScreenElementWrapper(AllGuiTextures element) {
            this.element = element;
            this.width = element.width;
            this.height = element.height;
        }

        public ScreenElementWrapper(RenderElement element) {
            this.element = element;
            this.width = element.getWidth();
            this.height = element.getHeight();
        }

        @Override
        public void draw(GuiGraphics graphics, int x, int y) {
            this.element.render(graphics, x, y);
        }

        @Override
        public int getHeight() {
            return this.height;
        }

        @Override
        public int getWidth() {
            return this.width;
        }
    }

    public static enum SlotType {

        NORMAL(AllGuiTextures.JEI_SLOT), CHANCE(AllGuiTextures.JEI_CHANCE_SLOT), CATALYST(AllGuiTextures.JEI_CATALYST_SLOT);

        final IDrawable element;

        private SlotType(AllGuiTextures element) {
            this.element = JEICompat.el(element);
        }
    }
}